package party.bricket.team7;

import java.util.ArrayList;

public class ResearchResult {
    ResearchResult(SearchResult item) {
        basicItemData = item;
    }

    private String id;                                                  // ID number in "####-####" format
    private String brickset;                                            // Brickset Link
    private String image;                                               // Link to image of item
    private String name;                                                // Set Name
    private double msrp;                                                // Manufacturer Suggested Retail Price
    private String theme;                                               // Theme of the set (Police, City, etc.)
    private String releaseDate;                                         // Release Date of set
    private int partCount;                                              // Part Count of the set
    private boolean retired;                                            // Retired Flag
    private String retireDate;                                          // Date of retirement, if applicable
    private double averageCurrentPrice;                                 // The current average resale price, calculated from selected databases
}
