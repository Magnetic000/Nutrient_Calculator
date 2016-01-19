/* Isaac Wismer
 *  Jun 14, 2015
 * This class contains all the data for a recipe
 */
package ics4u.ics4u_final_project;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * @author isaac
 */
public class Recipe {

    private static final Font TITLE_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
    private static final Font LABEL_FONT = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
    private static final Font LABEL_NORMAL = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
    private static final Font LABEL_SMALL = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
    private static final Font LABEL_BOLD = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
    private Double[] nutrients;
    private String title = "", instructions = "", servingName = "";
    private int servings = 1, photo;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();

    /**
     * @param title        the Title of the recipe
     * @param instructions the instructions for the recipe
     */
    public Recipe(String title, String instructions) {
        this.title = title;
        this.instructions = instructions;
    }

    /**
     * @param title the title of the recipe
     */
    public Recipe(String title) {
        this.title = title;
    }

    public Recipe(String title, int photo) {
        this.title = title;
        this.photo = photo;
    }

    /**
     * the default constructor
     */
    public Recipe() {
    }

    /**
     * @return the title of the recipe
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title of the recipe
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the instructions for the recipe
     */
    public String getInstructions() {
        return instructions;
    }

    /**
     * @return the number of ingredients
     */
    public int size() {
        return ingredients.size();
    }

    /**
     * @param instructions instructions for the recipe
     */
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    /**
     * @return
     */
    public String getServingName() {
        return servingName;
    }

    /**
     * @param servingName
     */
    public void setServingName(String servingName) {
        this.servingName = servingName;
    }

    /**
     * @return the number of servings it makes
     */
    public int getServings() {
        return servings;
    }

    /**
     * @param servings the number of servings it makes
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * @param ingred an ingredient object to add to the recipe
     */
    public void addIngredient(Ingredient ingred) {
        ingredients.add(ingred);
    }

    /**
     * @param index  the index to overwrite/place the ingredient at
     * @param ingred the ingredient to place
     */
    public void setSingleIngredient(int index, Ingredient ingred) {
        ingredients.set(index, ingred);
    }

    /**
     * @return the entire array of ingredients
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * @param ingredients the entire array of ingredients
     */
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    /**
     * @param index the index of the ingredient
     * @return the ingredient object from the specified index
     */
    public Ingredient getSingleIngredientIndex(int index) {
        return ingredients.get(index);
    }

    /**
     * @param ID the ID of the ingredient you want
     * @return the ingredient with the matching ID
     */
    public Ingredient getSingleIngredientID(int ID) {
        for (int i = 0; i < ingredients.size(); i++) {
            if (ingredients.get(i).getID() == ID) {
                return ingredients.get(i);
            }
        }
        return null;
    }

    /**
     * @param index the index of the ingredient to remove
     */
    public void remove(int index) {
        ingredients.remove(index);
    }

    /**
     * @return the list of ingredients for a combobox
     */
    public ArrayList<String> getList() {
        return list;
    }

    /**
     * @param list the list of ingredients for a combobox
     */
    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    /**
     * @param item the item to be added to the list
     */
    public void addListItem(String item) {
        list.add(item);
    }

    /**
     * returns the nutrients array for the recipe
     *
     * @return a double array containing the nutrient data
     */
    public Double[] getNutrients() {
        return nutrients;
    }

