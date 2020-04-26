package party.bricket.team7.control;

import party.bricket.team7.data.ResearchResult;
import party.bricket.team7.data.Search;
import party.bricket.team7.data.SearchResult;
import party.bricket.team7.data.Speculator;

import java.util.Calendar;
import java.util.Iterator;

/**
 * The controller for Bricket to be a mediator between the model and a view
 * @author Matthew Hise
 */

public class BricketController {

    private Search search;

    final private Speculator spec;

    /**
     * Constructor for the controller
     */
    public BricketController() {
        // Initialize members
        search = null;
        spec = new Speculator();
    }

    /**
     * Gets an iterator to a list of search results found for a query
     * @param query the user's search query
     * @return an iterator to the list of search results found
     */
    public Iterator<SearchResult> refreshSearch(String query) {
        search = new Search(query);                                   // Creates a search for the query
        return search.getSearchIterator();                            // Returns an iterator to the list of results to the view
    }

    /**
     * Requests the speculator build a research result for a search result selected
     * @param index the index of the search result the user selected
     * @return the constructed research result
     */
    public ResearchResult selectSearchResult(int index) {
        spec.addResearchResult(search.getSearchResult(index));   // Get the search result at the selected index and add it to speculator
        System.out.println("Peak: $" + spec.calcValue());
        return spec.getResearchResult();                         // Return the research result created to the view
    }

    /**
     * Gets the currently selected research result from the speculator
     * @return the selected research result
     */
    public ResearchResult getResearchResult() {
        return spec.getResearchResult();
    }

    /**
     * Removes the currently selected research result from the speculator
     */
    public void removeResearchResult() {
        spec.removeResearchResult();
    }

    /**
     * Saves the currently selected research result to a file
     * @param path the path to the file to save the result to
     * @return true if save succeeded
     */
    public boolean saveToFile(String path) {
        ResearchIO io = new ResearchIO();                           // Create the ResearchIO object
        return io.saveResearch(spec.getResearchResult(),path);      // Save the research result to the file
    }

    /**
     * Load a research result from a file
     * @param path the path to the file where the result is saved
     * @return the loaded research result
     */
    public ResearchResult loadFromFile(String path) {
        ResearchIO io = new ResearchIO();                   // Create the ResearchIO object
        ResearchResult res = io.loadResearch(path);         // Attempt to open the file
        if(res == null) {                                   // If the result object was not successfully created
            return null;
        }

        spec.addResearchResult(res);                        // If successfully created result, add it to the speculator
        return spec.getResearchResult();                    // And return it to the view
    }

    /**
     * Update the release date of the currently selected research result in speculator
     * @param newDate the Calendar object representing the new date
     */
    public void updateReleaseDate(Calendar newDate) {
        spec.setReleaseDate(newDate);
    }

    /**
     * Update the retire date of the currently selected research result in speculator
     * @param newDate the Calendar object representing the new date
     */
    public void updateRetireDate(Calendar newDate) {
        spec.setRetireDate(newDate);
    }

    /**
     * Update the part count of the currently selected research result in speculator
     * @param count the new part count
     */
    public void updatePartCount(int count) {
        spec.setPartCount(count);
    }

    /**
     * Update the price per part of the currently selected research result in speculator
     * @param ppp the price per part in dollars
     */
    public void updatePricePerPart(double ppp) {
        spec.setPricePerPart(ppp);
    }

    /**
     * Update the retail price of the currently selected research result in speculator
     * @param price the new retail price in dollars
     */
    public void updatePrice(double price) {
        spec.setRetailPrice(price);
    }

    /**
     * Update the rating of the currently selected research result in speculator
     * @param rating a rating as an integer between 20 and 100
     */
    public void updateRating(int rating) {
        spec.setRating(rating);
    }

    /**
     * Set the currently selected research result in speculator
     * @param index
     */
    public void updateSelected(int index) {
        spec.setSelectedResult(index);
    }

    /**
     * Reset the currently selected research result in speculator to its unmodified state
     * @return true if the reset succeeded
     */
    public boolean resetToOG() {
        return spec.resetResearchResult();
    }
}
