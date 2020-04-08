package party.bricket.team7;

import java.util.*;
/** party.bricket.team7.SearchResult: A class that will act as a data type for a single item
 * from a brickset search's result list.
 * Stored inside are:
 * ID#, Brickset page link, Thumbnail link, Name, MSRP, Theme, Release Date,
 * Part Count, Retirement Date (if applicable), and a list of Minifigures in the set.
 * @author Daniel Morris
 */
public class SearchResult {
    private int idNumber = 0;                                           // ID number in "####-####" format
    private String brickset = "";                                       // Brickset Link
    private String thumbnail = "";                                      // Thumbnail Link
    private String name = "";                                           // Set Name
    private double msrp = 0.0;                                          // Manufacturer Suggested Retail Price
    private String theme = "";                                          // Theme of the set (Police, City, etc.)
    private String releaseDate = "";                                    // Release Date of set
    private int partCount = 0;                                          // Part Count of the set
    private boolean retired = false;                                       // Retired Flag
    private String retireDate = "";                                     // Date of retirement, if applicable
    private ArrayList<String> minifigNames = new ArrayList<>();   // List of Minifigs in the set
    /** Full Constructor. Takes all inputs to fill out the attributes.
     */
    public SearchResult(int idNumberIn, String bricksetIn, String thumbnailIn, String nameIn, double msrpIn, String themeIn, String releaseDateIn, int partCountIn, boolean retiredFlag, String retireDateIn, ArrayList<String> minifigNamesIn) {
        idNumber = idNumberIn;
        brickset = bricksetIn;
        thumbnail = thumbnailIn;
        name = nameIn;
        msrp = msrpIn;
        theme = themeIn;
        releaseDate = releaseDateIn;
        partCount = partCountIn;
        retired = retiredFlag;
        retireDate = retireDateIn;
        minifigNames = minifigNamesIn;
    }

    /** GetIDNumber.
     *Retrieves a copy of the id number.
     */
    public int getIdNumber() {
        int decoy = idNumber;
        return decoy;
    }

    /**SetIDNumber.
     *Sets the id number to the input parameter.
     */
    public boolean setIdNumber(int id) {
        idNumber = id;
        return true;
    }

    /**GetBSLink.
     *Retrieves a copy of the brickset link.
     */
    public String getBSLink() {
        String decoy = brickset;
        return decoy;
    }

    /**SetBSLink.
     *Sets the brickset link to the input parameter.
     */
    public boolean setBSLink(String link) {
        brickset = link;
        return true;
    }

    /**getThumbnail.
     *Retrieves a copy of the thumbnail link.
     */
    public String getThumbnail() {
        String decoy = thumbnail;
        return decoy;
    }

    /**setThumbnail.
     *Sets the thumbnail link to the input parameter.
     */
    public boolean setThumbnail(String TN) {
        thumbnail = TN;
        return true;
    }

    /**getName.
     *Retrieves a copy of the name.
     */
    public String getName() {
        String decoy = name;
        return decoy;
    }

    /**setName.
     *Sets the name to the input parameter.
     */
    public boolean setName(String nameIn) {
        name = nameIn;
        return true;
    }

    /**getMSRP
     *Retrieves a copy of the msrp.
     */
    public double getMSRP() {
        double decoy = msrp;
        return decoy;
    }

    /**setMSRP
     *Sets the msrp to the input parameter.
     */
    public boolean setMSRP(double price) {
        msrp = price;
        return true;
    }

    /**getTheme
     *Retrieves a copy of the theme.
     */
    public String getTheme() {
        String decoy = theme;
        return decoy;
    }

    /**setTheme
     *Sets the name to the input parameter.
     */
    public boolean setTheme(String thm) {
        theme = thm;
        return true;
    }

    /**getReleaseDate
     *Retrieves a copy of the Release Date.
     */
    public String getReleaseDate() {
        String decoy = releaseDate;
        return decoy;
    }

    /**setReleaseDate
     *Sets the release date to the input parameter.
     */
    public boolean setReleaseDate(String rd) {
        releaseDate = rd;
        return true;
    }

    /**getPC
     *Retrieves a copy of the part count.
     */
    public int getPC() {
        int decoy = partCount;
        return decoy;
    }

    /**setPC
     *Sets the part count to the input parameter.
     */
    public boolean setPC(int pc) {
        partCount = pc;
        return true;
    }

    /**getRetireFlag
     *Retrieves a copy of the retire flag.
     */
    public boolean getRetireFlag() {
        boolean decoy = retired;
        return decoy;
    }

    //There is only a get method for the Retired flag

    /**getRetireDate
     *Retrieves a copy of the retirement date.
     */
    public int getRetireDate() {
        int decoy = partCount;
        return decoy;
    }

    /**setRetireDate
     *Sets the part count to the retirement date.
     */
    public boolean setRetirementDate(String rt) {
        retireDate = rt;
        retired = rt.compareTo("") != 0;
        return true;
    }

    /**getList
     *Retrieves a copy of the minifig list.
     */
    public ArrayList<String> getList() {
        ArrayList<String> decoy = minifigNames;
        return decoy;
    }

    /**setList
     *Sets the minifig list to the input parameter.
     */
    public boolean setList(ArrayList<String> minis) {
        minifigNames = minis;
        return true;
    }

    /**toString()
     * Default print method, outputs a string of the SearchResult's
     * data that will be used in the list created by the GUI.
     */
    public String toString() {
        String excerpt = idNumber + " " + thumbnail + " " + name + " " + theme;
        return excerpt;
    }
}
