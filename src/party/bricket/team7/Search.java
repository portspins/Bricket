package party.bricket.team7;

/**Search class will be responsible for:
 * Maintaining a list of results
 * Takes a query from main
 * Uses an instantiation of BrickSet Scraper
 */
import java.util.ArrayList;


public class Search {
    //Used to store input query from user and send it to the scraper
    private String searchQuery;
    //Used to store Search Results from the scraper
    private ArrayList<SearchResult> sResults = new ArrayList<SearchResult>();
    //Object to access scraper
    private BricksetScraper bSScraper;

    //Constructor
    private void Search(String query){
        searchQuery = query;
        bSScraper = new BricksetScraper();
        bSScraper.BricksetScraper(searchQuery);

    }

    //Used to get searchQuery
    private String getSearchQuery(){
        String nothing = searchQuery;
        return nothing;
    }

    //I'm having a little trouble with this class.
    //I just need to know what else will be accessible from the scraper once it is done
    private void addSearchResult(){
        SearchResult sR = new SearchResult(bSScraper.getName(), bSScraper.getID());
        sResults.add(sR);
    }

    //This will return a search result in sResult array with the given ID number
    private SearchResult getSearchResult(Integer ID){
        Integer i = 0;

        for(SearchResult sR: sResults){
            if(sResults.get(i).getIdNumber()==ID){
                return sResults.get(i);
            }
            else{
                i++;
            }
        }
        System.out.println("Could not find search result");
        return null;
    }

    //This will display all the search results currently inside sResults
    private void displaySearchResults(){

        for(SearchResult i: sResults){
            System.out.println(i.toString());
        }
    }

}