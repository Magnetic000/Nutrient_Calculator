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
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    RecyclerView rView;
    RecipeAdapter rcAdapter;
    Stack<Recipe> deleted = new Stack<>();
    private LinearLayoutManager lLayout;

    /**
     * When the activity starts
     * Runs through this method every time the activity is reloaded from scratch
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Store value stating user isn't on ingredient selection activity
        IngredientSelectionActivity.onIngredient = false;
        //Import data
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

        //Link .java file with correct xml for visuals
        setContentView(R.layout.rv_mainactivity);
        //Set title to nothing
        setTitle(null);
        //Find reference location of toolbar
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        //Set toolbar with logo
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.drawable.logo_updated);
        //Instantiate LLM to store RecyclerView
        lLayout = new LinearLayoutManager(MainActivity.this);
        //Find reference location of recycler view, container for cards
        rView = (RecyclerView) findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);
        //Instantiate adapter, something used to push information to cards
        //Pass data to be stored
        rcAdapter = new RecipeAdapter(MainActivity.this, importedRecipes);
        rView.setAdapter(rcAdapter);
        //Floating action button
        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.add_recipe_button);
        //Check to see if user clicked the button
        //Start recipe creating activity
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRecipe();
            }
        });
        //Checks if user has swiped to delete recipe
        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(rView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            //Allow user to swipe an existing card
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            //When user swipes left
                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    //add deleted to a stack of the deleted recipes
                                    deleted.push(importedRecipes.get(position).clone());
                                    //remove it from the list
                                    importedRecipes.remove(position);
                                    //Update cards
                                    rebuildSaves();
                                    rcAdapter.notifyItemRemoved(position);
                                }
                                rcAdapter.notifyDataSetChanged();
                            }

                            //When user swipes right
                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    //add deleted to a stack of the deleted recipes
                                    deleted.push(importedRecipes.get(position).clone());
                                    //remove it from the list

                                    importedRecipes.remove(position);
                                    rebuildSaves();
                                    //Update cards
                                    rcAdapter.notifyItemRemoved(position);
                                }
                                rcAdapter.notifyDataSetChanged();
                            }
                        });

        rView.addOnItemTouchListener(swipeTouchListener);
    }

    /**
     * Creating menu
     *
     * @param menu the toolbar of the activity
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Runs this method when the user selects an option on the menu
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //If user presses the undo button
        if (id == R.id.action_undo) {

            //check to see if there are any restorable recipes
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
        //If user pressed the about button
        if (id == R.id.action_about) {
            //Begin about activity
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Starts recipe create activity and updates variables
     */
    public void createRecipe() {
        RecyclerViewHolders.edit = false;
        Intent intent = new Intent(this, RecipeCreateActivity.class);
        startActivity(intent);
    }

    /**
     * Calls function every time user returns to this activity
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        //update the saved recipes
        rebuildSaves();
        //updates variables stating user is no longer on recipe creating activity
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
     *
     * @param requestCode  what I was asking the user
     * @param permissions  The permissions asked for
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
