package party.bricket.team7;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Iterator;

/**
 * Interface for any views that will interact with the BricketController
 * @author Matthew Hise
 */

public interface BricketView {
    public String getFilename();                                            // Gets the filename from the user and returns it
    public void viewSearchResults(Iterator<SearchResult> itr) throws IOException;              // Goes through list of SearchResults and outputs them
    public void submitSearchQuery();                                        // Gets the search query and submits it to the controller
    public boolean promptAddOrReplace();                                    // Determines if the user wants to open a new result or replace one
    public boolean promptSave();                                            // Determines if the user wants to save the current research
    public void viewResearchResult(ResearchResult result, boolean newRes);  // Outputs the updated list of Research
    public void submitRetailPrice(double price);                            // Sends the controller the modified retail price
    public void submitPricePerPart(double ppp);                             // Sends the controller the modified price per part
    public void submitRating(int rating);                                   // Sends the controller the modified rating from 20-100
    public void submitPartCount(int count);                                 // Sends the controller the modified part count
    public void submitReleaseDate(String date) throws ParseException;       // Sends the controller the modified release date
    public void submitRetireDate(String date) throws ParseException;        // Sends the controller the modified retire date
    public void submitSearchSelected(int index);                            // Sends the controller the selected search result number
    public void resetTabWithResearchResult();                               // Messages the controller to reset the open research result
}
