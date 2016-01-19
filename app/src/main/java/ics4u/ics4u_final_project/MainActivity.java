package ics4u.ics4u_final_project;

import android.app.Activity;
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
import java.util.Stack;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager lLayout;
    public static ArrayList<Recipe> importedRecipes;
    public static SharedPreferences prefs = null;
    RecyclerView rView;
    RecipeAdapter rcAdapter;
    static Activity fa;
    Stack<Recipe> deleted = new Stack<>();
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
            Intent intent = new Intent(this,Settings.class);
            startActivity(intent);
        }
        if (id == R.id.action_refresh) {
            if (deleted.isEmpty()){
                Toast.makeText(MainActivity.this, "No recipes to restore", Toast.LENGTH_LONG).show();
            }else {
                importedRecipes.add(deleted.pop());
                rebuildSaves();
                Toast.makeText(MainActivity.this, "Last deleted recipe restored", Toast.LENGTH_LONG).show();

            }
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
    protected void onRestart(){
        super.onRestart();
        rebuildSaves();
    }

    public void rebuildSaves(){
        //rebuild imported recipes
        File recipeFolder = new File(this.getFilesDir() + "/recipes/");
        File[] listOfFiles = recipeFolder.listFiles();
        for (File file : listOfFiles) {
            System.out.println(file.delete());
        }
        for (int i = 0; i < importedRecipes.size(); i++){
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


}
