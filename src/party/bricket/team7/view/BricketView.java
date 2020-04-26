package party.bricket.team7.view;

import party.bricket.team7.data.ResearchResult;
import party.bricket.team7.data.SearchResult;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

/**
 * Interface for any views that will interact with the BricketController
 * @author Matthew Hise
 */

public interface BricketView {
    void viewSearchResults(Iterator<SearchResult> itr) throws IOException;  // Goes through list of SearchResults and outputs them
    void submitSearchQuery();                                        // Gets the search query and submits it to the controller
    void viewResearchResult(ResearchResult result, boolean newRes);  // Outputs the updated list of Research
    void submitRetailPrice(double price);                            // Sends the controller the modified retail price
    void submitPricePerPart(double ppp);                             // Sends the controller the modified price per part
    void submitRating(int rating);                                   // Sends the controller the modified rating from 20-100
    void submitPartCount(int count);                                 // Sends the controller the modified part count
    void submitReleaseDate(String date) throws ParseException;       // Sends the controller the modified release date
    void submitRetireDate(String date) throws ParseException;        // Sends the controller the modified retire date
    void submitSearchSelected(int index);                            // Sends the controller the selected search result number
    void resetTabWithResearchResult();                               // Messages the controller to reset the open research result
}
