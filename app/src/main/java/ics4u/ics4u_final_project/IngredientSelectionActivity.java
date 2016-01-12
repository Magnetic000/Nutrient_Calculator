package ics4u.ics4u_final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class IngredientSelectionActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText edtSeach;
    private LinearLayoutManager lLayoutIngredient;
    ArrayList<Ingredient> results = new ArrayList<>(), catResults = new ArrayList<>();
    String[] ingredientCategories;
    ArrayAdapter<String> adapter;
    Spinner ingredientDropdown;
    IngredientAdapter rcAdapter;
    List<Ingredient> rowListItem;
    RecyclerView rView;
    static boolean onIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onIngredient = true;
        results.add(new Ingredient(0,"Search for an Ingredient. Use commas to separate keywords."));
        setContentView(R.layout.rv_ingredientselect);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Create A Recipe");
        setSupportActionBar(mToolbar);
        rowListItem = getAllItemList();
        lLayoutIngredient = new LinearLayoutManager(IngredientSelectionActivity.this);

        rView = (RecyclerView) findViewById(R.id.recycler_view_ingredient);
        rView.setLayoutManager(lLayoutIngredient);

        rcAdapter = new IngredientAdapter(IngredientSelectionActivity.this, rowListItem);

        AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selCat = ingredientCategories[position].toString();
                System.out.println(selCat);
                if (!selCat.equals("Show all") && !selCat.equals("Show All") && !selCat.equals("")) {
                    catResults = (ArrayList<Ingredient>) results.clone();
                    for (int i = catResults.size() - 1; i >= 0; i--) {
                        if (catResults.get(i).getName().contains(",")) {
                            if (!catResults.get(i).getName().substring(0, catResults.get(i).getName().indexOf(",")).equals(selCat)) {
                                catResults.remove(i);
                            }
                        } else if (!catResults.get(i).getName().equals(selCat)) {
                            catResults.remove(i);
                        }
                    }
                    rcAdapter = new IngredientAdapter(IngredientSelectionActivity.this, catResults);
                    rView.setAdapter(rcAdapter);
                } else {
                    System.out.println("reset");
                    rcAdapter = new IngredientAdapter(IngredientSelectionActivity.this, results);
                    rView.setAdapter(rcAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        ingredientDropdown = (Spinner) findViewById(R.id.category_combobox);
        ingredientCategories = new String[]{"Show all"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ingredientCategories);
        ingredientDropdown.setPrompt("Please select a category of ingredient");
        ingredientDropdown.setAdapter(adapter);
        ingredientDropdown.setOnItemSelectedListener(onSpinner);


    }

    private List<Ingredient> getAllItemList() {

        return results;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch() {
        ActionBar action = getSupportActionBar(); //get the actionbar

        if (isSearchOpened) { //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.search));

            isSearchOpened = false;
        } else { //open the search entry

            action.setDisplayShowCustomEnabled(true); //enable it to display a
            // custom view in the action bar.
            action.setCustomView(R.layout.search_bar);//add the custom view
            action.setDisplayShowTitleEnabled(false); //hide the title

            edtSeach = (EditText) action.getCustomView().findViewById(R.id.edtSearch); //the text editor

            //this is a listener to do a search when the user clicks on search button
            edtSeach.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(edtSeach.getWindowToken(), 0);
                        doSearch(true);
                        return true;
                    }
                    return false;
                }
            });


            edtSeach.requestFocus();

            //open the keyboard focused in the edtSearch
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtSeach, InputMethodManager.SHOW_IMPLICIT);


            //add the close icon
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.close));

            isSearchOpened = true;
        }
    }

    private void doSearch(boolean fetchCats) {
        String searchText = edtSeach.getText().toString();
        if (searchText.length() < 2) {
            Toast.makeText(getBaseContext(), "Search keyword too short, please be more specific", Toast.LENGTH_SHORT).show();
        } else {
            results = Database.search(searchText);
            System.out.println("Search Done");
            quickSort(results, 0, results.size() - 1);
            //TextView t = (TextView) findViewById(R.id.textView);
            //System.out.println(results.size());
            //t.setText(results.get(0).getName());
            if (results.isEmpty()) {
                Toast.makeText(getBaseContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
            } else {
                if (fetchCats) {
                    //add the matches to the list model
                    ArrayList<String> cats = new ArrayList<>();
                    cats.add("Show All");
                    for (int i = 0; i < results.size(); i++) {
                        if (fetchCats) {
                            if (results.get(i).getName().contains(",")) {
                                cats.add(results.get(i).getName().substring(0, results.get(i).getName().indexOf(",")));
                            } else {
                                cats.add(results.get(i).getName());
                            }
                        }
                    }
                    for (int i = 0; i < cats.size(); i++) {
                        for (int j = i + 1; j < cats.size(); j++) {
                            if (cats.get(i).equals(cats.get(j))) {
                                cats.remove(j);
                                j--;
                            }
                        }
                    }
                    System.out.println(cats.size());
                    String[] categories = new String[cats.size()];
                    for (int i = 0; i < cats.size(); i++) {
                        categories[i] = cats.get(i);
                        //System.out.println(categories[i]);
                    }
                    quickSort(categories, 1, categories.length - 1);
                    ingredientCategories = categories;
                    adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ingredientCategories);
                    ingredientDropdown.setAdapter(adapter);
                }
                lLayoutIngredient = new LinearLayoutManager(IngredientSelectionActivity.this);

                rView = (RecyclerView) findViewById(R.id.recycler_view_ingredient);
                rView.setLayoutManager(lLayoutIngredient);

                rcAdapter = new IngredientAdapter(IngredientSelectionActivity.this, results);
                rView.setAdapter(rcAdapter);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    /**
     * This method sorts an array of strings in ascending lexicographical order
     *
     * @param list the list to sort
     * @param low  the low index of the list to sort
     * @param high the high index of the list to sort
     */
    public static void quickSort(String[] list, int low, int high) {
        //only do this while the size of the array is at least 1
        if (low < high) {
            //set the pivot to the middle, the left to the low index, right to the high index
            int l = low, r = high;
            String pivot = list[(high + low) / 2];
            //loop while the left and right have not passed each other
            while (l <= r) {
                //decrement the right index until it finds a number out of place
                while (list[r].compareToIgnoreCase(pivot) > 0) {
                    r--;
                }
                //increment the left index until it finds a number out of place
                while (list[l].compareToIgnoreCase(pivot) < 0) {
                    l++;
                }
                //if the left and right don't overlap, swap l and r
                if (l <= r) {
                    String temp = list[l];
                    list[l] = list[r];
                    list[r] = temp;
                    l++;
                    r--;
                }
            }
            //do the preceding process on the left and right sides of the partition
            quickSort(list, low, r);
            quickSort(list, l, high);
        }
    }

    /**
     * This method sorts an array of strings in ascending lexicographical order
     *
     * @param list the list to sort
     * @param low  the low index of the list to sort
     * @param high the high index of the list to sort
     */
    public static void quickSort(ArrayList<Ingredient> list, int low, int high) {
        //only do this while the size of the array is at least 1
        if (low < high) {
            //set the pivot to the middle, the left to the low index, right to the high index
            int l = low, r = high;
            String pivot = list.get((high + low) / 2).getName();
            //loop while the left and right have not passed each other
            while (l <= r) {
                //decrement the right index until it finds a number out of place
                while (list.get(r).getName().compareToIgnoreCase(pivot) > 0) {
                    r--;
                }
                //increment the left index until it finds a number out of place
                while (list.get(l).getName().compareToIgnoreCase(pivot) < 0) {
                    l++;
                }
                //if the left and right don't overlap, swap l and r
                if (l <= r) {
                    Ingredient temp = list.get(l);
                    list.set(l, list.get(r));
                    list.set(r, temp);
                    l++;
                    r--;
                }
            }
            //do the preceding process on the left and right sides of the partition
            quickSort(list, low, r);
            quickSort(list, l, high);
        }
    }

    public void launchMeasure(){
        Intent intent = new Intent(this, IngredientSelectionActivity.class);
        startActivity(intent);
    }
}
