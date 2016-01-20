/*
The main activity in the app. It launches first (after the splash screen)
 */
/*
Copyright (C) 2016 Isaac Wismer & Andrew Xu

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ics4u.ics4u_final_project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<Recipe> importedRecipes;
    public static SharedPreferences prefs = null;
    static Activity fa;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    RecyclerView rView;
    RecipeAdapter rcAdapter;
    Stack<Recipe> deleted = new Stack<>();
    private LinearLayoutManager lLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IngredientSelectionActivity.onIngredient = false;
        Database.importData(this);
        prefs = getSharedPreferences("ics4u.ics4u_final_project", MODE_PRIVATE);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissions);
//        }
        //Checking if the app is running on marshmallow
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            //check if we have write permissions
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //if we don't ask for it
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && hasWriteContactsPermission == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
        //begin importing the saved recipes
        importedRecipes = Database.importRecipes(this);
        importedRecipes.get(0).setPhoto(R.drawable.banana);
        //        importedRecipes.get(0).export(new File("/sdcard/Recipes/", importedRecipes.get(0).getTitle() + ".pdf"));


        setContentView(R.layout.rv_mainactivity);
        setTitle(null);

        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.drawable.logo_updated);

        lLayout = new LinearLayoutManager(MainActivity.this);

        rView = (RecyclerView) findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        rcAdapter = new RecipeAdapter(MainActivity.this, importedRecipes);
        rView.setAdapter(rcAdapter);
        System.out.println("reached");
        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.add_recipe_button);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRecipe();
            }
        });


        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    //add deleted to a stack of the deleted recipes
                                    deleted.push(importedRecipes.get(position).clone());
                                    //remove it from the list
                                    importedRecipes.remove(position);
                                    rebuildSaves();
                                    rcAdapter.notifyItemRemoved(position);
                                }
                                rcAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    //add deleted to a stack of the deleted recipes
                                    deleted.push(importedRecipes.get(position).clone());
                                    //remove it from the list
                                    importedRecipes.remove(position);
                                    rebuildSaves();
                                    rcAdapter.notifyItemRemoved(position);
                                }
                                rcAdapter.notifyDataSetChanged();
                            }
                        });

        rView.addOnItemTouchListener(swipeTouchListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_undo) {
            //if there are no recipes to restore
            if (deleted.isEmpty()) {
                Toast.makeText(MainActivity.this, "No recipes to restore", Toast.LENGTH_LONG).show();
            } else {
                //add the last deleted back to the list of recipes
                importedRecipes.add(deleted.pop());
                //refresh the saves
                rebuildSaves();
                //notify user
                Toast.makeText(MainActivity.this, "Last deleted recipe restored", Toast.LENGTH_LONG).show();

            }
        }
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    public void createRecipe() {
        RecyclerViewHolders.edit = false;
        System.out.println("clicked the button");
        Intent intent = new Intent(this, RecipeCreateActivity.class);
        startActivity(intent);
    }

    public void buttonCode() {
        /*final Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(
            new Button.OnClickListener(){
                public void onClick (View v){
                    button.setText("Clicked");
                }
            }
        );

        button.setOnLongClickListener(
                new Button.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        button.setText("long");
                        return false;
                    }
                }
        );*/

    }

    /**
     * called when the main activity comes back into focus
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        //update the saved recipes
        rebuildSaves();
        RecipeCreateActivity.onRecipe = false;
    }

    public void rebuildSaves() {
        //rebuild imported recipes
        //go through the files in the save directory
        File recipeFolder = new File(this.getFilesDir() + "/recipes/");
        File[] listOfFiles = recipeFolder.listFiles();
        //delete them all
        for (File file : listOfFiles) {
            System.out.println(file.delete());
        }
        //resave each of the recipes
        for (int i = 0; i < importedRecipes.size(); i++) {
            try {
                importedRecipes.get(i).save(new File(this.getFilesDir() + "/recipes/" + i + ".xml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        //reimport the recipes
        importedRecipes = Database.importRecipes(this);
        //refresh the list
        rcAdapter = new RecipeAdapter(MainActivity.this, importedRecipes);
        rView.setAdapter(rcAdapter);
    }

    /**
     * called when the user accepts/rejects a permission request
     * @param requestCode what I was asking the user
     * @param permissions The permissions asked for
     * @param grantResults whether or not they were granted
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                //if they granted
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                    //try and make a folder
                    File pdfFolder = new File("/sdcard/Recipes/");
                    if (!pdfFolder.isDirectory()) {
                        pdfFolder.mkdir();
                    }
                    return;
                } else {
                    // Permission Denied
                    //tell them what they have done
                    Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
