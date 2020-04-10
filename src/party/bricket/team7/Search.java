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
        bSScraper = new BricksetScraper(searchQuery);
    }

    //Used to get searchQuery
    private String getSearchQuery(){
        String nothing = searchQuery;
        return nothing;
    }

    //I'm having a little trouble with this class.
    //I just need to know what else will be accessible from the scraper once it is done
    private void addSearchResult() throws NoSuchFieldException {
        ArrayList<String> ids = bSScraper.getIDs();
        ArrayList<String> names = bSScraper.getNames();
        ArrayList<String> links = bSScraper.getLinks();
        if(ids.size() != names.size()) {
            throw new NoSuchFieldException();
        }
        for(int i = 0; i < names.size(); i++) {
            SearchResult sR = new SearchResult();
            sR.setName(names.get(i));
            sR.setIdNumber(ids.get(i));
            sR.setBSLink(links.get(i));
            sResults.add(sR);
        }
    }

    //This will return a search result in sResult array with the given ID number
    private SearchResult getSearchResult(String ID){
        for(SearchResult sR: sResults){
            if(sR.getIdNumber().equals(ID)){
                return sR;
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