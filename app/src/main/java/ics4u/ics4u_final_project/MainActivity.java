package ics4u.ics4u_final_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView personName;
    TextView personAge;
    ImageView personPhoto;
    TextView personName1;
    TextView personAge1;
    ImageView personPhoto1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cardview_activity);
        personName = (TextView)findViewById(R.id.person_name);
        personAge = (TextView)findViewById(R.id.person_age);
        personPhoto = (ImageView)findViewById(R.id.person_photo);

        personName.setText("Emma Wilson");
        personAge.setText("23 years old");
    }


    public void loadRecipe(View view){
        Intent intent = new Intent(this, RecipeCreator.class);
        startActivity(intent);
    }

    public void buttonCode(){
        /*final Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(
            new Button.OnClickListener(){
                public void onClick (View v){
                    button.setText("Clicked");
                }
            }
        );

        button.setOnLongClickListener(
                new Button.OnLongClickListener() {
                    public boolean onLongClick(View v) {
                        button.setText("long");
                        return false;
                    }
                }
        );*/


    }

}
