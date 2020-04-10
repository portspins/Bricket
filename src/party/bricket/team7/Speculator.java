package party.bricket.team7;

import java.util.ArrayList;

public class Speculator {
    private BricksetScraper bSScraper;
    private ArrayList<ResearchResult> specs = new ArrayList<ResearchResult>;

    void Speculator(){

    }

    /**The query taken from this method will be either be the original query or just the name and ID from the SearchResult
     * Either way, we may need to add a new method to SearchResult.
     * Either have it hold the original search query or have a "toQuery" method that will just return a string of the name and ID
     * @param res
     * @param query
     */
    private void addResearchResult(SearchResult res, String query) {
        bSScraper = new BricksetScraper(query);
        ResearchResult rResult = new ResearchResult();
        /**we can either add code here to be able to move the info from SearchResult to the current Research Result
         * or handle the conversion in the controller
         */

        specs.add(rResult);
    }
    private void modifyResearchResult(String setName){
        /**Search the list for the set based on the set name
         * Not entirely sure how else we wanted to format this part
         */
    }


}
