package ics4u.ics4u_final_project;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MeasureSelectionActivity extends AppCompatActivity {
    Ingredient selected;
    boolean edit;
    String[] types, items;
    Context c = this;

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
        final Spinner measureSize = (Spinner) findViewById(R.id.measurement_amount);
        Spinner measureType = (Spinner) findViewById(R.id.measurement_type);
        AdapterView.OnItemSelectedListener onSpinnerType = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //change what is showing depending on what unit is selected
                if (types[(int) id].equals("Metric Cooking Measures")) {
                    items = new String[]{"1/4 Teaspoon", "1/2 Teaspoon", "1 Teaspoon", "1 Tablespoon", "1/4 Cup", "1/3 Cup", "1/2 Cup", "1 Cup"};
                    ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_dropdown_item, items);
                    measureSize.setPrompt("Please select a measure");
                    measureSize.setAdapter(sizeAdapter);
                    measureSize.setEnabled(true);
                } else if (types[(int) id].equals("Other")) {
                    items = new String[selected.getMeasures().size()];
                    for (int i = 0; i < selected.getMeasures().size(); i++) {
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

}