package ics4u.ics4u_final_project;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecipeCreateActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayoutManager lLayoutIngredient;
    static Recipe recipe;
    boolean addedIngred;
    static boolean onRecipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onRecipe = true;
        setContentView(R.layout.rv_recipecreate);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Create A Recipe");
        setSupportActionBar(mToolbar);
        setTitle(null);
        addedIngred = false;


        final Button button = (Button)findViewById(R.id.instructions_button);
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
                launchIngredients();
            }
        });
        List<Ingredient> rowListItem;
        if (RecyclerViewHolders.edit){
            recipe = MainActivity.importedRecipes.get(RecyclerViewHolders.location);
            TextView name = (TextView) findViewById(R.id.recipe_name);
            name.setText(recipe.getTitle());
            rowListItem = recipe.getIngredients();
        } else {
            recipe = new Recipe();
            rowListItem = getAllItemList();
        }

        lLayoutIngredient = new LinearLayoutManager(RecipeCreateActivity.this);

        RecyclerView rView = (RecyclerView) findViewById(R.id.recycler_view_recipe);
        rView.setLayoutManager(lLayoutIngredient);

        RecipeCreateAdapter rcAdapter = new RecipeCreateAdapter(RecipeCreateActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
    }
    private List<Ingredient> getAllItemList() {
        List<Ingredient> allItems = new ArrayList<>();
        allItems.add(new Ingredient(-1, "Add Ingredients"));
        return allItems;
    }

    public void launchInstructions(){
        Intent intent = new Intent(this,InstructionCreator.class);
        startActivity(intent);
    }
    public void launchIngredients(){
        Intent intent = new Intent(this,IngredientSelectionActivity.class);
        startActivity(intent);
    }
}
