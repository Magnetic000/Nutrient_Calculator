/* Isaac Wismer
 *  Jun 15, 2015
 */
package ics4u.ics4u_final_project;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * @author isaac
 */
public class Database {

    static final DecimalFormat oneDecimal = new DecimalFormat("#,##0.0");
    static ArrayList<Object[]> fdName = new ArrayList<>(), msName = new ArrayList<>(), ntName = new ArrayList<>(), convFact = new ArrayList<>(), ntAmt = new ArrayList<>();
    static Recipe recipe = new Recipe();
    static final String delimiter = "#";

    /**
     * Imports the data stored in files to arraylists to faster searching
     */
    public static void importData(Context c) {
        System.out.println("food Name");
        ArrayList<String> file = readFile(c.getResources().openRawResource(R.raw.food_nm));
        int fields = getFields(file.get(0));
        String[] line = new String[fields];
        Object[] temp = new Object[fields];
        for (int i = 0; i < file.size(); i++) {
            String s = file.get(i);
            for (int j = 0; j < fields - 1; j++) {//go through each of the fields in the line
                line[j] = s.substring(0, s.indexOf(delimiter));//add the field
                s = s.substring(s.indexOf(delimiter) + 1);//remove it from the string
            }
            line[fields - 1] = s;
            temp[0] = Integer.parseInt(line[0]);
            temp[1] = line[1];
            fdName.add(temp);
        }
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
            convFact.add(temp);
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
            ntAmt.add(temp);
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
            msName.add(temp);
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
            ntName.add(temp);
        }
    }

    public static ArrayList<String> readFile(InputStream filePath) {
        BufferedReader br = new BufferedReader(new InputStreamReader(filePath));
        ArrayList<String> a = new ArrayList<>();
        try {
            String s = br.readLine();
            while (s != null) {
                a.add(s);
                s = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("finish read");
        return a;
    }

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

    /**
     * Searches the list of ingredients for the keyword
     *
     * @param keyword the word(s) to search with
     * @return a list of the matches
     */
    public static ArrayList<Ingredient> search(String keyword) {
        double temp;
        //create new arraylist for the matched
        ArrayList<Ingredient> match = new ArrayList<>(0);
        int counter = 0, index;
        //create new arraylist for the search queryies
        ArrayList<String> query = new ArrayList<>(0);
        //separate the search string into queryies
        while (keyword.contains(",")) {
            index = keyword.indexOf(",");
            query.add(keyword.substring(0, index));
            keyword = keyword.substring(index + 1);
            if (keyword.substring(0, 1).equals(" ")) {
                keyword = keyword.substring(1);
            }
        }
        //add the remaining part of the string to the queries
        query.add(keyword);
        //read from the nutrients to check if they match the queries
        //read from the file
        //read each line and check if it matched the search terms
        for (int i = 0; i < fdName.size(); i++) {
            boolean matches = true;
            //check if the food item matches the query
            for (int j = 0; j < query.size(); j++) {
                if (!(fdName.get(i)[1]).toString().contains(query.get(j))) {
                    matches = false;
                }
            }
            //if it does add it to the list of matches
            if (matches) {
                match.add(new Ingredient((Integer) fdName.get(i)[0], (String) fdName.get(i)[1]));
            }
        }
        return match;
    }//End searchByName()

    /**
     * This method saves the recipe to a file
     *
     * @param file The file path
     * @throws FileNotFoundException
     */
    public static void save(File file) throws FileNotFoundException {
        //PrintWriter p = new PrintWriter(file + ".txt");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("recipe");
            doc.appendChild(rootElement);

//             title element
            Element title = doc.createElement("title");
            title.appendChild(doc.createTextNode(recipe.getTitle()));
//            p.println(GUI.recipe.getTitle());
            rootElement.appendChild(title);
//             title element
            Element instructions = doc.createElement("instructions");
            instructions.appendChild(doc.createTextNode(recipe.getInstructions()));
//            p.println(GUI.recipe.getInstructions());
            rootElement.appendChild(instructions);

            Element servings = doc.createElement("servings");
            instructions.appendChild(doc.createTextNode(recipe.getServings() + ""));
//            p.println(GUI.recipe.getInstructions());
            rootElement.appendChild(servings);

            Element servingName = doc.createElement("servingName");
            instructions.appendChild(doc.createTextNode(recipe.getServingName()));
//            p.println(GUI.recipe.getInstructions());
            rootElement.appendChild(servingName);

            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                Element ingredients = doc.createElement("ingredients");
                rootElement.appendChild(ingredients);
                Ingredient ing = recipe.getSingleIngredientIndex(i);

                Element id = doc.createElement("ID");
                id.appendChild(doc.createTextNode(ing.getID() + ""));
                ingredients.appendChild(id);
//                p.println(ing.getID());

                Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(ing.getName()));
                ingredients.appendChild(name);
//                p.println(ing.getName());

                Element formattedName = doc.createElement("formattedName");
                formattedName.appendChild(doc.createTextNode(ing.getFormattedName()));
                ingredients.appendChild(formattedName);
//                p.println(ing.getFormattedName());

                Element unitName = doc.createElement("unitName");
                unitName.appendChild(doc.createTextNode(ing.getUnit()));
                ingredients.appendChild(unitName);
//                p.println(ing.getUnit());

                Element unitNum = doc.createElement("unitNum");
                unitNum.appendChild(doc.createTextNode(ing.getUnitNum() + ""));
                ingredients.appendChild(unitNum);
//                p.println(ing.getUnitNum());

                Element fractionName = doc.createElement("fractionName");
                fractionName.appendChild(doc.createTextNode(ing.getFractionName()));
                ingredients.appendChild(fractionName);
//                p.println(ing.getFractionName());

                Element fractionNum = doc.createElement("fractionNum");
                fractionNum.appendChild(doc.createTextNode(ing.getFractionNum() + ""));
                ingredients.appendChild(fractionNum);
//                p.println(ing.getFractionNum());

                Element quantity = doc.createElement("quantity");
                quantity.appendChild(doc.createTextNode(ing.getQuantity() + ""));
                ingredients.appendChild(quantity);
//                p.println(ing.getQuantity());

                for (int j = 0; j < ing.getMeasures().size(); j++) {
                    Element measures = doc.createElement("measures");
                    ingredients.appendChild(measures);
                    Measures m = ing.getSingleMeasureIndex(j);

                    Element measureID = doc.createElement("id");
                    measureID.appendChild(doc.createTextNode(m.getID() + ""));
                    measures.appendChild(measureID);
//                    p.println(m.getID());

                    Element conversion = doc.createElement("conversion");
                    conversion.appendChild(doc.createTextNode(m.getConversion() + ""));
                    measures.appendChild(conversion);
//                    p.println(m.getConversion());

                    Element measureName = doc.createElement("measureName");
                    measureName.appendChild(doc.createTextNode(m.getName()));
                    measures.appendChild(measureName);
//                    p.println(m.getName());
                }
            }
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result;
            if (!file.toString().substring(file.toString().length() - 4, file.toString().length()).equals(".xml")) {
                result = new StreamResult(file + ".xml");
            } else {
                result = new StreamResult(file);
            }
            transformer.transform(source, result);
            System.out.println("File saved!");
//            p.close();
        } catch (ParserConfigurationException | TransformerException ex) {
            System.out.println("XML Error: " + ex.toString());
        }
    }//End save()

    /**
     * This method opens a saved recipe and re-assings the variable with the new
     * data
     *
     * @param file The file path
     * @return
     */
    public static Recipe open(File file) {
        System.out.println(file);
        Recipe opened = new Recipe();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            //title
            NodeList nList = doc.getElementsByTagName("title");
            Node nNode = nList.item(0);
            Element eElement = (Element) nNode;
            opened.setTitle(eElement.getTextContent());
            System.out.println(opened.getTitle());
            //instructions
            nList = doc.getElementsByTagName("instructions");
            nNode = nList.item(0);
            eElement = (Element) nNode;
            opened.setInstructions(eElement.getTextContent());
            //Servings
            nList = doc.getElementsByTagName("servings");
            nNode = nList.item(0);
            eElement = (Element) nNode;
            opened.setServings(Integer.parseInt(eElement.getTextContent()));
            //Serving Name
            nList = doc.getElementsByTagName("servingName");
            nNode = nList.item(0);
            eElement = (Element) nNode;
            opened.setServingName(eElement.getTextContent());
            //System.out.println(GUI.recipe.getInstructions());
            //ingredients
            nList = doc.getElementsByTagName("ingredients");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    eElement = (Element) nNode;
                    Ingredient i = new Ingredient();
                    i.setID(Integer.parseInt(eElement.getElementsByTagName("ID").item(0).getTextContent()));
                    i.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                    i.setFormattedName(eElement.getElementsByTagName("formattedName").item(0).getTextContent());
                    i.setUnit(eElement.getElementsByTagName("unitName").item(0).getTextContent());
                    i.setUnitNum(Integer.parseInt(eElement.getElementsByTagName("unitNum").item(0).getTextContent()));
                    i.setFractionName(eElement.getElementsByTagName("fractionName").item(0).getTextContent());
                    i.setFractionNum(Integer.parseInt(eElement.getElementsByTagName("fractionNum").item(0).getTextContent()));
                    i.setQuantity(Integer.parseInt(eElement.getElementsByTagName("quantity").item(0).getTextContent()));

                    //measures
                    NodeList measureList = eElement.getElementsByTagName("measures");
                    for (int j = 0; j < measureList.getLength(); j++) {
                        Node mNode = measureList.item(j);
                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element mElement = (Element) nNode;
                            Measures m = new Measures();
                            m.setID(Integer.parseInt(mElement.getElementsByTagName("id").item(j).getTextContent()));
                            m.setConversion(Double.parseDouble(mElement.getElementsByTagName("conversion").item(j).getTextContent()));
                            m.setName(mElement.getElementsByTagName("measureName").item(j).getTextContent());
                            i.addMeasureFull(m);
                        }
                    }
                    opened.addIngredient(i);
                }
            }
            for (int i = 0; i < opened.getIngredients().size(); i++) {
                opened.addListItem(opened.getSingleIngredientIndex(i).getFormattedName());
            }

        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            System.out.println("File Read Error: " + e.toString());
        }
        return opened;
    }//end open()

    /**
     * This method creates the output that will be displayed for the user
     *
     * @param recipe          The entire recipe with all ingredient and measure
     *                        information
     * @param recipeNutrients
     * @param label           whether or not to print the information as a label
     * @return a string with the formatted output
     */
    public static String createOutput(Recipe recipe, Double[] recipeNutrients, boolean label) {
        String output;
        //make the title
        output = "Nutrition Facts for " + recipe.getTitle() + "\r\n\r\n";
        output += "Ingredients:\r\n";
        //print out the ingredients
        for (int i = 0; i < recipe.getList().size(); i++) {
            output += recipe.getList().get(i) + "\r\n";
        }
        output += "\r\n\r\n";
        //print out the instructions
        if (!recipe.getInstructions().equals("")) {
            output += "Directions\n";
            output += recipe.getInstructions();
            output += "\r\n\r\n";
        }
        if (!label) {//print out the info if its not a label
            for (int i = 0; i < ntName.size(); i++) {
                int id = (int) ntName.get(i)[0];
                //makes sure there is data for the nutrient
                if (recipeNutrients[id] != null) {
                    //makes sure the value is not 0
                    if (!oneDecimal.format((recipeNutrients[id])).equals("0.0")) {
                        //add the nutrient name
                        output += ntName.get(i)[2];
                        //add the periods to line up the output
                        int dots = (80 - ntName.get(i)[2].toString().length());
                        for (int j = 0; j < dots; j++) {
                            output += ".";
                        }
                        //add the nutrient information
                        output += oneDecimal.format((recipeNutrients[id])) + ntName.get(i)[1] + "\r\n";
                    }
                }
            }
        } else {//if it is a label
            //make the percent decimal format
            DecimalFormat onePer = new DecimalFormat("#,##0%");
            //begin printing out the label
            output += "Makes " + recipe.getServings() + ". The label is made for that serving size.\r\n";
            output += "=========================================\r\n";
            output += "|Nutrition Facts\t\t\t|\r\n";
            output += "|Per " + recipe.getServingName();
            double servingsD = recipe.getServings();
            //the if staments checks the size of the number to determine the 
            //number of tabs that are necissary to line up the label
            if (recipe.getServingName().length() < 5) {
                output += "\t\t\t\t";
            } else if (recipe.getServingName().length() >= 30) {
            } else if (recipe.getServingName().length() >= 22) {
                output += "\t";
            } else if (recipe.getServingName().length() >= 14) {
                output += "\t\t";
            } else {
                output += "\t\t\t";
            }
            output += "|\r\n";
            output += "|=======================================|\r\n";
            output += "|Amount\t\t\t% Daily Value\t|\r\n";
            output += "|Teneur\t\t % valeur quotodoenne\t|\r\n";
            output += "|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|\r\n";
            output += "|Calories/Calories " + (int) (recipeNutrients[208] / servingsD) + "\t\t";
            if ((recipeNutrients[208] / servingsD) < 10000) {
                output += "\t";
            }
            output += "|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|Fat/Lipides " + (int) (recipeNutrients[204] / servingsD) + "g\t\t";
            if ((int) (recipeNutrients[204] / servingsD) <= 9) {
                output += "\t";
            }
            output += onePer.format((recipeNutrients[204] / servingsD) / 60.0) + "\t|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|\tSaturated/Satures " + (int) (recipeNutrients[606] / servingsD) + "g\t" + onePer.format((recipeNutrients[606] / servingsD) / 20.0) + "\t|\r\n";
            output += "|\t+ Trans/trans " + (int) (recipeNutrients[605] / servingsD) + "g\t\t|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|Cholesterol/Cholesterol " + (int) (recipeNutrients[601] / servingsD) + "mg\t";
            if (recipeNutrients[601] / servingsD < 1000) {
                output += "\t";
            }
            output += "|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|Sodium/Sodium " + (int) (recipeNutrients[307] / servingsD) + "mg\t\t" + onePer.format((recipeNutrients[307] / servingsD) / 2400.0) + "\t|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|Carbohydrate/Glucides " + (int) (recipeNutrients[205] / servingsD) + "g\t" + onePer.format((recipeNutrients[205] / servingsD) / 300.0) + "\t|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|\tFibre/Fibres " + (int) (recipeNutrients[291] / servingsD) + "g\t";
            if ((recipeNutrients[291] / servingsD) < 10) {
                output += "\t";
            }
            output += onePer.format((recipeNutrients[291] / servingsD) / 25.0) + "\t|\r\n";
            output += "|\t--------------------------------|\r\n";
            output += "|\tSugars/Sucres " + (int) (recipeNutrients[269] / servingsD) + "g\t\t|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|Protein/Proteines " + (int) (recipeNutrients[203] / servingsD) + "g\t\t";
            if (recipeNutrients[203] / servingsD < 1000) {
                output += "\t";
            }
            output += "|\r\n";
            output += "|~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~|\r\n";
            output += "|Vitamin A/Vitamine A\t\t**%\t|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|Vitamin C/Vitamine C\t\t" + onePer.format((recipeNutrients[401] / servingsD) / 60.0) + "\t|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|Calcium/Calcium\t\t" + onePer.format((recipeNutrients[301] / servingsD) / 1100.0) + "\t|\r\n";
            output += "|---------------------------------------|\r\n";
            output += "|Iron/fer\t\t\t" + onePer.format((recipeNutrients[303] / servingsD) / 14.0) + "\t|\r\n";
            output += "=========================================\r\n";
        }
        return output;
    }//End createOutput()

    /**
     * @param ID the ID of the measure
     * @return whether or not it can be measured in mL
     */
    public static boolean checkMeasuresML(int ID) {
        //get conversion rates
        getConv(ID);
        //check the conversions for mL measurements
        for (int i = 0; i < recipe.getSingleIngredientID(ID).getMeasures().size(); i++) {
            int measureID = recipe.getSingleIngredientID(ID).getSingleMeasureIndex(i).getID();
            if (measureID > 340 && measureID < 380) {
                return true;
            } else if (measureID > 384 && measureID < 394) {
                return true;
            } else if (measureID > 412 && measureID < 427) {
                return true;
            } else if (measureID == 439 || measureID == 388
                    || measureID == 389 || measureID == 923
                    || measureID == 932 || measureID == 638
                    || measureID == 640 || measureID == 641
                    || measureID == 430 || measureID == 939
                    || measureID == 943 || measureID == 429
                    || measureID == 428 || measureID == 383) {
                return true;
            } else if (measureID >= 385 && measureID <= 387) {
                return true;
            }
        }
        return false;
    }//End checkMeasurementML()

    /**
     * This method may cause errors with the new arraylists. Check this if there
     * is an issue
     *
     * @param ID the ID of the measure
     */
    public static void getConv(int ID) {
        System.out.println("Getting conversion rates for food ID = " + ID);
        //search for the nutrient ID in the file
        int begin = binarySearch(convFact, ID, 0, convFact.size() - 1);
        if (begin == -1) {
            System.out.println("Not Found");
        }
        //step back to the beginning of the nutrient
        while (begin > 0 && (int) convFact.get(begin - 1)[0] == ID) {
            begin--;
        }
        //read until the end of the nutrient
        for (int i = begin; (int) convFact.get(i)[0] == ID; i++) {
            if ((int) convFact.get(i)[1] != 1572) {
                recipe.getSingleIngredientID(ID).addMeasure((int) convFact.get(i)[1], (Double) convFact.get(i)[2]);
            }
        }
//        for (int i = 0; i < convFact.size(); i++) {
//            //get the measure conversion factor and measure ID
//            if (ID == (int) convFact.get(i)[0] && (int) convFact.get(i)[1] != 1572) {
//                GUI.recipe.getSingleIngredientID(ID).addMeasure((int) convFact.get(i)[1], (Double) convFact.get(i)[2]);
//            }
//        }
        //loop through the ingredients
        for (int i = 0; i < recipe.getSingleIngredientID(ID).getMeasures().size(); i++) {
            //get the id of the measure
            int measureID = recipe.getSingleIngredientID(ID).getSingleMeasureIndex(i).getID();
            //delete if it's a no measure ingredient
            if (measureID == 1572) {
                recipe.getSingleIngredientID(ID).removeSingleMeasure(i);
                //move back the counter becuase an inde was removed
                i--;
            } else {
                //otherwise get the name
                //System.out.println("ID" + measureID);
                recipe.getSingleIngredientID(ID).getSingleMeasureIndex(i).setName(msName.get(binarySearch(msName, measureID, 0, msName.size() - 1))[1].toString());
            }
        }
//        counter = 0;
//        for (int i = 0; i < msName.size(); i++) {
//            if (GUI.recipe.getSingleIngredientID(ID).getMeasures().size() == counter) {
//                break;
//            }
//            //get the measure name
//            //temp = Double.parseDouble(conversionRates.get(counter)[0].toString());
//            int measureID = GUI.recipe.getSingleIngredientID(ID).getSingleMeasureIndex(counter).getID();
//            if (measureID == (Integer) msName.get(i)[0] && measureID != 1572) {
//                // conversionRates.get(counter)[2] = aobj[1];//Add measure Name
//                GUI.recipe.getSingleIngredientID(ID).getSingleMeasureID((Integer) msName.get(i)[0]).setName(msName.get(i)[1].toString());
//                counter++;
//                //dont allow no measure specified
//            } else if (measureID == 1572) {
//                //conversionRates.remove(counter);
//                GUI.recipe.getSingleIngredientID(ID).removeSingleMeasure(counter);
//            }
//        }
    }//end getConv

    /**
     * Not quite sure what this method does.... I'm not sure that it's ever
     * called....
     *
     * @param ID the ID of the measure
     * @return
     */
    public static ArrayList<String> measures(int ID) {
        //get the conversion rates for the ingredient
        //getConv(ID);
        //create the combobox model
        ArrayList<String> measures = new ArrayList<>();
        //add the names of the potions to the cmb model
        for (int i = 0; i < recipe.getSingleIngredientID(ID).getMeasures().size(); i++) {
            measures.add(recipe.getSingleIngredientID(ID).getSingleMeasureIndex(i).getName());
        }
        return measures;
    }//End convRate()

    /**
     * This method gets the information for each ingredient needed to create the
     * label/output
     *
     * @param recipe The complete recipe with all the ingredient and measure
     *               info
     * @param label  a boolean stating whether or not to print as a label
     * @param pdf    Whether or not the output is being used to print a pdf
     * @return a string with the output
     */
    public static String recipe(Recipe recipe, boolean label, boolean pdf) {
        Double[] recipeNutrients = new Double[870], indiviual;
        double nutrientConv = 1.0, portionSize = 100.0;
        for (int i = 0; i < recipeNutrients.length; i++) {
            recipeNutrients[i] = 0.0;
        }
        for (int i = 0; i < recipe.size(); i++) {
            //get the nutrient data for the food
            indiviual = getNutrientData(recipe.getSingleIngredientIndex(i).getID());
            System.out.println("Ingredient: " + recipe.getSingleIngredientIndex(i).getName());
            System.out.println("Measurement: " + recipe.getSingleIngredientIndex(i).getFractionName());
            //convert the nutrients to be for the specified quantity
            if (recipe.getSingleIngredientIndex(i).getUnit().equals("g")) {
                //grams just have to be divided by 100 to get the conversion rate
                nutrientConv = ((double) recipe.getSingleIngredientIndex(i).getQuantity()) / 100.0;
            } else if (recipe.getSingleIngredientIndex(i).getUnit().equals("Metric Cooking Measures")) {
                //get the correct portion size (measurement)
                int fractionNum = recipe.getSingleIngredientIndex(i).getFractionNum();
                switch (fractionNum) {
                    case 0:
                        portionSize = 1.25;
                        break;
                    case 1:
                        portionSize = 2.5;
                        break;
                    case 2:
                        portionSize = 5.0;
                        break;
                    case 3:
                        portionSize = 15.0;
                        break;
                    case 4:
                        portionSize = 62.5;
                        break;
                    case 5:
                        portionSize = 250.0 / 3.0;
                        break;
                    case 6:
                        portionSize = 125;
                        break;
                    default:
                        portionSize = 250.0;
                        break;
                }
                //multiply the measure by the specified quantity
                portionSize *= (double) recipe.getSingleIngredientIndex(i).getQuantity();
                System.out.println("Portion Size = " + portionSize + "mL");
            } else if (recipe.getSingleIngredientIndex(i).getUnit().equals("Other")) {
                //look for the matching portion size
                for (int j = 0; j < recipe.getSingleIngredientIndex(i).getMeasures().size(); j++) {
                    if (recipe.getSingleIngredientIndex(i).getFractionName().equals(recipe.getSingleIngredientIndex(i).getSingleMeasureIndex(j).getName())) {
                        nutrientConv = recipe.getSingleIngredientIndex(i).getSingleMeasureIndex(j).getConversion() * recipe.getSingleIngredientIndex(i).getQuantity();
                        j = recipe.getSingleIngredientIndex(i).getMeasures().size() + 1;
                    }
                }
            }
            if (recipe.getSingleIngredientIndex(i).getUnit().equals("Metric Cooking Measures") || recipe.getSingleIngredientIndex(i).getUnit().equals("mL")) {
                for (int j = 0; j < recipe.getSingleIngredientIndex(i).getMeasures().size(); j++) {
                    int ID = recipe.getSingleIngredientIndex(i).getSingleMeasureIndex(j).getID();
                    double conversion = recipe.getSingleIngredientIndex(i).getSingleMeasureIndex(j).getConversion();
                    if (ID > 340 && ID < 380) {
                        nutrientConv = portionSize * (conversion / 100.0);
                        break;
                    } else if (ID == 383) {
                        nutrientConv = portionSize * (conversion / 125.0);
                        break;
                    } else if (ID == 415 || ID == 418 || ID == 939 || ID == 943) {
                        nutrientConv = portionSize * (conversion / 250.0);
                        break;
                    } else if (ID == 439) {
                        nutrientConv = portionSize * (conversion / 5.0);
                        break;
                    } else if (ID == 413) {
                        nutrientConv = portionSize * (conversion / 200.0);
                        break;
                    } else if (ID == 414) {
                        nutrientConv = portionSize * (conversion / 225.0);
                        break;
                    } else if (ID == 419) {
                        nutrientConv = portionSize * (conversion / 275.0);
                        break;
                    } else if (ID == 428) {
                        nutrientConv = portionSize * (conversion / 30.0);
                        break;
                    } else if (ID == 429) {
                        nutrientConv = portionSize * (conversion / 300.0);
                        break;
                    } else if (ID == 430) {
                        nutrientConv = portionSize * (conversion / 375.0);
                        break;
                    } else if (ID >= 385 && ID <= 387) {
                        nutrientConv = portionSize * (conversion / 15.0);
                        break;
                    } else if (ID == 388) {
                        nutrientConv = portionSize * (conversion / 150.0);
                        break;
                    } else if (ID == 389) {
                        nutrientConv = portionSize * (conversion / 175.0);
                        break;
                    } else if (ID == 638) {
                        nutrientConv = portionSize * (conversion / 50.0);
                        break;
                    } else if (ID == 640) {
                        nutrientConv = portionSize * (conversion / 60.0);
                        break;
                    } else if (ID == 641) {
                        nutrientConv = portionSize * (conversion / 75.0);
                        break;
                    } else if (ID == 923) {
                        nutrientConv = portionSize * (conversion / 45.0);
                        break;
                    } else if (ID == 932) {
                        nutrientConv = portionSize * (conversion / 80.0);
                        break;
                    }
                }
            }
            System.out.println("Conversion Rate = " + nutrientConv + "\n End of Ingredient\n");
            //add the individual nutrients to the recipe ingredients
            for (int j = 0; j < 870; j++) {
                if (indiviual[j] != null) {
                    recipeNutrients[j] += (indiviual[j] * nutrientConv);
                }
            }
        }
        if (pdf) {
            recipe.setNutrients(recipeNutrients.clone());
            return null;
        }
        //ask for the name of the recipe
        if (recipe.getTitle().equals("")) {
            //recipe.setTitle(JOptionPane.showInputDialog("Please enter the name of the recipe"));
        }
        return createOutput(recipe, recipeNutrients, label);
    }//End recipe()

    /**
     * This method collects all the nutrient amounts
     *
     * @param ID the food ID of the food item
     * @return an array with the nutrient ID and the amount of that nutrient
     */
    public static Double[] getNutrientData(int ID) {
        Double[] nutrients = new Double[870];
        int begin = binarySearch(ntAmt, ID, 0, ntAmt.size() - 1);
        if (begin == -1) {
            System.out.println("Not Found");
        }
        //step back to the beginning of the nutrient
        while (begin > 0 && (int) ntAmt.get(begin - 1)[0] == ID) {
            begin--;
        }
        //read until the end of the nutrient
        for (int i = begin; (int) ntAmt.get(i)[0] == ID; i++) {
            nutrients[(int) ntAmt.get(i)[1]] = (double) ntAmt.get(i)[2];
        }
//        for (int i = 0; i < ntAmt.size(); i++) {
//            //check if the food on the line matches the ID of the target
//            if (ID == (int) ntAmt.get(i)[0]) {
//                //if it does get the nutrent amount and add it to the array
//                nutrients[(int) ntAmt.get(i)[1]] = (double) ntAmt.get(i)[2];
//            }
//        }
        return nutrients;
    }//End getData()

    /**
     * Imports the recipes from the recipe folder
     *
     * @return an array list containing all the recipes in the folder
     */
    public static ArrayList<Recipe> importRecipes() {
        ArrayList<Recipe> database = new ArrayList<>();
        File folder = new File("recipes//");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().substring(file.getName().indexOf(".")).equals(".xml")) {
                System.out.println("true");
                database.add(open(file));
            }
        }
        return database;
    }

    /**
     * binary searches an array list for the specified ID
     *
     * @param list   the list to search
     * @param id     the ID to look for
     * @param bottom the bottom index of the search area
     * @param top    the top index of the search area
     * @return the index where the ID can be found
     */
    public static int binarySearch(ArrayList<Object[]> list, int id, int bottom, int top) {
        if (top < bottom) {
            return -1;
        }
        int middle = (top + bottom) / 2;
        //System.out.println("Middle " + (Integer) list.get(middle)[0] + "\nLine: " + middle);
        if (id < (Integer) list.get(middle)[0]) {
            return binarySearch(list, id, bottom, middle - 1);
        } else if (id > (Integer) list.get(middle)[0]) {
            return binarySearch(list, id, middle + 1, top);
        } else {
            return middle;
        }
    }
}
