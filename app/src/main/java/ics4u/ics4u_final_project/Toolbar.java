package ics4u.ics4u_final_project;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Toolbar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        android.support.v7.widget.Toolbar myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }
}
