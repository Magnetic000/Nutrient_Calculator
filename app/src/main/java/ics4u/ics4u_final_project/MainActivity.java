package ics4u.ics4u_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutManager lLayout;
    public ArrayList<Recipe> importedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Database.importData(this);
        importedRecipes = Database.importRecipes(this);
        importedRecipes.get(0).setPhoto(R.drawable.shaq);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_mainactivity);
        setTitle(null);

        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
        topToolBar.setLogo(R.drawable.logo);
        topToolBar.setLogoDescription(getResources().getString(R.string.logo_desc));

        List<Recipe> rowListItem = getAllItemList();
        lLayout = new LinearLayoutManager(MainActivity.this);

        RecyclerView rView = (RecyclerView) findViewById(R.id.recycler_view);
        rView.setLayoutManager(lLayout);

        RecipeAdapter rcAdapter = new RecipeAdapter(MainActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);

//        for(int i = 0; i < Database.fdName.size(); i++){
//            System.out.println(Database.fdName.get(i)[1]);
//        }
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
        if (id == R.id.action_new) {
            createRecipe();
        }

        return super.onOptionsItemSelected(item);
    }

    private List<Recipe> getAllItemList() {

        List<Recipe> allItems = new ArrayList<>();
        System.out.println("Getting imported recipes");
        for (int i = 0; i < importedRecipes.size(); i++){
            allItems.add(importedRecipes.get(i));
            System.out.println("Title" + importedRecipes.get(i).getTitle());
        }
        allItems.add(new Recipe("Breadsticks", R.drawable.bread));
        allItems.add(new Recipe("Fishsticks", R.drawable.fish));
        allItems.add(new Recipe("Banana Bread", R.drawable.banana));
        allItems.add(new Recipe("Seafood", R.drawable.crab));
        allItems.add(new Recipe("Soup", R.drawable.soup));

        return allItems;
    }


    public void createRecipe() {
        Intent intent = new Intent(this, IngredientSelectionActivity.class);
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

}
