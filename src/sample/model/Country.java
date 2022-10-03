package sample.model;

/** this class creates a Country object.
 *
 */
public class Country {
    public int countryID;
    public String countryName;

    /** this method creates a Country object with the given parameters.
     *
     * @param countryID
     * @param countryName
     */
    public Country(int countryID, String countryName){
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public int getCountryID(){
        return countryID;
    }

    @Override
    public String toString(){
        return countryName;
    }
}
