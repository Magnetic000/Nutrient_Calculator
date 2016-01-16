package ics4u.ics4u_final_project;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MeasureSelectionActivity extends AppCompatActivity {
    Ingredient selected;
    boolean edit;
    String[] types, items;
    Context c = this;
    Spinner measureType, measureSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measure_selector);
        if (RecipeCreateActivity.search) {
            selected = IngredientSelectionActivity.getResults().get(RecyclerViewHolders.location);
        } else {
            selected = RecipeCreateActivity.recipe.getSingleIngredientIndex(RecyclerViewHolders.location);
            //add making it show the right measure
        }
        RecipeCreateActivity.search = false;
        measureSize = (Spinner) findViewById(R.id.measurement_amount);
        measureType = (Spinner) findViewById(R.id.measurement_type);
        setTitle("Ingredient Amount");
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

        AdapterView.OnItemSelectedListener onSpinnerType = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSizeSpinner((int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        AdapterView.OnItemSelectedListener onSpinnerSize = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        TextView name = (TextView) findViewById(R.id.ingredient_title);
        name.setText(selected.getName());

        types = new String[]{"Metric Cooking Measures", "mL", "g", "Other"};

        items = new String[]{"1/4 Teaspoon", "1/2 Teaspoon", "1 Teaspoon", "1 Tablespoon", "1/4 Cup", "1/3 Cup", "1/2 Cup", "1 Cup"};
        edit = (selected.getUnit() != null);
        System.out.println("Edit: " + edit);
        if (!checkMeasuresML(selected.getID())) {
            types = new String[]{"g", "Other"};
            measureSize.setEnabled(false);
        } else {
            measureSize.setEnabled(true);
        }

        if (selected.getMeasures().isEmpty()) {
            Toast.makeText(this, "Error: No measures available. Cannot use ingredient; Please choose another.", Toast.LENGTH_LONG);
            this.finish();
        } else {
            if (edit) {
                while (selected.getSingleMeasureIndex(selected.getMeasures().size() - 1).getName().equals("")) { //prevents empty measures in the list
                    selected.getMeasures().remove(selected.getMeasures().size() - 1);
                }

            }
        }

        measureType.setPrompt("Please select a measure type");
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        measureType.setAdapter(typesAdapter);
        measureType.setOnItemSelectedListener(onSpinnerType);

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        measureSize.setPrompt("Please select a measure");
        measureSize.setAdapter(sizeAdapter);
        measureSize.setOnItemSelectedListener(onSpinnerSize);
        if (edit) {
            measureType.setSelection(selected.getUnitNum());
            setSizeSpinner(selected.getUnitNum());
            measureSize.setSelection(selected.getFractionNum());
            EditText measureQuantity = (EditText) findViewById(R.id.ingredient_amount);
            measureQuantity.setText(String.valueOf(selected.getQuantity()));
        }
    }

    /**
     * @param ID the ID of the measure
     * @return whether or not it can be measured in mL
     */
    public boolean checkMeasuresML(int ID) {
        //get conversion rates
        getConv(ID);
        //check the conversions for mL measurements
        for (int i = 0; i < selected.getMeasures().size(); i++) {
            int measureID = selected.getSingleMeasureIndex(i).getID();
            if (measureID > 340 && measureID < 380) {
                return true;
            } else if (measureID > 384 && measureID < 394) {
                return true;
            } else if (measureID > 412 && measureID < 427) {
                return true;
            } else if (measureID == 439 || measureID == 388
                    || measureID == 389 || measureID == 923
                    || measureID == 932 || measureID == 638
                    || measureID == 640 || measureID == 641
                    || measureID == 430 || measureID == 939
                    || measureID == 943 || measureID == 429
                    || measureID == 428 || measureID == 383) {
                return true;
            } else if (measureID >= 385 && measureID <= 387) {
                return true;
            }
        }
        return false;
    }//End checkMeasurementML()

    /**
     * This method may cause errors with the new arraylists. Check this if there
     * is an issue
     *
     * @param ID the ID of the measure
     */
    public void getConv(int ID) {
        System.out.println("Getting conversion rates for food ID = " + ID);
        //search for the nutrient ID in the file
        int begin = Database.binarySearch(Database.convFact, ID, 0, Database.convFact.size() - 1);
        if (begin == -1) {
            System.out.println("Not Found");
        }
        //step back to the beginning of the nutrient
        while (begin > 0 && (int) Database.convFact.get(begin - 1)[0] == ID) {
            begin--;
        }
        //read until the end of the nutrient
        for (int i = begin; (int) Database.convFact.get(i)[0] == ID; i++) {
            if ((int) Database.convFact.get(i)[1] != 1572) {
                selected.addMeasure((int) Database.convFact.get(i)[1], (Double) Database.convFact.get(i)[2]);
            }
        }
//        for (int i = 0; i < convFact.size(); i++) {
//            //get the measure conversion factor and measure ID
//            if (ID == (int) convFact.get(i)[0] && (int) convFact.get(i)[1] != 1572) {
//                GUI.recipe.getSingleIngredientID(ID).addMeasure((int) convFact.get(i)[1], (Double) convFact.get(i)[2]);
//            }
//        }
        //loop through the ingredients
        for (int i = 0; i < selected.getMeasures().size(); i++) {
            //get the id of the measure
            int measureID = selected.getSingleMeasureIndex(i).getID();
            //delete if it's a no measure ingredient
            if (measureID == 1572) {
                selected.removeSingleMeasure(i);
                //move back the counter becuase an inde was removed
                i--;
            } else {
                //otherwise get the name
                //System.out.println("ID" + measureID);
                selected.getSingleMeasureIndex(i).setName(Database.msName.get(Database.binarySearch(Database.msName, measureID, 0, Database.msName.size() - 1))[1].toString());
            }
        }
//        counter = 0;
//        for (int i = 0; i < msName.size(); i++) {
//            if (GUI.recipe.getSingleIngredientID(ID).getMeasures().size() == counter) {
//                break;
//            }
//            //get the measure name
//            //temp = Double.parseDouble(conversionRates.get(counter)[0].toString());
//            int measureID = GUI.recipe.getSingleIngredientID(ID).getSingleMeasureIndex(counter).getID();
//            if (measureID == (Integer) msName.get(i)[0] && measureID != 1572) {
//                // conversionRates.get(counter)[2] = aobj[1];//Add measure Name
//                GUI.recipe.getSingleIngredientID(ID).getSingleMeasureID((Integer) msName.get(i)[0]).setName(msName.get(i)[1].toString());
//                counter++;
//                //dont allow no measure specified
//            } else if (measureID == 1572) {
//                //conversionRates.remove(counter);
//                GUI.recipe.getSingleIngredientID(ID).removeSingleMeasure(counter);
//            }
//        }
    }//end getConv

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_measurement, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        EditText quantityBox = (EditText) findViewById(R.id.ingredient_amount);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            int temp;
            String ingredient = "";
            int quantity = Integer.parseInt(quantityBox.getText().toString());
            if (measureType.getSelectedItem().toString() == "Metric Cooking Measures") {
                //add the correct fraction to the begining of the ingredient name
                if (quantity > 1) {
                    temp = Integer.parseInt(measureSize.getSelectedItem().toString().substring(0, 1)) * quantity;
                    if (measureSize.getSelectedItemPosition() != 2 && measureSize.getSelectedItemPosition() != 3 && measureSize.getSelectedItemPosition() != 7) {//not full measures eg 1 cup
                        if (temp / Integer.parseInt(measureSize.getSelectedItem().toString().substring(2, 3)) >= 1) {
                            if (temp % Double.parseDouble(measureSize.getSelectedItem().toString().substring(2, 3)) == 0) {
                                ingredient = temp / Integer.parseInt(measureSize.getSelectedItem().toString().substring(2, 3)) + measureSize.getSelectedItem().toString().substring(3);
                                if (temp / Integer.parseInt(measureSize.getSelectedItem().toString().substring(2, 3)) > 1) {
                                    ingredient += "s";
                                }
                                ingredient += " " + selected.getName();
                            } else {
                                ingredient += (int) Math.floor(temp / Double.parseDouble(measureSize.getSelectedItem().toString().substring(2, 3)));
                                temp -= Double.parseDouble(measureSize.getSelectedItem().toString().substring(2, 3));
                                double temp2 = (double) temp / Double.parseDouble(measureSize.getSelectedItem().toString().substring(2, 3));
                                temp2 -= Math.floor(temp2);//leftovers
                                if (temp2 == 0.25) {
                                    ingredient += " 1/4";
                                } else if (temp2 == 0.5) {
                                    ingredient += " 1/2";
                                } else if (temp2 == 0.75) {
                                    ingredient += " 3/4";
                                } else if ((temp2 + "").substring(2, 3).equals("3")) {
                                    ingredient += " 1/3";
                                } else {
                                    ingredient += " 2/3";
                                }
                                ingredient += measureSize.getSelectedItem().toString().substring(3) + "s" + " " + selected.getName();
                            }
                        } else {
                            ingredient = temp + measureSize.getSelectedItem().toString().substring(1) + "s" + " " + selected.getName();
                        }
                    } else {
                        ingredient = temp + measureSize.getSelectedItem().toString().substring(1) + "s" + " " + selected.getName();
                    }
                } else {
                    ingredient = measureSize.getSelectedItem() + " " + selected.getName();
                }
            } else if (measureType.getSelectedItem().toString() == "g") {
                if (quantity < 1000) {
                    ingredient = quantity + "g " + selected.getName();
                } else {
                    ingredient = ((double) quantity / 1000.0) + "Kg " + selected.getName();
                }

            } else if (measureType.getSelectedItem().toString() == "Other") {
                String str = "";
                int i;
                for (i = 0; i < measureSize.getSelectedItem().toString().length(); i++) {//get the numbers from the front of the other name
                    if (measureSize.getSelectedItem().toString().charAt(i) >= 48
                            && measureSize.getSelectedItem().toString().charAt(i) <= 57) {
                        str += measureSize.getSelectedItem().toString().charAt(i);
                    } else {
                        break;
                    }
                }
                int num = Integer.parseInt(str);
                int num2 = num * quantity;
                ingredient = num2 + measureSize.getSelectedItem().toString().substring(i) + " " + selected.getName();
            } else if (quantity < 1000) {
                ingredient = quantity + "mL " + selected.getName();
            } else {
                ingredient = ((double)quantity / 1000.0) + "L " + selected.getName();
            }
            selected.setUnit(measureType.getSelectedItem().toString());
            selected.setUnitNum(measureType.getSelectedItemPosition());
            selected.setFractionNum(measureSize.getSelectedItemPosition());
            selected.setFractionName(measureSize.getSelectedItem().toString());

            System.out.println(quantity);
            selected.setQuantity(quantity);
            if (RecyclerViewHolders.edit) {
                RecipeCreateActivity.recipe.setSingleIngredient(RecyclerViewHolders.location, selected);
            } else {
                RecipeCreateActivity.recipe.addIngredient(selected);
            }
            // TODO: 1/15/2016 update the cards
//            RecipeCreateActivity.updateAdapter();
            this.finish();
        } else if (id == R.id.action_cancel) {
            this.finish();
            //insert code for what happens after add is pressed
        } else if (id == R.id.action_cancel) {
            //insert code for what happens after cancel is pressed
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSizeSpinner(int id) {
        //change what is showing depending on what unit is selected
        if (types[(int) id].equals("Metric Cooking Measures")) {
            items = new String[]{"1/4 Teaspoon", "1/2 Teaspoon", "1 Teaspoon", "1 Tablespoon", "1/4 Cup", "1/3 Cup", "1/2 Cup", "1 Cup"};
            ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_dropdown_item, items);
            measureSize.setPrompt("Please select a measure");
            measureSize.setAdapter(sizeAdapter);
            measureSize.setEnabled(true);
        } else if (types[id].equals("Other")) {
            // FIXME: 1/13/2016 this should only be a temp fix.
            items = new String[selected.getMeasures().size() / 2];
            for (int i = 0; i < items.length; i++) {
                items[i] = selected.getSingleMeasureIndex(i).getName();
            }
            ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_dropdown_item, items);
            measureSize.setPrompt("Please select a measure");
            measureSize.setAdapter(sizeAdapter);
            measureSize.setEnabled(true);
        } else {
            measureSize.setEnabled(false);
        }
    }
}