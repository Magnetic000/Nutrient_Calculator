package ics4u.ics4u_final_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class RecipeCreateActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayoutManager lLayoutIngredient;
    static Recipe recipe;
    static boolean addedIngred;
    static boolean onRecipe, search;
    static RecyclerView rView;
    ImageView iconContextMenu;
    EditText quanAmt, quanNm;
    int index = -1;
    RecipeCreateAdapter rcAdapter;

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
            rowListItem = recipe.getIngredients();
            ImageView recipeIcon = (ImageView) findViewById(R.id.recipe_icon);
            recipeIcon.setImageDrawable(getResources().getDrawable(recipe.getPhoto()));
            if (recipe.getIngredients().size() > 0) {
                addedIngred = true;
            }
            quanAmt.setText(String.valueOf(recipe.getServings()));
            quanNm.setText(recipe.getServingName());
        } else {
            //create a new recipe
            recipe = new Recipe();
            //set a default photo
            recipe.setPhoto(R.drawable.banana);
            rowListItem = getAllItemList();
        }

        lLayoutIngredient = new LinearLayoutManager(RecipeCreateActivity.this);

        rView = (RecyclerView) findViewById(R.id.recycler_view_recipe);
        rView.setLayoutManager(lLayoutIngredient);

        rcAdapter = new RecipeCreateAdapter(RecipeCreateActivity.this, rowListItem);
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
                                    rowListItem.remove(position);
                                    rcAdapter.notifyItemRemoved(position);
                                }
                                rcAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    rowListItem.remove(position);
                                    rcAdapter.notifyItemRemoved(position);
                                }
                                rcAdapter.notifyDataSetChanged();
                            }
                        });

        rView.addOnItemTouchListener(swipeTouchListener);
    }

    private List<Ingredient> getAllItemList() {
        //add a default ingredient
        List<Ingredient> allItems = new ArrayList<>();
        allItems.add(new Ingredient(-1, "Add Ingredients"));
        allItems.get(0).setFormattedName("Add Ingredients using the floating button.");
        return allItems;
    }

    public void launchInstructions() {
        Intent intent = new Intent(this, InstructionCreator.class);
        startActivity(intent);
    }

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
            //export to PDF
            recipe.export(new File("/sdcard/Recipes/", recipe.getTitle() + ".pdf"));
            //open the PDF
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(new File("/sdcard/Recipes/" + recipe.getTitle() + ".pdf")), "application/pdf");
            startActivity(i);
        } else if (id == R.id.action_undo){
            //UNDO ISACC INSERT CODE HERE
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
