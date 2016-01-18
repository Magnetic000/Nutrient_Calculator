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
        List<Ingredient> rowListItem;
        if (RecyclerViewHolders.edit) {
            //get the recipe that was clicked on
            recipe = MainActivity.importedRecipes.get(RecyclerViewHolders.location);
            //set each of the elements to the specified ones in the recipe
            TextView name = (TextView) findViewById(R.id.recipe_name);
            name.setText(recipe.getTitle());
            rowListItem = recipe.getIngredients();
            ImageView recipeIcon = (ImageView) findViewById(R.id.recipe_icon);
            recipeIcon.setImageDrawable(getResources().getDrawable(recipe.getPhoto()));
            if (recipe.getIngredients().size() > 0) {
                addedIngred = true;
            }
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

        RecipeCreateAdapter rcAdapter = new RecipeCreateAdapter(RecipeCreateActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
        clickingIcon();
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
        if (id == R.id.action_add) {
            //save the recipe
            saveRecipe();
            //export to PDF
            recipe.export(new File("/sdcard/Recipes/", recipe.getTitle() + ".pdf"));
            //open the PDF
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setDataAndType(Uri.fromFile(new File("/sdcard/Recipes/" + recipe.getTitle() + ".pdf")), "application/pdf");
            startActivity(i);
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

    @Override
    protected void onRestart() {
        //update the ingredients
        super.onRestart();
        RecipeCreateAdapter rcAdapter = new RecipeCreateAdapter(RecipeCreateActivity.this, recipe.getIngredients());
        rView.setAdapter(rcAdapter);
        //save the recipe
        saveRecipe();
    }

    public void saveRecipe() {
        // TODO: 1/18/2016 make saving work when the name of the recipe changes
        //get the name of the recipe
        EditText nameBox = (EditText) findViewById(R.id.recipe_name);
        recipe.setTitle(nameBox.getText().toString());
        //save the recipe to the correct spot on the imported recipes
        if (RecyclerViewHolders.edit) {
            MainActivity.importedRecipes.set(RecyclerViewHolders.location, recipe);
            //rebuild imported recipes
            File recipeFolder = new File(this.getFilesDir() + "/recipes/");
            recipeFolder.mkdir();
            RecipeCreateActivity.onRecipe = false;
            for (Recipe r: MainActivity.importedRecipes){
                try {
                    r.save(new File(getFilesDir() + "/recipes/" + r.getTitle() + ".xml"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            MainActivity.importedRecipes = Database.importRecipes(this);
        } else {
            //add it as a new recipe if it's new, and set the edit to the correct ingredient
            MainActivity.importedRecipes.add(recipe);
            RecyclerViewHolders.location = MainActivity.importedRecipes.size() - 1;
            RecyclerViewHolders.edit = true;
            //save it
            try {
                recipe.save(new File(getFilesDir() + "/recipes/" + recipe.getTitle() + ".xml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        saveRecipe();
    }

}
