/* Isaac Wismer
 * 
 */
package ics4u.ics4u_final_project;

import java.util.ArrayList;

/**
 *
 * @author isaac
 */
public class Ingredient {

    //food ID number, the cmb number for the fraction, the cmb num for the unit, quantity
    private int id, fractionNum, unitNum, quantity;
    //Short name, what unit is used, Formatted name (with correct quantity in front), the name of the fraction/other unit
    private String name, unit, formattedName, FractionName;
    private ArrayList<Measures> measures = new ArrayList<>();

    /**
     *
     * @param i The ID of the ingredient
     * @param s The name of the ingredient
     */
    public Ingredient(int i, String s) {
        id = i;
        name = s;
    }

    /**
     * Constructor method
     */
    public Ingredient() {
    }

    /**
     *
     * @return the ID of the ingredient
     */
    public int getID() {
        return id;
    }

    /**
     *
     * @return the name of the ingredient
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return the quantity of the the ingredient
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @return the unit used for the ingredient
     */
    public String getUnit() {
        return unit;
    }

    /**
     *
     * @param i the ID of the ingredient
     */
    public void setID(int i) {
        id = i;
    }

    /**
     *
     * @param s the name of the ingredient
     */
    public void setName(String s) {
        name = s;
    }

    /**
     *
     * @param q the quantity of the ingredient
     */
    public void setQuantity(int q) {
        quantity = q;
    }

    /**
     *
     * @param u the Unit of the ingredient
     */
    public void setUnit(String u) {
        unit = u;
    }

    /**
     *
     * @return the index in the combobox of the fraction
     */
    public int getFractionNum() {
        return fractionNum;
    }

    /**
     *
     * @param fractionNum the index in the combobox of the fraction
     */
    public void setFractionNum(int fractionNum) {
        this.fractionNum = fractionNum;
    }

    /**
     *
     * @return The formatted name for output to the user
     */
    public String getFormattedName() {
        return formattedName;
    }

    /**
     *
     * @param formattedName The formatted name for output to the user
     */
    public void setFormattedName(String formattedName) {
        this.formattedName = formattedName;
    }

    /**
     *
     * @return the name of the fraction
     */
    public String getFractionName() {
        return FractionName;
    }

    /**
     *
     * @param FractionName the name of the fraction
     */
    public void setFractionName(String FractionName) {
        this.FractionName = FractionName;
    }

    /**
     *
     * @return the combobox index of the unit
     */
    public int getUnitNum() {
        return unitNum;
    }

    /**
     *
     * @param unitNum the combobox index of the unit
     */
    public void setUnitNum(int unitNum) {
        this.unitNum = unitNum;
    }

    /**
     *
     * @return a full list of measures
     */
    public ArrayList<Measures> getMeasures() {
        return measures;
    }

    /**
     *
     * @param measures set a full list of measures
     */
    public void setMeasures(ArrayList<Measures> measures) {
        this.measures = measures;
    }

    /**
     *
     * @param ID the ID of the measure
     * @return the matching measure object
     */
    public Measures getSingleMeasureID(int ID) {
        for (int i = 0; i < measures.size(); i++) {
            if (measures.get(i).getID() == ID) {
                return measures.get(i);
            }
        }
        return null;
    }

    /**
     *
     * @param ID the ID of the measure
     * @param name the name of the measure
     */
    public void setMeasures(int ID, String name) {
        for (int i = 0; i < measures.size(); i++) {
            if (measures.get(i).getID() == ID) {
                measures.get(i).setName(name);
            }
        }
    }

    /**
     *
     * @param ID the ID of the measure
     * @param conversion the conversion rate of the measure
     */
    public void addMeasure(int ID, double conversion) {
        measures.add(new Measures(ID, conversion));
    }

    /**
     *
     * @param measure the measure to add, as an object
     */
    public void addMeasureFull(Measures measure) {
        measures.add(measure);
    }

    /**
     *
     * @param index the index of the measure to remove
     */
    public void removeSingleMeasure(int index) {
        measures.remove(index);
    }

    /**
     *
     * @param index the position of the mesure
     * @return the specified measure object
     */
    public Measures getSingleMeasureIndex(int index) {
        return measures.get(index);
    }

    /**
     * Checks if 2 ingredients are equal
     * @param in Another ingedient object
     * @return a boolean with true of they are equal, false if not
     */
    public boolean equals(Ingredient in) {
        if (in == null) {
            return false;
        }
        if (getClass() != in.getClass()) {
            return false;
        }
        if (this.id != in.id) {
            return false;
        }
        if (this.quantity != in.quantity) {
            return false;
        }
        return true;
    }

}
