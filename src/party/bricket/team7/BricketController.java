package party.bricket.team7;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Iterator;

public class BricketController {

    private Search search;

    private Speculator spec;

    BricketController() {
        search = null;
    }

    public Iterator<SearchResult> refreshSearch(String query) {
        search = new Search(query);
        spec = new Speculator();
        return search.getSearchIterator();
    }

    public ResearchResult selectSearchResult(int index) {
        //return spec.addResearchResult(search.getSearchResult(index));
        return null;
    }
}
