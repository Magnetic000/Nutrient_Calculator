package ics4u.ics4u_final_project;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class MeasureSelectionActivity extends AppCompatActivity {
    Ingredient selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measure_selector);
        selected = IngredientSelectionActivity.getResults().get(RecyclerViewHolders.location);
        System.out.println(selected);
        System.out.println(selected.getName());
        System.out.println(selected.getID());
        AdapterView.OnItemSelectedListener onSpinnerType = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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


        Spinner measureType = (Spinner) findViewById(R.id.measurement_type);
        String[] types = {"Metric Cooking Measures", "mL", "g", "Other"};
        measureType.setPrompt("Please select a measure type");
        ArrayAdapter<String> typesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, types);
        measureType.setAdapter(typesAdapter);
        measureType.setOnItemSelectedListener(onSpinnerType);

        Spinner measureSize = (Spinner) findViewById(R.id.measurement_amount);
        String[] items = new String[]{"1/4 Teaspoon", "1/2 Teaspoon", "1 Teaspoon", "1 Tablespoon", "1/4 Cup", "1/3 Cup", "1/2 Cup", "1 Cup"};
        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        measureSize.setPrompt("Please select a measure");
        measureSize.setAdapter(sizeAdapter);
        measureSize.setOnItemSelectedListener(onSpinnerSize);

    }


}