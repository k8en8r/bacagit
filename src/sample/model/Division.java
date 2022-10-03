package sample.model;

/** this class creates a Division object.
 *
 */
public class Division {
    public int divID;
    public String divName;

    /** this method creates a Division object with the given parameters.
     *
     * @param divID
     * @param divName
     */
    public Division(int divID, String divName){
        this.divID = divID;
        this.divName = divName;
    }

    @Override
    public String toString(){
        return divName;
    }
}
