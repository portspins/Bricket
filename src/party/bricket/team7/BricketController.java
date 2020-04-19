package party.bricket.team7;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class BricketController {

    private Search search;

    private Speculator spec;

    BricketController() {
        search = null;
        spec = new Speculator();
    }

    public Iterator<SearchResult> refreshSearch(String query) {
        search = new Search(query);
        return search.getSearchIterator();
    }

    public ResearchResult selectSearchResult(int index) {
        spec.addResearchResult(search.getSearchResult(index));
        System.out.println("$"+ spec.getResearchResult().getPricePerPart());
        return spec.getResearchResult();
    }

    public ResearchResult getResearchResult() {
        return spec.getResearchResult();
    }

    public boolean saveToFile(String path) {
        ResearchIO io = new ResearchIO();
        io.saveResearch(spec.getResearchResult(),path);
        return true;
    }

    public ResearchResult loadFromFile(String path) {
        ResearchIO io = new ResearchIO();
        SearchResult res = io.loadResearch(path);
        if(res == null) {
            return null;
        }
        spec.addResearchResult(res);
        return spec.getResearchResult();
    }

    public void updateReleaseDate(Calendar newDate) {
        spec.setReleaseDate(newDate);
    }

    public void updateSelected(int index) {
        spec.setSelectedResult(index);
    }
}
