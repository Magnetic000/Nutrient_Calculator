/*
This class is for the about screen
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
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class About extends AppCompatActivity {
    /**
     * Method that runs when the activity is first load
     * Will run every time activity is loaded from scratch
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Link .java file with the about xml file to load visuals
        setContentView(R.layout.about_activity);
        //Open stream to about file
        BufferedReader br = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.about)));
        String file = "";
        try {
            //read each line of the file and add it to the arraylist
            String s = br.readLine();
            while (s != null) {
                file += s + "\n";
                s = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Find the reference location of the text view
        TextView about = (TextView) findViewById(R.id.about);
        //Set text
        about.setText(Html.fromHtml(file));
        //Set the title of the toolbar
        setTitle("About This App");
        //Find reference location of the toolbar from xml
        Toolbar topToolBar = (Toolbar) findViewById(R.id.toolbar);
        //Set toolbar with updated info
        setSupportActionBar(topToolBar);
    }
}
