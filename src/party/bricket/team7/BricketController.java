package party.bricket.team7;

import javax.swing.*;
import java.util.ArrayList;

public class BricketController {
    BricketController() {

    }

    public void searchSubmitted(String query) {
        BricksetSearchScraper searchScraper = new BricksetSearchScraper(query);
        // did some testing of BricksetItemScraper here. will change
        // from searchresult to researchresult as a parameter later
        //
        //SearchResult res = new SearchResult();
        //res.setBSLink(searchScraper.getLinks().get(0));
        //BricksetItemScraper individual = new BricksetItemScraper(res);
        //String setType = individual.scrapeSetType();
        //String theme = individual.scrapeTheme();
        //individual.scrapeIsRetired();
        ArrayList<String> IDs = searchScraper.getIDs();
        for(String id : IDs) {
            JLabel searchItem = new JLabel(id);
        }
    }
}
