package party.bricket.team7;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * enum for search scope types on brickset.com
 */
enum scopes { ALL, SETS, MINIFIGS, PARTS, BRICKLISTS, NEWS, MEMBERS }

/**
 * BricksetSearchScraper is an html scraper for searches on brickset.com
 * @author Dayton Hasty
 */
public class BricksetSearchScraper {

    private String url;
    private ArrayList<String> IDs;
    private ArrayList<String> names;
    private ArrayList<String> links;
    private ArrayList<String> thumbnails;
    private ArrayList<Integer> years;
    private Document doc;

    /**
     * Constructor for basic searches without a scope
     * @param query the string a user searches for. can be name, ID, etc.
     */
    BricksetSearchScraper(String query) {
        names = new ArrayList<String>();
        IDs = new ArrayList<String>();
        links = new ArrayList<String>();
        thumbnails = new ArrayList<String>();
        years = new ArrayList<Integer>();

        Elements elID;
        Elements elName;
        Elements elUrl;
        Elements elItems;
        Elements elYear;
        // build URL
        url = "https://brickset.com/sets?query=" + query;
        System.out.println("Scraping: " + url);
        try {
            doc = Jsoup.connect(url).cookie("setsPageLength","500").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        elItems = doc.select("article.set");
        elUrl = elItems.select("div.meta").select("h1").select("a[href]");
        elID = elItems.select("div.meta");
        elName = elItems.select("div.highslide-caption > h1");
        elYear = elItems.select("div.meta");
        Integer tempYear = -1;
        for(int i = 0; i < elItems.size(); i++) { // loop through as long as we have a name
            // if year is not found, a -1 is returned in place of the year for that item.
            if(!elYear.get(i).select("a.year").text().isEmpty()) {
                tempYear = Integer.valueOf(elYear.get(i).select("a.year").text());
                years.add(tempYear);
                tempYear = -1;
            } else {
                years.add(tempYear); // set to -1
            }
            IDs.add(elID.get(i).select("div.tags").select("a[href]").get(0).text());
            names.add(elName.get(i).text());
            links.add(elUrl.get(i).attr("href"));
            thumbnails.add(elItems.get(i).select("img").attr("src"));
            System.out.println("Name:      "+ names.get(i));
            System.out.println("ID:        " + IDs.get(i));
            System.out.println("Link:      " + links.get(i));
            System.out.println("Thumbnail: " + thumbnails.get(i));
            System.out.println("Year:      " + years.get(i));
            System.out.println("");
        }
        System.out.println("===============\nGot " + years.size() + " items\n================");
    }

    /**
     * get for item IDs from search
     * @return ArrayList of item IDs as strings
     */
    public ArrayList<String> getIDs() {
        return IDs;
    }

    /**
     * get for item names from search
     * @return ArrayList of item names as strings
     */
    public ArrayList<String> getNames() {
        return names;
    }

    /**
     * get for links to item pages from search
     * @return ArrayList of relative path links as strings
     */
    public ArrayList<String> getLinks() {
        return links;
    }

    /**
     * get for thumbnail image links for items from search
     * @return ArrayList of absolute path image links as strings
     */
    public ArrayList<String> getThumbnails() {
        return thumbnails;
    }

    /**
     * get for release years of items from search
     * @return ArrayList of years as Integers
     */
    public ArrayList<Integer> getYears() {
        return years;
    }

}
