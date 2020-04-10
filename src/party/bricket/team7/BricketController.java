package party.bricket.team7;

import javax.swing.*;
import java.util.ArrayList;

public class BricketController {
    BricketController() {

    }

    public void searchSubmitted(String query) {
        BricksetScraper searchScraper = new BricksetScraper(query);
        ArrayList<String> IDs = searchScraper.getIDs();
        for(String id : IDs) {
            JLabel searchItem = new JLabel(id);
        }
    }
}
