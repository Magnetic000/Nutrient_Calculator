/*
This is the class for the activity that allows the user to make a new or edit an existing recipe
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
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RecipeCreateActivity extends AppCompatActivity {
    static Recipe recipe;
    static boolean addedIngred;
    static boolean onRecipe, search;
    static RecyclerView rView;
    ImageView iconContextMenu;
    EditText quanAmt, quanNm;
    int index = -1;
    RecipeCreateAdapter rcAdapter;
    Stack<Ingredient> deleted = new Stack<>();
    private Toolbar mToolbar;
    private LinearLayoutManager lLayoutIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRecipe = true;
        setContentView(R.layout.rv_recipecreate);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Create A Recipe");
        setSupportActionBar(mToolbar);
        addedIngred = false;


        final Button button = (Button) findViewById(R.id.instructions_button);
        button.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {
                        launchInstructions();
                    }
                }
        );
        FloatingActionButton FAB = (FloatingActionButton) findViewById(R.id.add_ingredient_button);
        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = true;
                launchIngredients();
            }
        });
        //list for the cards
        final List<Ingredient> rowListItem;
        quanAmt = (EditText) findViewById(R.id.quantity_amount);
        quanNm = (EditText) findViewById(R.id.quantity_type);
        if (RecyclerViewHolders.edit) {
            index = RecyclerViewHolders.location;
            //get the recipe that was clicked on
            recipe = MainActivity.importedRecipes.get(index);
            //set each of the elements to the specified ones in the recipe
            TextView name = (TextView) findViewById(R.id.recipe_name);
            name.setText(recipe.getTitle());
            ImageView recipeIcon = (ImageView) findViewById(R.id.recipe_icon);
            recipeIcon.setImageDrawable(getResources().getDrawable(recipe.getPhoto()));
            if (recipe.getIngredients().size() > 0) {
                addedIngred = true;
            }
            quanAmt.setText(String.valueOf(recipe.getServings()));
            quanNm.setText(recipe.getServingName());
            rcAdapter = new RecipeCreateAdapter(RecipeCreateActivity.this, recipe.getIngredients());
        } else {
            //create a new recipe
            recipe = new Recipe();
            //set a default photo
            recipe.setPhoto(R.drawable.banana);
            rowListItem = getAllItemList();
            rcAdapter = new RecipeCreateAdapter(RecipeCreateActivity.this, rowListItem);

        }
        lLayoutIngredient = new LinearLayoutManager(RecipeCreateActivity.this);
        rView = (RecyclerView) findViewById(R.id.recycler_view_recipe);
        rView.setLayoutManager(lLayoutIngredient);
        rView.setAdapter(rcAdapter);
        clickingIcon();

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
                                    if (recipe.getIngredients().size() > 0) {
                                        deleted.push(recipe.getIngredients().get(position));
                                        recipe.getIngredients().remove(position);
                                        rcAdapter.notifyItemRemoved(position);
                                    }
                                }
                                rcAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    if (recipe.getIngredients().size() > 0) {
                                        deleted.push(recipe.getIngredients().get(position));
                                        recipe.getIngredients().remove(position);
                                        rcAdapter.notifyItemRemoved(position);
                                    }
                                }
                                rcAdapter.notifyDataSetChanged();
                            }
                        });

        rView.addOnItemTouchListener(swipeTouchListener);
    }

    /**
     * creates a default card in the list to promt the user
     *
     * @return the list with the default card
     */
    private List<Ingredient> getAllItemList() {
        //add a default ingredient
        List<Ingredient> allItems = new ArrayList<>();
        allItems.add(new Ingredient(-1, "Add Ingredients"));
        allItems.get(0).setFormattedName("Add Ingredients using the floating button.");
        return allItems;
    }

    /**
     * launches the instructions activity
     */
    public void launchInstructions() {
        Intent intent = new Intent(this, InstructionCreator.class);
        startActivity(intent);
    }

    /**
     * launches the ingredient selection activity
     */
    public void launchIngredients() {
        Intent intent = new Intent(this, IngredientSelectionActivity.class);
        startActivity(intent);
    }

    //    public static void updateAdapter(){
