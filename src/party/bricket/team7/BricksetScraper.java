package party.bricket.team7;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
        name = new ArrayList<String>();
        ID = new ArrayList<String>();
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
            System.out.println("URL: " + url);
        }
    }

    BricksetScraper(String query) {
        name = new ArrayList<String>();
        ID = new ArrayList<String>();
        // build URL
        url = "https://brickset.com/search?query=" + query + "&scope=All";
        System.out.println("Scraping: " + url);
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(doc.html());
        Elements categories = doc.select("div.col"); // narrow down to divs w/ col
        categories = categories.select("div.span3"); // narrow down to divs w/ col && span3
        Elements names = categories.select("h1"); // names will always be in h1 tag
        Elements ids = categories.select("li.set"); // unordered list item per result, with class 'set'
        //System.out.println("Found " + categories.size() + " divs");
        //System.out.println("Found " + names.size() + " items");
        //System.out.println("Found " + ids.size() + " ahrefs");
        String tempID;
        for(int i = 0; i < names.size(); i++) { // loop through as long as we have a name
            tempID = ids.get(i).select("a[href]").attr("href"); // get link in href
            tempID = tempID.substring(tempID.lastIndexOf('/')+1);
            if(tempID.contains(".")) {
                tempID = tempID.substring(0,tempID.indexOf('.'));
            }
            name.add(names.get(i).text());
            System.out.println("Name: "+ name.get(i));
            System.out.println("ID: " + tempID);
            ID.add(tempID);
        }
    }

    public ArrayList<String> getIDs() {
        return ID;
    }

    public ArrayList<String> getNames() {
        return name;
    }

}
