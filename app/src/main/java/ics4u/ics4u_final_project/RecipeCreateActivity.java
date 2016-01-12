package ics4u.ics4u_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecipeCreateActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private LinearLayoutManager lLayoutIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rv_recipecreate);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Create A Recipe");
        setSupportActionBar(mToolbar);
        setTitle(null);


        List<Recipe> rowListItem = getAllItemList();
        lLayoutIngredient = new LinearLayoutManager(RecipeCreateActivity.this);

        RecyclerView rView = (RecyclerView) findViewById(R.id.recycler_view_recipe);
        rView.setLayoutManager(lLayoutIngredient);

        RecipeAdapter rcAdapter = new RecipeAdapter(RecipeCreateActivity.this, rowListItem);
        rView.setAdapter(rcAdapter);
    }
    private List<Recipe> getAllItemList() {

        List<Recipe> allItems = new ArrayList<>();

        allItems.add(new Recipe("Breadsticks", R.drawable.bread));
        allItems.add(new Recipe("Fishsticks", R.drawable.fish));
        allItems.add(new Recipe("Banana Bread", R.drawable.banana));
        allItems.add(new Recipe("Seafood", R.drawable.crab));
        allItems.add(new Recipe("Soup", R.drawable.soup));

        return allItems;
    }
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

        if (id == R.id.action_new) {
            Intent intent = new Intent(this,IngredientSelectionActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
