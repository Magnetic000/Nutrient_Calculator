package ics4u.ics4u_final_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.ToggleButton;

/**
 * Created by Andrew on 2016-01-19.
 */
public class UnitConverter extends AppCompatActivity {
    public double rate;
    private double[] imperialRates = {0.0283495,0.9464,0.550610475000932, 3.78541,0.453592,212};
    private double[] metricRates = {1000, 1, 1, 1000, 100};
    private String[] imperial = {"Ounce","Quart","Pint","Gallon","Pounds","Fahrenheit"};
    private String[] metric = {"Millilitre","Litre","Kilogram","Gram","Celsius"};
    Spinner inputType, outputType;
    Switch check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.converter_activity);
        setTitle("Unit Conversion");

        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

        check = (Switch)findViewById(R.id.switch_button);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if(check.getText().equals("Imperial > Metric")){
                    check.setText("Metric > Imperial");
                } else {
                    check.setText("Imperial > Metric");
                }
            }
        });

        inputType = (Spinner) findViewById(R.id.input_type);
        outputType = (Spinner) findViewById(R.id.output_type);

        AdapterView.OnItemSelectedListener onSpinnerInput = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //setSizeSpinner((int) id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        AdapterView.OnItemSelectedListener onSpinnerOutput = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        inputType.setPrompt("Please select an imperial measurement");
        ArrayAdapter<String> inputAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, imperial);
        inputType.setAdapter(inputAdapter);
        inputType.setOnItemSelectedListener(onSpinnerInput);

        ArrayAdapter<String> outputAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, metric);
        outputType.setPrompt("Please select a metric measurement");
        outputType.setAdapter(outputAdapter);
        outputType.setOnItemSelectedListener(onSpinnerOutput);
    }
}
