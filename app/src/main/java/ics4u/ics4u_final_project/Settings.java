package ics4u.ics4u_final_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

/**
 * Created by Andrew on 2016-01-18.
 */
public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        setTitle("Settings");

        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);
    }
}
