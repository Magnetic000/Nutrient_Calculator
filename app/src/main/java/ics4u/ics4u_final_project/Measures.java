/* Isaac Wismer
 *  Jun 17, 2015
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

    @Override
    public Measures clone() {
        Measures clone = new Measures(ID, conversion);
        clone.setName(name);
        return clone;
    }

}
