package party.bricket.team7;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class BricketController {

    private Search search;

    BricketController() {
        search = null;
    }

    public Iterator<SearchResult> refreshSearch(String query) {
        search = new Search(query);
        return search.getSearchIterator();
    }

}
