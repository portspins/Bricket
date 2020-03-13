package party.bricket.team7;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

enum scopes { ALL, SETS, MINIFIGS, PARTS, BRICKLISTS, NEWS, MEMBERS }

public class BricksetScraper {

    private String url;
    private ArrayList<String> ID;
    private ArrayList<String> name;
    private Document doc;
    private Elements ElID;
    private Elements ElName;


    /**
     * Constructor if a user wants to search within a specific scope
     * @param query the keyword a user wants to search for
     * @param scope the type of sets a user wants to search within
     */
    BricksetScraper(String query, scopes scope) {
        // build URL
        url = "https://brickset.com/search?query=" + query + "&scope=";
        switch(scope) {
            case ALL:
                url += "All";
                break;
            case SETS:
                url += "Sets";
                break;
            case MINIFIGS:
                url += "Minifigs";
                break;
            case PARTS:
                url += "Parts";
                break;
            case BRICKLISTS:         // don't think we actually need cases from here down,
                url += "BrickLists"; // but they are options on the site so I will include them
                break;
            case NEWS:
                url += "News";
                break;
            case MEMBERS:
                url += "Members";
                break;
            default:           // default scope will be all
                url += "All";
                break;
        }
        if(scope == scopes.ALL) {
            try {
                doc = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements categories = doc.select(".col span3");

        }
    }

    BricksetScraper(String query) {
        // build URL
        url = "https://brickset.com/search?query=" + query + "&scope=All";
    }

    public ArrayList<String> getIDs() {
        return ID;
    }

}
