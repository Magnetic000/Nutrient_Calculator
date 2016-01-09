/* Isaac Wismer
 *  Jun 17, 2015
 */
package ics4u.ics4u_final_project;

/**
 * @author isaac
 */
public class Measures {

    private int ID;
    private double conversion;
    private String name = "";

    /**
     * @param ID         The measure ID
     * @param conversion the measure conversion rate
     */
    public Measures(int ID, double conversion) {
        this.ID = ID;
        this.conversion = conversion;
    }

    /**
     * the default constructor
     */
    public Measures() {
    }

    /**
     * @return the ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the ID of the measure
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the Conversion
     */
    public double getConversion() {
        return conversion;
    }

    /**
     * @param conversion the measure conversion rate
     */
    public void setConversion(double conversion) {
        this.conversion = conversion;
    }

    /**
     * @return the name of the measure
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of the measure
     */
    public void setName(String name) {
        this.name = name;
    }

}
