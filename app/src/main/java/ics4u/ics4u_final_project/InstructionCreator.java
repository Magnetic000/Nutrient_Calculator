package ics4u.ics4u_final_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class InstructionCreator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instruction_creator);
        EditText editor = (EditText) findViewById(R.id.instructionsText);
        editor.setText(RecipeCreateActivity.recipe.getInstructions());
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //save the ingredients
        EditText editor = (EditText) findViewById(R.id.instructionsText);
        RecipeCreateActivity.recipe.setInstructions(editor.getText().toString());
    }
}
