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
        spec.addResearchResult(search.getSearchResult(index));
        System.out.println("$"+ spec.getResearchResult().getPricePerPart());
        return spec.getResearchResult();
    }

    public boolean saveToFile(String path) {
        ResearchIO io = new ResearchIO(path);
        io.saveResearch(spec.getResearchResult());
        return true;
    }
}