//        RecipeCreateAdapter rcAdapter = new RecipeCreateAdapter(this.getApplicationContext(), recipe.getIngredients());
//        rView.setAdapter(rcAdapter);
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_print) {
            //save the recipe
            saveRecipe();
            File pdfFolder = new File("/sdcard/Recipes/");

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                if (!pdfFolder.isDirectory()) {
                    pdfFolder.mkdir();
                }
                //export to PDF
                recipe.export(new File("/sdcard/Recipes/", recipe.getTitle() + ".pdf"));
                //open the PDF
                Intent i = new Intent(Intent.ACTION_VIEW);
                System.out.println(getFilesDir().toString());
                i.setDataAndType(Uri.fromFile(new File("/sdcard/Recipes/" + recipe.getTitle() + ".pdf")), "application/pdf");
                startActivity(i);
            } else {
                Toast.makeText(this, "Permission denied. Please grant storage permissions", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.action_undo) {
            //they haven't deleted anything
            if (deleted.isEmpty()) {
                Toast.makeText(this, "No ingredients to restore", Toast.LENGTH_LONG).show();
            } else {
                recipe.getIngredients().add(deleted.pop());
                RecipeCreateAdapter rcAdapter = new RecipeCreateAdapter(RecipeCreateActivity.this, recipe.getIngredients());
                rView.setAdapter(rcAdapter);
                //save the recipe
                saveRecipe();
                Toast.makeText(this, "Last deleted ingredient restored", Toast.LENGTH_LONG).show();
            }
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Change recipe icon");
        inflater.inflate(R.menu.menu_icon, menu);
    }

    /**
     * Called when an item on the photo menu is selected
     *
     * @param item
     * @return
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            case R.id.icon1:
                iconContextMenu.setImageResource(R.drawable.banana);
                recipe.setPhoto(R.drawable.banana);
                return true;
            case R.id.icon2:
                iconContextMenu.setImageResource(R.drawable.fish);
                recipe.setPhoto(R.drawable.fish);
                return true;
            case R.id.icon3:
                iconContextMenu.setImageResource(R.drawable.cake);
                recipe.setPhoto(R.drawable.cake);
                return true;
            case R.id.icon4:
                iconContextMenu.setImageResource(R.drawable.cookies);
                recipe.setPhoto(R.drawable.cookies);
                return true;
            case R.id.icon5:
                iconContextMenu.setImageResource(R.drawable.dairy);
                recipe.setPhoto(R.drawable.dairy);
                return true;
            case R.id.icon6:
                iconContextMenu.setImageResource(R.drawable.muffin);
                recipe.setPhoto(R.drawable.muffin);
                return true;
            case R.id.icon7:
                iconContextMenu.setImageResource(R.drawable.sandwich);
                recipe.setPhoto(R.drawable.sandwich);
                return true;
            case R.id.icon8:
                iconContextMenu.setImageResource(R.drawable.soup);
                recipe.setPhoto(R.drawable.soup);
                return true;
            case R.id.icon9:
                iconContextMenu.setImageResource(R.drawable.wheat);
                recipe.setPhoto(R.drawable.wheat);
                return true;
            default:
                return false;
        }
    }

    public void clickingIcon() {
        iconContextMenu = (ImageView) findViewById(R.id.recipe_icon);
        iconContextMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                registerForContextMenu(iconContextMenu);
                return false;
            }
        });
    }

    /**
     * refreshes the ingredients and saves the recipe
     */
    @Override
    protected void onRestart() {
        //update the ingredients
        super.onRestart();
        RecipeCreateAdapter rcAdapter = new RecipeCreateAdapter(RecipeCreateActivity.this, recipe.getIngredients());
        rView.setAdapter(rcAdapter);
        //save the recipe
        saveRecipe();
        onRecipe = true;
    }

    /**
     * deletes all the save files and saves the recies currently in memory
     */
    public void saveRecipe() {
        //get the name of the recipe
        EditText nameBox = (EditText) findViewById(R.id.recipe_name);
        recipe.setTitle(nameBox.getText().toString());
        recipe.setServings(Integer.parseInt(quanAmt.getText().toString()));
        recipe.setServingName(quanNm.getText().toString());
        //save the recipe to the correct spot on the imported recipes
        if (index >= 0) {
            MainActivity.importedRecipes.set(index, recipe);
        } else {
            //add it as a new recipe if it's new, and set the edit to the correct ingredient
            MainActivity.importedRecipes.add(recipe);
            index = MainActivity.importedRecipes.size() - 1;
//            //save it
//            try {
//                recipe.save(new File(getFilesDir() + "/recipes/" + recipe.getTitle() + ".xml"));
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
        }
        //rebuild saves
        //rebuild imported recipes
        // TODO: 1/18/2016 find a way to link this with the duplicate method in the main activity
        File recipeFolder = new File(this.getFilesDir() + "/recipes/");
        File[] listOfFiles = recipeFolder.listFiles();
        //delete all the files in the folder
        for (File file : listOfFiles) {
            System.out.println(file.delete());
        }
        //resave all the recipes that are in memory
        for (int i = 0; i < MainActivity.importedRecipes.size(); i++) {
            try {
                MainActivity.importedRecipes.get(i).save(new File(this.getFilesDir() + "/recipes/" + i + ".xml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * called when the back button is pressed on a recipe
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //refresh the saves
        saveRecipe();
    }

}
