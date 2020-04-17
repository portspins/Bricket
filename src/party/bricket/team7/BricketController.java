package party.bricket.team7;

import javax.swing.*;
import java.util.ArrayList;

public class BricketController {

    private Search search;

    BricketController() {
        search = null;
    }

    public void searchSubmitted(String query) {
        // BricksetSearchScraper searchScraper = new BricksetSearchScraper(query);
        // did some testing of BricksetItemScraper here. will change
        // from searchresult to researchresult as a parameter later
        //
        //SearchResult res = new SearchResult("7235-1","Police Motorcycle",2005,"/sets/7235-1/Police-Motorcycle","https://images.brickset.com/sets/small/7235-1.jpg?200411060233");
        //BricksetItemScraper individual = new BricksetItemScraper(res);
        //String setType = individual.scrapeSetType();
        //String theme = individual.scrapeTheme();
        //individual.scrapeImgLink();
        //individual.scrapeIsRetired();
        search = new Search(query);
        search.displaySearchResults();
    }
}
