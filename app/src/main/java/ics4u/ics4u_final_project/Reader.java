/* Isaac Wismer
 * 
 */
package ics4u.ics4u_final_project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author isaac
 */
public class Reader {


    private final InputStream filePath;
    private int fields = 1;
    private BufferedReader br;
    private InputStreamReader isr;
    private Scanner sc;
    //the delimiter used in the files ("#" doesn't appear but "'", ";", ":" and "." all appear
    private final String delimiter = "#";

    /**
     * @param
     */
    public Reader(InputStream input) {
        filePath = input;
        //isr = new InputStreamReader(input);
        //br = new BufferedReader(new InputStreamReader(filePath));
        //countFields();//init the reader
    }

    public String[][] readFile(InputStream filePath){
        sc = new Scanner(filePath);
        ArrayList<String> a = new ArrayList<>();
        while(sc.hasNextLine()){
            a.add(sc.nextLine());
        }
        String s = a.get(0);
        fields = 1;
        //minimum of one field
        //count the number of additional fields
        for (int i = 0; i < s.length(); i++) {
            if (s.substring(i, i + 1).equals(delimiter)) {
                fields++;
            }
        }
        String[][] file = new String[a.size()][fields];
        for(int i = 0; i < a.size(); i++){
            s = a.get(i);
            for (int j = 0; j < fields - 1; j++) {//go through each of the fields in the line
                file[i][j] = s.substring(0, s.indexOf(delimiter));//add the field
                s = s.substring(s.indexOf(delimiter) + 1);//remove it from the string
            }
            file[i][fields - 1] = s;
        }
        return file;
    }

    /**
     * @return an object with each index containing one field
     */
    public String[] getNextLine() {
        String s = "";
        //read the next line
        //System.out.println("READ");
        try {
            s = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(s);
        //create an array with as many indexes as there are fields
        String[] line = new String[fields];
        //put the information into the fields
        for (int i = 0; i < fields - 1; i++) {//go through each of the fields in the line

            line[i] = s.substring(0, s.indexOf(delimiter));//add the field
            s = s.substring(s.indexOf(delimiter) + 1);//remove it from the string
        }
        line[fields - 1] = s;
        return line;
    }

    /**
     * @return the number of lines in the file
     */
    public int getLength() {
        System.out.println("Get Length");
        //put the buffered reader back to the top of the file
        try {
            System.out.println(filePath);
            InputStreamReader is = new InputStreamReader(filePath);
            br = new BufferedReader(is);

            int c = 0;
            //count the number of lines in the file
            //System.out.println();
            String l = br.readLine();
            while (l != null) {
                //System.out.println(l);
                c++;
                l = br.readLine();
            }
            System.out.println(c);
            //br.close();
            br = new BufferedReader(new InputStreamReader(filePath));
            return c;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private void countFields() {
        //initialize the reader
        System.out.println("Path: " + filePath);
        try {
            br = new BufferedReader(new InputStreamReader(filePath));
            //count the number of fields
            //read the next line
            String s = br.readLine();
            System.out.println("Line: " + s);
            fields = 1;
            //minimum of one field
            //count the number of additional fields
            for (int i = 0; i < s.length(); i++) {
                if (s.substring(i, i + 1).equals(delimiter)) {
                    fields++;
                }
            }
            //reinitialize the reader
            //br.close();
            br = new BufferedReader(new InputStreamReader(filePath));
            System.out.println(fields);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
