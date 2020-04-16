package party.bricket.team7;

/**Search class will be responsible for:
 * Maintaining a list of results
 * Takes a query from main
 * Uses an instantiation of BrickSet Scraper
 */
import java.util.ArrayList;
import java.util.Iterator;


public class Search {
    //Used to store input query from user and send it to the scraper
    private String searchQuery;
    //Used to store Search Results from the scraper
    private ArrayList<SearchResult> sResults = new ArrayList<SearchResult>();
    //Object to access scraper
    private BricksetSearchScraper bSScraper;

    //Constructor
    public Search(String query) {
        searchQuery = query;
        bSScraper = new BricksetSearchScraper(searchQuery);
        try {
            addSearchResults();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    //Used to get searchQuery
    public String getSearchQuery() {
        return searchQuery;
    }

    //I'm having a little trouble with this class.
    //I just need to know what else will be accessible from the scraper once it is done
    private void addSearchResults() throws NoSuchFieldException {
        ArrayList<String> ids = bSScraper.getIDs();
        ArrayList<String> names = bSScraper.getNames();
        ArrayList<String> links = bSScraper.getLinks();
        ArrayList<String> thumbnails = bSScraper.getThumbnails();
        if(ids.size() != names.size()) {
            throw new NoSuchFieldException();
        }
        for(int i = 0; i < names.size(); i++) {
            SearchResult sR = new SearchResult(ids.get(i), names.get(i), 2000, links.get(i), thumbnails.get(i)); // Needs real data for year and link
            sResults.add(sR);
        }
    }

    //This will return a search result in sResult array at the given slot
    public SearchResult selectSearchResult(int i) {
        if (i >= 0 && i < sResults.size()) {
            return sResults.get(i);
        }
        return null;
    }

    //This will return an iterator to search results
    public Iterator<SearchResult> getSearchIterator() {
        return sResults.iterator();
    }

    //This will display all the search results currently inside sResults
    public void displaySearchResults() {
        for(SearchResult i: sResults){
            System.out.println(i.toString());
        }
    }

}