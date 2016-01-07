/* Isaac Wismer
 * 
 */
package ics4u.ics4u_final_project;

import java.io.InputStream;
import java.util.*;

/**
 * @author isaac
 */
public class Reader {

    //private BufferedReader br;
    //private InputStreamReader isr;
    // private InputStream in;
    private Scanner sc;
    private final InputStream filePath;
    private int fields = 1;
    //the delimiter used in the files ("#" doesn't appear but "'", ";", ":" and "." all appear
    private final String delimiter = "#";

    /**
     * @param
     */
    public Reader(InputStream input) {
        filePath = input;
        reset();//init the reader
    }

    /**
     * @return an object with each index containing one field
     */
    public String[] getNextLine() {
        String s;
        //read the next line
        s = sc.nextLine();
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
        reset();
        int c = 0;
        //count the number of lines in the file
        while (sc.hasNextLine()) {
            c++;
            sc.nextLine();
        }
        System.out.println(c);
        reset();
        return c;
    }

    private void reset() {
        //initialize the reader
        System.out.println("Path: " + filePath);
        sc = new Scanner(filePath);
        //count the number of fields
        //read the next line
        String s = sc.nextLine();
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
        sc = new Scanner(filePath);
    }

}
