package party.bricket.team7;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class BricketController {

    private Search search;

    final private Speculator spec;

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
        System.out.println("Peak: $" + spec.calcValue());
        return spec.getResearchResult();
    }

    public ResearchResult getResearchResult() {
        return spec.getResearchResult();
    }

    public void removeResearchResult() {
        spec.removeResearchResult();
    }

    public boolean saveToFile(String path) {
        ResearchIO io = new ResearchIO();
        io.saveResearch(spec.getResearchResult(),path);
        return true;
    }

    public ResearchResult loadFromFile(String path) {
        ResearchIO io = new ResearchIO();
        ResearchResult res = io.loadResearch(path);
        if(res == null) {
            return null;
        }
        // will make io.loadResearch return ResearchResult tomorrow, just did less involved SearchResult to test
        spec.addResearchResult(res);
        return spec.getResearchResult();
    }

    public void updateReleaseDate(Calendar newDate) {
        spec.setReleaseDate(newDate);
    }

    public void updateRetireDate(Calendar newDate) {
        spec.setRetireDate(newDate);
    }

    public void updatePartCount(int count) {
        spec.setPartCount(count);
    }

    public void updatePricePerPart(double ppp) {
        spec.setPricePerPart(ppp);
    }

    public void updatePrice(double price) {
        spec.setRetailPrice(price);
    }

    public void updateRating(int rating) {
        spec.setRating(rating);
    }

    public void updateSelected(int index) {
        spec.setSelectedResult(index);
    }

    public boolean resetToOG() {
        return spec.resetResearchResult();
    }
}