    /**
     * sets the nutrients data for this recipe
     *
     * @param nutrients a double array containing the nutrient data
     */
    public void setNutrients(Double[] nutrients) {
        this.nutrients = nutrients;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getPhoto() {
        return photo;
    }

    @Override
    public Recipe clone() {
        Recipe clone = new Recipe(title, instructions);
        clone.setPhoto(photo);
        clone.setServings(servings);
        clone.setServingName(servingName);
        clone.setIngredients((ArrayList<Ingredient>) ingredients.clone());
        return clone;
    }


    /**
     * @param r another recipe
     * @return whether or not they are equal
     */
    public boolean equals(Recipe r) {
        if (r == null) {
            return false;
        }
        if (getClass() != r.getClass()) {
            return false;
        }
        if (!Objects.equals(this.title, r.title)) {
            return false;
        }
        if (!Objects.equals(this.instructions, r.instructions)) {
            return false;
        }
        if (!Objects.equals(this.ingredients, r.ingredients)) {
            return false;
        }
        if (!Objects.equals(this.list, r.list)) {
            return false;
        }
        return true;
    }

    /**
     * Exports the recipe to a PDF
     *
     * @param path the path to save the recipe to
     */
    public void export(File path) {
        //get the Data
        Database.recipe(this, true, true);
        //create the new document
        Document doc = new Document();
        try {
            //create the file
            PdfWriter.getInstance(doc, new FileOutputStream(path));
        } catch (FileNotFoundException | DocumentException ex) {
            System.out.println("Error: " + ex.toString());
        }
        //set the margins
        doc.setMargins(36, 36, 18, 18);
        doc.open();
        //add properties to the document
        addMetaData(doc);
        //add the data to the document
        addData(doc);
        //close the document, finish writing to disk
        doc.close();
    }

    private void addMetaData(Document doc) {
        //add all the meta data of the docmuent
        doc.addTitle(title);
        doc.addSubject("Recipies");
        doc.addKeywords("recipe, " + title);
        doc.addAuthor("Isaac Wismer");
        doc.addCreator("Nutrient Calculator. Made using the iTextPDF Library 5.5.8 http://itextpdf.com/");
    }

    private void addData(Document doc) {
        //create the format for the % Daily Value
        DecimalFormat onePer = new DecimalFormat("#,##0%");
        try {
            //add the title of the recipe
            doc.add(new Paragraph(title, TITLE_FONT));
            //add each of the ingredients
            for (int i = 0; i < ingredients.size(); i++) {
                doc.add(new Paragraph(ingredients.get(i).getFormattedName(), LABEL_NORMAL));
            }
            //add the instructions
            Paragraph p = new Paragraph();
            if (!instructions.equals("")) {
                p = new Paragraph(new Phrase("Instructions:\n", LABEL_FONT));
                doc.add(p);
                p = new Paragraph();
                p.add(new Phrase(instructions + "\n", LABEL_NORMAL));
            } else {
                p.add(new Phrase("\n"));
            }
            //create the nutrient value table
            PdfPTable label = new PdfPTable(10);
            label.setWidthPercentage(30);
            //Top
            PdfPCell c = new PdfPCell(new Phrase("Nutrition Facts", LABEL_FONT));
            c.setColspan(10);
            c.setBorderWidthBottom(0);
            label.addCell(c);
            c = new PdfPCell(new Phrase("Per " + servingName, LABEL_BOLD));
            c.setColspan(10);
            c.setBorderWidthTop(0);
            label.addCell(c);
            //Amount
            PdfPCell c2a = new PdfPCell(new Phrase("Amount", LABEL_SMALL));
            c2a.setColspan(3);
            c2a.setBorderWidthRight(0);
            PdfPCell c2b = new PdfPCell(new Phrase("% Daily Value", LABEL_SMALL));
            c2b.setColspan(10);
            c2b.setHorizontalAlignment(Element.ALIGN_RIGHT);
            c2b.setBorderWidthLeft(0);
            label.addCell(c2a);
            label.addCell(c2b);
            //Calories
            c = new PdfPCell(new Phrase("Calories " + (int) (nutrients[208] / servings), LABEL_BOLD));
            c.setColspan(10);
            c.setBorderWidthTop(2);
            label.addCell(c);
            //Fat
            PdfPCell ca = new PdfPCell(new Phrase("Fat " + (int) (nutrients[204] / servings) + "g", LABEL_BOLD));
            ca.setColspan(6);
            ca.setBorderWidthRight(0);
            PdfPCell cb = new PdfPCell(new Phrase(onePer.format((nutrients[204] / servings) / 60.0), LABEL_BOLD));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            label.addCell(ca);
            label.addCell(cb);
            //fat 2
            ca = new PdfPCell(new Phrase("Saturated " + ((int) (nutrients[606] / servings)) + "g\n\t+ Trans " + ((int) (nutrients[605] / servings)) + "g", LABEL_NORMAL));
            ca.setColspan(6);
            ca.setIndent(16);
            ca.setBorderWidthRight(0);
            cb = new PdfPCell(new Phrase(onePer.format((nutrients[606] / servings) / 20.0), LABEL_BOLD));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            label.addCell(ca);
            label.addCell(cb);
            //Cholesterol
            c = new PdfPCell(new Phrase("Cholesterol " + ((int) (nutrients[601] / servings)) + "mg", LABEL_BOLD));
            c.setColspan(10);
            label.addCell(c);
            //Sodium
            ca = new PdfPCell(new Phrase("Sodium " + (int) (nutrients[307] / servings) + "mg", LABEL_BOLD));
            ca.setColspan(6);
            ca.setBorderWidthRight(0);
            cb = new PdfPCell(new Phrase(onePer.format((nutrients[307] / servings) / 2400.0), LABEL_BOLD));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            label.addCell(ca);
            label.addCell(cb);
            //Carbs
            ca = new PdfPCell(new Phrase("Carbohydrate " + (int) (nutrients[205] / servings) + "g", LABEL_BOLD));
            ca.setColspan(7);
            ca.setBorderWidthRight(0);
            cb = new PdfPCell(new Phrase(onePer.format((nutrients[205] / servings) / 300.0), LABEL_BOLD));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            label.addCell(ca);
            label.addCell(cb);
            //Fibre
            ca = new PdfPCell(new Phrase("Fibre " + (int) (nutrients[291] / servings) + "g", LABEL_NORMAL));
            ca.setColspan(6);
            ca.setIndent(16);
            ca.setBorderWidthRight(0);
            cb = new PdfPCell(new Phrase(onePer.format((nutrients[291] / servings) / 25.0), LABEL_BOLD));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            label.addCell(ca);
            label.addCell(cb);
            //Sugar
            ca = new PdfPCell(new Phrase("Sugars " + (int) (nutrients[269] / servings) + "g", LABEL_NORMAL));
            ca.setColspan(10);
            ca.setIndent(16);
            label.addCell(ca);
            //Protein
            ca = new PdfPCell(new Phrase("Protein " + (int) (nutrients[203] / servings) + "g", LABEL_BOLD));
            ca.setColspan(10);
            label.addCell(ca);
            //Vit A
            ca = new PdfPCell(new Phrase("Vitamin A", LABEL_NORMAL));
            ca.setColspan(6);
            ca.setBorderWidthRight(0);
            ca.setBorderWidthTop(2);
            cb = new PdfPCell(cb = new PdfPCell(new Phrase(onePer.format(((nutrients[319] + (nutrients[321] / 12) + (nutrients[834] / 24)) / servings) / 1000), LABEL_NORMAL)));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            cb.setBorderWidthTop(2);
            label.addCell(ca);
            label.addCell(cb);
            //Vit C
            ca = new PdfPCell(new Phrase("Vitamin C", LABEL_NORMAL));
            ca.setColspan(6);
            ca.setBorderWidthRight(0);
            cb = new PdfPCell(new Phrase(onePer.format((nutrients[401] / servings) / 60.0), LABEL_NORMAL));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            label.addCell(ca);
            label.addCell(cb);
            //Calcium
            ca = new PdfPCell(new Phrase("Calcium", LABEL_NORMAL));
            ca.setColspan(6);
            ca.setBorderWidthRight(0);
            cb = new PdfPCell(new Phrase(onePer.format((nutrients[301] / servings) / 1100.0), LABEL_NORMAL));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            label.addCell(ca);
            label.addCell(cb);
            //Iron
            ca = new PdfPCell(new Phrase("Iron", LABEL_NORMAL));
            ca.setColspan(6);
            ca.setBorderWidthRight(0);
            cb = new PdfPCell(new Phrase(onePer.format((nutrients[303] / servings) / 14.0), LABEL_NORMAL));
            cb.setColspan(10);
            cb.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cb.setBorderWidthLeft(0);
            label.addCell(ca);
            label.addCell(cb);
            //Add to the table
            label.setHorizontalAlignment(Element.ALIGN_LEFT);
            p.add(label);
            //add to the document
            doc.add(p);

        } catch (DocumentException ex) {
            System.out.println("Error: " + ex.toString());
        }
    }

    /**
     * This method saves the recipe to a file
     *
     * @param file The file path
     * @throws FileNotFoundException
     */
    public void save(File file) throws FileNotFoundException {
        Database.recipe(this, true, true);
        //PrintWriter p = new PrintWriter(file + ".txt");
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            org.w3c.dom.Document doc = docBuilder.newDocument();
            org.w3c.dom.Element rootElement = doc.createElement("recipe");
            doc.appendChild(rootElement);

//             title element
            org.w3c.dom.Element title = doc.createElement("title");
            title.appendChild(doc.createTextNode(this.title + ""));
//            p.println(GUI.recipe.getTitle());
            rootElement.appendChild(title);
//             title element
            org.w3c.dom.Element instructions = doc.createElement("instructions");
            instructions.appendChild(doc.createTextNode(this.instructions + ""));
//            p.println(GUI.recipe.getInstructions());
            rootElement.appendChild(instructions);

            org.w3c.dom.Element servings = doc.createElement("servings");
            servings.appendChild(doc.createTextNode(this.servings + ""));
//            p.println(GUI.recipe.getInstructions());
            rootElement.appendChild(servings);

            org.w3c.dom.Element servingName = doc.createElement("servingName");
            servingName.appendChild(doc.createTextNode(this.servingName + ""));
//            p.println(GUI.recipe.getInstructions());
            rootElement.appendChild(servingName);

            org.w3c.dom.Element photo = doc.createElement("photo");
            photo.appendChild(doc.createTextNode(this.photo + ""));
//            p.println(GUI.recipe.getInstructions());
            rootElement.appendChild(photo);

            for (int i = 0; i < ingredients.size(); i++) {
                org.w3c.dom.Element ingredients = doc.createElement("ingredients");
                rootElement.appendChild(ingredients);
                Ingredient ing = this.ingredients.get(i);

                org.w3c.dom.Element id = doc.createElement("ID");
                id.appendChild(doc.createTextNode(ing.getID() + ""));
                ingredients.appendChild(id);
//                p.println(ing.getID());

                org.w3c.dom.Element name = doc.createElement("name");
                name.appendChild(doc.createTextNode(ing.getName()));
                ingredients.appendChild(name);
//                p.println(ing.getName());

                org.w3c.dom.Element formattedName = doc.createElement("formattedName");
                formattedName.appendChild(doc.createTextNode(ing.getFormattedName()));
                ingredients.appendChild(formattedName);
//                p.println(ing.getFormattedName());

                org.w3c.dom.Element unitName = doc.createElement("unitName");
                unitName.appendChild(doc.createTextNode(ing.getUnit()));
                ingredients.appendChild(unitName);
//                p.println(ing.getUnit());

                org.w3c.dom.Element unitNum = doc.createElement("unitNum");
                unitNum.appendChild(doc.createTextNode(ing.getUnitNum() + ""));
                ingredients.appendChild(unitNum);
//                p.println(ing.getUnitNum());

                org.w3c.dom.Element fractionName = doc.createElement("fractionName");
                fractionName.appendChild(doc.createTextNode(ing.getFractionName()));
                ingredients.appendChild(fractionName);
//                p.println(ing.getFractionName());

                org.w3c.dom.Element fractionNum = doc.createElement("fractionNum");
                fractionNum.appendChild(doc.createTextNode(ing.getFractionNum() + ""));
                ingredients.appendChild(fractionNum);
//                p.println(ing.getFractionNum());

                org.w3c.dom.Element quantity = doc.createElement("quantity");
                quantity.appendChild(doc.createTextNode(ing.getQuantity() + ""));
                ingredients.appendChild(quantity);
//                p.println(ing.getQuantity());

                for (int j = 0; j < ing.getMeasures().size(); j++) {
                    org.w3c.dom.Element measures = doc.createElement("measures");
                    ingredients.appendChild(measures);
                    Measures m = ing.getSingleMeasureIndex(j);

                    org.w3c.dom.Element measureID = doc.createElement("id");
                    measureID.appendChild(doc.createTextNode(m.getID() + ""));
                    measures.appendChild(measureID);
//                    p.println(m.getID());

                    org.w3c.dom.Element conversion = doc.createElement("conversion");
                    conversion.appendChild(doc.createTextNode(m.getConversion() + ""));
                    measures.appendChild(conversion);
//                    p.println(m.getConversion());

                    org.w3c.dom.Element measureName = doc.createElement("measureName");
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
}
