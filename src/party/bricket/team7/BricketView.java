package party.bricket.team7;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

/**
 * Interface for any views that will interact with the BricketController
 * @author Matthew Hise
 */

public interface BricketView {
    public String getFilename();                                            // Gets the filename from the user and returns it
    public void viewSearchResults(Iterator<SearchResult> itr) throws IOException;              // Goes through list of SearchResults and outputs them
    public void submitSearchQuery();                                        // Gets the search query and submits it to the controller
    public int getScope();                                                  // Gets the scope selected and returns it
    public boolean promptAddOrReplace();                                    // Determines if the user wants to open a new result or replace one
    public boolean promptSave();                                            // Determines if the user wants to save the current research
    public void viewResearchResult(ResearchResult results);                 // Outputs the updated list of Research
    public void submitRetailPrice();                                        // Sends the controller the modified retail price
    public void submitPricePerPart();                                       // Sends the controller the modified price per part
    public void submitMinifigName();                                        // Sends the controller the new minifig name
    public void submitRating();                                             // Sends the controller the modified rating from 20-100
    public void submitPartCount();                                          // Sends the controller the modified part count
    public void submitReleaseDate();                                        // Sends the controller the modified release date
    public void submitRetireDate();                                         // Sends the controller the modified retire date
    public void submitSearchSelected();                                     // Sends the controller the selected search result number
    public void submitResetResult();                                        // Messages the controller to reset the open research result
}
