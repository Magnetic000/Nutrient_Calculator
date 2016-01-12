package ics4u.ics4u_final_project;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


public class MeasureSelectionActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        setContentView(R.layout.measure_selector);
        Spinner dropdown = (Spinner) findViewById(R.id.measurement_amount);
        String[] items = new String[]{"", "1", "2", "three"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setPrompt("Please select a value");
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(onSpinner);
        items[0] = "Changed";
        adapter.notifyDataSetChanged();//tells adapter that data set has changed and updates


    }


}