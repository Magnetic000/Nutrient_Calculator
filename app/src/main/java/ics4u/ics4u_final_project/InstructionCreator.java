/*
This class is that activity that allows the user to add ingredients to their recipe
 */
/*
Copyright (C) 2016 Isaac Wismer & Andrew Xu

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package ics4u.ics4u_final_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    public void onBackPressed() {
        super.onBackPressed();
        //save the ingredients
        EditText editor = (EditText) findViewById(R.id.instructionsText);
        RecipeCreateActivity.recipe.setInstructions(editor.getText().toString());
    }
}
