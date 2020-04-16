package party.bricket.team7;

import java.util.*;

/** party.bricket.team7.SearchResult: A class that will act as a data type for a single item
 * from a brickset search's result list.
 * Stores ID#, Name, Release Year, Brickset page link, Thumbnail link
 * @author Daniel Morris
 */
public class SearchResult {
    private String id;                                                  // ID number in "####-####" format
    private String name;                                                // Item name
    private int releaseYear;                                            // Item release year
    private String itemLink;                                            // Item's Brickset Link
    private String thumbnailLink;                                       // Thumbnail Link


    /** Full Constructor. Takes all inputs to fill out the attributes.
     */
    public SearchResult(String id, String name, int releaseYear, String itemLink, String thumbnailLink) {
        this.id = id;
        this.name = name;
        this.releaseYear = releaseYear;
        this.itemLink = itemLink;
        this.thumbnailLink = thumbnailLink;
    }

    /**
     * Default constructor
     */
    public SearchResult() {

    }

    /** GetId
     *Retrieves a copy of the id number.
     */
    public String getId() {
        return id;
    }

    /** GetItemLink.
     *Retrieves the brickset link.
     */
    public String getItemLink() {
        return itemLink;
    }

    /**getThumbnail.
     *Retrieves the thumbnail link.
     */
    public String getThumbnailLink() {
        return thumbnailLink;
    }

    /**getName.
     *Retrieves the name.
     */
    public String getName() {
        return name;
    }

    /**getReleaseYear
     * Retrieves the release year.
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**toString()
     * Default print method, outputs a string of the SearchResult's
     * data that will be used in the list created by the GUI.
     */
    public String toString() {
        return id + " " + name + " " + releaseYear;
    }
}
