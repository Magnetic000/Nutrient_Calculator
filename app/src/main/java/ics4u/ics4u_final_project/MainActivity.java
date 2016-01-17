package ics4u.ics4u_final_project;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager lLayout;
    public static ArrayList<Recipe> importedRecipes;
    public static SharedPreferences prefs = null;
    RecyclerView rView;
    RecipeAdapter rcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IngredientSelectionActivity.onIngredient = false;
        Database.importData(this);
        prefs = getSharedPreferences("ics4u.ics4u_final_project", MODE_PRIVATE);
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
                                    importedRecipes.remove(position);
                                    rcAdapter.notifyItemRemoved(position);
                                }
                                rcAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    importedRecipes.remove(position);
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
            return true;
        }
        if (id == R.id.action_refresh) {
            Toast.makeText(MainActivity.this, "Refresh App", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }


    public void createRecipe() {
        RecyclerViewHolders.edit = false;
        Intent intent = new Intent(this, RecipeCreateActivity.class);
        finish();
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
    protected void onRestart(){
        super.onRestart();
        //rebuild imported recipes
        File recipeFolder = new File(this.getFilesDir() + "/recipes/");
        recipeFolder.mkdir();
        RecipeCreateActivity.onRecipe = false;
        for (Recipe r: importedRecipes){
            try {
                r.save(new File(getFilesDir() + "/recipes/" + r.getTitle() + ".xml"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        importedRecipes = Database.importRecipes(this);
        rcAdapter = new RecipeAdapter(MainActivity.this, importedRecipes);
        rView.setAdapter(rcAdapter);
    }


}
