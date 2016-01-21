package ics4u.ics4u_final_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Isaac on 1/21/2016.
 */
public class ImportData extends AsyncTask<String, Integer, Boolean> {
    static ArrayList<Object[]> fdName = new ArrayList<>(), msName = new ArrayList<>(), ntName = new ArrayList<>(), convFact = new ArrayList<>(), ntAmt = new ArrayList<>();
    static final String delimiter = "#";
    Context c;
    static ProgressBar p;
    static double perProgress = 0.0;

    public ImportData(Context c, ProgressBar p) {
        this.c = c;
        this.p = p;
        p.setMax(100);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        System.out.println("food Name");
        //get the file's content as an arraylist
        ArrayList<String> file = readFile(c.getResources().openRawResource(R.raw.food_nm));
        //count the number of fields
        int fields = getFields(file.get(0));
        //create arrays for each line
        String[] line = new String[fields];
        Object[] temp = new Object[fields];
        //get the number of lines
        int fileSize = file.size();
        for (int i = 0; i < fileSize; i++) {
            String s = file.get(i);
            for (int j = 0; j < fields - 1; j++) {//go through each of the fields in the line
                line[j] = s.substring(0, s.indexOf(delimiter));//add the field
                s = s.substring(s.indexOf(delimiter) + 1);//remove it from the string
            }
            //add the last field to the array
            line[fields - 1] = s;
            //add the strings to the object array
            temp[0] = Integer.parseInt(line[0]);
            temp[1] = line[1];
            //add the to the arraylist of the file
            fdName.add(temp.clone());
        }
        //repeat for each data file
        System.out.println("Conv Fac");
        file = readFile(c.getResources().openRawResource(R.raw.conv_fac));
        fields = getFields(file.get(0));
        line = new String[fields];
        temp = new Object[fields];
        for (int i = 0; i < file.size(); i++) {
            String s = file.get(i);
            for (int j = 0; j < fields - 1; j++) {//go through each of the fields in the line
                line[j] = s.substring(0, s.indexOf(delimiter));//add the field
                s = s.substring(s.indexOf(delimiter) + 1);//remove it from the string
            }
            line[fields - 1] = s;
            if (line[2].equals("")) {
                continue;
            }
            temp[0] = Integer.parseInt(line[0]);
            temp[1] = Integer.parseInt(line[1]);
            temp[2] = Double.parseDouble(line[2]);
            convFact.add(temp.clone());
        }
        System.out.println("Nt amount");
        file = readFile(c.getResources().openRawResource(R.raw.nt_amt));
        fields = getFields(file.get(0));
        line = new String[fields];
        temp = new Object[fields];
        for (int i = 0; i < file.size(); i++) {
            String s = file.get(i);
            for (int j = 0; j < fields - 1; j++) {//go through each of the fields in the line
                line[j] = s.substring(0, s.indexOf(delimiter));//add the field
                s = s.substring(s.indexOf(delimiter) + 1);//remove it from the string
            }
            line[fields - 1] = s;
            if (line[2].equals("")) {
                continue;
            }
            temp[0] = Integer.parseInt(line[0]);
            temp[1] = Integer.parseInt(line[1]);
            temp[2] = Double.parseDouble(line[2]);
            ntAmt.add(temp.clone());
        }
        System.out.println("measures");
        file = readFile(c.getResources().openRawResource(R.raw.measure));
        fields = getFields(file.get(0));
        line = new String[fields];
        temp = new Object[fields];
        for (int i = 0; i < file.size(); i++) {
            String s = file.get(i);
            for (int j = 0; j < fields - 1; j++) {//go through each of the fields in the line
                line[j] = s.substring(0, s.indexOf(delimiter));//add the field
                s = s.substring(s.indexOf(delimiter) + 1);//remove it from the string
            }
            line[fields - 1] = s;
            temp[0] = Integer.parseInt(line[0]);
            temp[1] = line[1];
            msName.add(temp.clone());
        }
        System.out.println("Nt name");
        file = readFile(c.getResources().openRawResource(R.raw.nt_nm));
        fields = getFields(file.get(0));
        line = new String[fields];
        temp = new Object[fields];
        for (int i = 0; i < file.size(); i++) {
            String s = file.get(i);
            for (int j = 0; j < fields - 1; j++) {//go through each of the fields in the line
                line[j] = s.substring(0, s.indexOf(delimiter));//add the field
                s = s.substring(s.indexOf(delimiter) + 1);//remove it from the string
            }
            line[fields - 1] = s;
            temp[0] = Integer.parseInt(line[0]);
            temp[1] = line[1];
            temp[2] = line[2];
            ntName.add(temp.clone());
        }
        return true;
    }

    protected void onProgressUpdate() {
        //called when the background task makes any progress
    }

    protected void onPreExecute() {
        //called before doInBackground() is started
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //called after doInBackground() is finished
        Database.fdName = this.fdName;
        Database.ntAmt = this.ntAmt;
        Database.ntName = this.ntName;
        Database.msName = this.msName;
        Database.convFact = this.convFact;
        endSplash();
    }
    public static void endSplash(){
        Splash.fa.finish();
        Intent intent = new Intent(Splash.c, MainActivity.class);
        Splash.fa.startActivity(intent);
    }
    /**
     * Reads a data file to an arraylist
     *
     * @param filePath the path of the file to be read
     * @return the file as an ArrayList<String>
     */
    public static ArrayList<String> readFile(InputStream filePath) {
        //create a buffered reader
        BufferedReader br = new BufferedReader(new InputStreamReader(filePath));
        //create an arraylist for the file
        ArrayList<String> file = new ArrayList<>();
        try {
            //read each line of the file and add it to the arraylist
            String s = br.readLine();
            while (s != null) {
                file.add(s);
                s = br.readLine();
                //perProgress += 1.0/525268.0;
                //setProgress((int) perProgress);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finish read");

        return file;
    }

    /**
     * returns the number of fields for the given string
     *
     * @param s the string to test
     * @return the number of fields
     */
    public static int getFields(String s) {
        int fields = 1;
        //minimum of one field
        //count the number of additional fields
        for (int i = 0; i < s.length(); i++) {
            if (s.substring(i, i + 1).equals(delimiter)) {
                fields++;
            }
        }
        return fields;
    }

    public static void setProgress(int progress){
        p.setProgress(progress);
    }
}
