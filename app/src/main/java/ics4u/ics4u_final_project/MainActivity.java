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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

    private LinearLayoutManager lLayout;
    public static ArrayList<Recipe> importedRecipes;
    public static SharedPreferences prefs = null;
    RecyclerView rView;
    RecipeAdapter rcAdapter;
    static Activity fa;
    Stack<Recipe> deleted = new Stack<>();
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IngredientSelectionActivity.onIngredient = false;
        Database.importData(this);
        prefs = getSharedPreferences("ics4u.ics4u_final_project", MODE_PRIVATE);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permissions);
//        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
        importedRecipes = Database.importRecipes(this);
        importedRecipes.get(0).setPhoto(R.drawable.banana);
        //        importedRecipes.get(0).export(new File("/sdcard/Recipes/", importedRecipes.get(0).getTitle() + ".pdf"));


        setContentView(R.layout.rv_mainactivity);
        setTitle(null);

        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.drawable.logo);
        topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));

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
                                    deleted.push(importedRecipes.get(position).clone());
                                    importedRecipes.remove(position);
                                    rebuildSaves();
                                    rcAdapter.notifyItemRemoved(position);
                                }
                                rcAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    deleted.push(importedRecipes.get(position).clone());
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, Settings.class);
            startActivity(intent);
        }
        if (id == R.id.action_undo) {
            if (deleted.isEmpty()) {
                Toast.makeText(MainActivity.this, "No recipes to restore", Toast.LENGTH_LONG).show();
            } else {
                importedRecipes.add(deleted.pop());
                rebuildSaves();
                Toast.makeText(MainActivity.this, "Last deleted recipe restored", Toast.LENGTH_LONG).show();

            }
        }
        if (id == R.id.action_unit_conversion){
            Intent intent = new Intent(this, UnitConverter.class);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        rebuildSaves();
        RecipeCreateActivity.onRecipe = false;
    }

    public void rebuildSaves() {
        //rebuild imported recipes
        File recipeFolder = new File(this.getFilesDir() + "/recipes/");
        File[] listOfFiles = recipeFolder.listFiles();
        for (File file : listOfFiles) {
            System.out.println(file.delete());
        }
        for (int i = 0; i < importedRecipes.size(); i++) {
            try {
                importedRecipes.get(i).save(new File(this.getFilesDir() + "/recipes/" + i + ".xml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        importedRecipes = Database.importRecipes(this);
        rcAdapter = new RecipeAdapter(MainActivity.this, importedRecipes);
        rView.setAdapter(rcAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permission Granted
                    File pdfFolder = new File("/sdcard/Recipes/");
                    if (!pdfFolder.isDirectory()) {
                        pdfFolder.mkdir();
                    }
                    return;
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
