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
    private ArrayList<String> IDs;
    private ArrayList<String> names;
    private ArrayList<String> links;
    private Document doc;

    /**
     * Constructor if a user wants to search within a specific scope
     * @param query the keyword a user wants to search for
     * @param scope the type of sets a user wants to search within
     */
    BricksetScraper(String query, scopes scope) {
        // build URL
        names = new ArrayList<String>();
        IDs = new ArrayList<String>();
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
        names = new ArrayList<String>();
        IDs = new ArrayList<String>();
        links = new ArrayList<String>();
        Elements elID;
        Elements elName;
        Elements elUrl;
        Elements elCategories;
        // build URL
        url = "https://brickset.com/search?query=" + query + "&scope=All";
        System.out.println("Scraping: " + url);
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println(doc.html());
        elCategories = doc.select("div.col"); // narrow down to divs w/ col
        elCategories = elCategories.select("div.span3"); // narrow down to divs w/ col && span3
        elName = elCategories.select("h1"); // names will always be in h1 tag
        elID = elCategories.select("li.set"); // unordered list item per result, with class 'set'
        elUrl = elCategories.select("div.tags");
        elUrl = elUrl.select("div.floatleft");
        //System.out.println("Found " + categories.size() + " divs");
        //System.out.println("Found " + names.size() + " items");
        //System.out.println("Found " + ids.size() + " ahrefs");
        String tempID;
        for(int i = 0; i < elName.size(); i++) { // loop through as long as we have a name
            tempID = elID.get(i).select("a[href]").attr("href"); // get link in href
            tempID = tempID.substring(tempID.lastIndexOf('/')+1);
            if(tempID.contains(".")) {
                tempID = tempID.substring(0, tempID.indexOf('.'));
            }
            names.add(elName.get(i).text());
            links.add(elUrl.get(i).select("a[href]").attr("href"));
            System.out.println("Name: "+ names.get(i));
            System.out.println("ID: " + tempID);
            System.out.println("Link: " + links.get(i));
            IDs.add(tempID);
        }
    }

    /**
     * Constructor for scraping an individual item page
     * @param result the desired item's SearchResult
     */
    BricksetScraper(SearchResult result) {
        url = "https://brickset.com" + result.getBSLink();
        try {
            doc = Jsoup.connect(url).get(); // store html in memory for speed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * scrapes item in right-hand side of item page "Set Type"
     * @return set type (Normal, Minifig, etc)
     */
    public String scrapeSetType() {
        // this is WIP... but then again isn't this whole project
        // will be in first <section class='featurebox'>.....
        Elements elFeatureboxes = doc.select("class.featurebox");
        String type = elFeatureboxes.get(0).select("dt.Set Type > dd").text();
        // the above code could very well not work, I haven't tried the > descriptor yet
        System.out.println("Set Type: " + type);
        return type;
    }

    public ArrayList<String> getIDs() {
        return IDs;
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

}
