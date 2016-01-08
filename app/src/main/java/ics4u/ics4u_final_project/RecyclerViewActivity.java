package ics4u.ics4u_final_project;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends Activity {

    public static List<Recipe> persons;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.recyclerview_activity);

        rv = (RecyclerView) findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData() {
        persons = new ArrayList<>();
        persons.add(new Recipe("Emma Wilson", "23 years old", R.drawable.bread));
        persons.add(new Recipe("Lavery Maiss", "25 years old", R.drawable.fish));
        persons.add(new Recipe("Lillie Watts", "35 years old", R.drawable.banana));
    }

    private void initializeAdapter() {
        MyAdapter adapter = new MyAdapter(persons);
        rv.setAdapter(adapter);
    }
}
