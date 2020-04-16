package party.bricket.team7;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents one Lego item to be researched.
 *
 * @author Matthew Hise
 *
 */

public class ResearchResult {

    // Attributes that cannot be changed once initialized
    private final String id;                                            // ID string in "####-####" format
    private final String name;                                          // Item name
    private final String theme;                                         // Theme of the item (Police, City, etc.)
    private final String bricksetLink;                                  // Link to product page on Brickset
    private final String imageLink;                                     // Link to image of item
    private final boolean retired;                                      // Retired flag

    // These attributes will be able to be affected by the user
    private double retailPrice;                                         // Retail Price in dollars
    private double value;                                               // Average value (or estimated value, if not retired) in dollars
    private double pricePerPart;                                        // The price per piece in cents
    private ArrayList<String> minifigNames;                             // List of minifigs contained in the item
    private int rating;                                                 // Rating from 20-100
    private int partCount;                                              // Part count of the set
    private Date releaseDate;                                           // Release date of set
    private Date retireDate;                                            // Date of retirement, if applicable


    /**
     * Creates a new ResearchResult object
     */

    // MODIFY TO TAKE SEARCH RESULT!!
    //Edit by Daniel added parameters, change if not what we wanted,
    // I just tried to fix the errors.
    public ResearchResult(SearchResult searchRes, String thm, String imgLink, boolean ret) {
        id = searchRes.getId();                                   // Initialize ID string in "####-####" format
        name = searchRes.getName();                               // Initialize item name
        bricksetLink = searchRes.getItemLink();                   // Initialize link to product page on Brickset
        this.theme = thm;                                         // Initialize theme
        imageLink = imgLink;                                      // Initialize link to image of item
        this.retired = ret;                                   // Initialize retired flag

        retailPrice = 0.0;                                        // Initialize Recommended Retail Price in dollars
        value = 0.0;                                              // Initialize average value (or estimated value, if not retired) in dollars
        pricePerPart = 0.0;                                       // Initialize price per piece in cents
        rating = 0;                                               // Initialize rating
        partCount = 0;                                            // Initialize part count of the set
        releaseDate = new Date();                                 // Initialize release date of set
        retireDate = new Date();                                  // Initialize date of retirement
        minifigNames = new ArrayList<String>();                   // Initialize list of minifigs
    }

    /** Gets the item's ID string.
     * @return the item's ID string
     */
    public String getID() {
        return id;
    }

    /** Gets the item's name.
     * @return the item's name
     */
    public String getName() {
        return name;
    }

    /** Gets the item's theme.
     * @return the item's theme
     */
    public String getTheme() {
        return theme;
    }

    /** Gets the item's Brickset link.
     * @return the item's Brickset link
     */
    public String getBricksetLink() {
        return bricksetLink;
    }

    /** Gets the item's image link.
     * @return the item's image link
     */
    public String getImageLink() {
        return imageLink;
    }

    /** Gets the item's retired flag.
     * @return the item's retired flag
     */
    public boolean isRetired() {
        return retired;
    }

    /** Gets the item's retail price in dollars.
     * @return the item's retail price in dollars
     */
    public double getRetailPrice() {
        return retailPrice;
    }

    /** Sets the item's retail price in dollars.
     * @param price the item's retail price in dollars
     */
    public void setRetailPrice(double price) {
        retailPrice = price;
        pricePerPart = price / partCount * 100; // Update the linked attribute price per part
    }

    /** Gets the item's current average value (or estimated peak retirement value, if not yet retired) in dollars.
     * @return the item's current average value (or estimated peak retirement value, if not yet retired) in dollars
     */
    public double getValue() {
        return value;
    }

    /** Sets the item's current average value (or estimated peak retirement value, if not yet retired) in dollars.
     * @param price the item's current average value (or estimated peak retirement value, if not yet retired) in dollars
     */
    public void setValue(double price) {
        value = price;
    }

    /** Gets the item's price per piece in cents.
     * @return the item's price per piece in cents
     */
    public double getPricePerPart() {
        return pricePerPart;
    }

    /** Sets the item's price per piece in cents.
     * @param partPrice the item's price per piece in cents
     */
    public void setPricePerPart(double partPrice) {
        pricePerPart = partPrice;
        retailPrice = partPrice * partCount / 100; // Update the linked attribute retail price
    }

    /** Gets the item's rating as an integer between 20-100.
     * @return the item's rating as an integer between 20-100
     */
    public int getRating() {
        return rating;
    }

    /** Sets the item's rating as an integer between 20-100.
     * @param rating the item's rating as a double between 1.0 and 5.0
     */
    public void setRating(double rating) {
        this.rating = (int) (rating * 20);
    }

    /** Gets the minifig's name at an index.
     * @param i the index of the minifig whose name is to be returned, starting at 0
     * @return the minifig's name or an empty string if i is out of range
     */
    public String getMinifig(int i) {
        if (i >= minifigNames.size() || i < 0)
            return "";
        return minifigNames.get(i);
    }

    /** Adds a minifig to this item's list of minifigs.
     * @param name the minifig's name
     */
    public void addMinifig(String name) {
        minifigNames.add(name);
    }

    /** Gets the item's part count.
     * @return the item's part count
     */
    public int getPartCount() {
        return partCount;
    }

    /** Sets the item's part count.
     * @param count the item's part count
     */
    public void setPartCount(int count) {
        partCount = count;
        pricePerPart = retailPrice / count * 100; // Update the linked attribute price per part
    }

    /** Gets the item's release date.
     * @return a copy of the item's release date as a Date object reference
     */
    public Date getReleaseDate() {
        return (Date) releaseDate.clone();
    }

    /** Sets the item's release date.
     * @param releaseDate the item's release date
     */
    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = (Date) releaseDate.clone();
    }

    /** Gets the item's release date.
     * @return a copy of the item's release date as a Date object reference
     */
    public Date getRetireDate() {
        return (Date) retireDate.clone();
    }

    /** Sets the item's retire date.
     * @param retireDate the item's retire date
     */
    public void setRetireDate(Date retireDate) {
        this.retireDate = (Date) retireDate.clone();
    }

    /** Provides a string representation of the object.
     * @return a string of some of the ResearchResult's data
     */
    public String toString() {
        return "Item: " + id + " " + name + "\tValue: " + value;
    }

    /** Implementing an equals method for easy comparison
     * only comparing equality based on ID and name. We may want to add more
     * Author: Andrew
     */
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        else if(obj == null){
            return false;
        }
        else if(obj instanceof ResearchResult){
            ResearchResult RE = (ResearchResult) obj;
            if(RE.getName().equals(name) && RE.getID().equals(id)){
                return true;
            }
        }
        return false;
    }
}

