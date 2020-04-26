package party.bricket.team7.scrape;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
    public BricksetSearchScraper(String query) {
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
        url = "https://brickset.com/sets?query=" + query; // link to grab with query parameter
        System.out.println("Scraping: " + url);
        try {
            // get first 50 results by setting setsPageLength cookie to 50 when submitting GET to page
            doc = Jsoup.connect(url).cookie("setsPageLength","50").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        elItems = doc.select("article.set"); // in article tag with 'set' class
        elUrl = elItems.select("div.meta").select("h1").select("a[href]");
        elID = elItems.select("div.meta"); //
        elName = elItems.select("div.highslide-caption > h1"); //
        elYear = elItems.select("div.meta");
        int tempYear = -1;
        String thumb = null;
        for(int i = 0; i < elItems.size(); i++) { // loop through as long as we have a name
            // if year is not found, a -1 is returned in place of the year for that item.
            if(!elYear.get(i).select("a.year").text().isEmpty()) {
                tempYear = Integer.parseInt(elYear.get(i).select("a.year").text());
                years.add(tempYear);
                tempYear = -1;
            } else {
                years.add(tempYear); // set to -1
            }
            IDs.add(elID.get(i).select("div.tags").select("a[href]").get(0).text());
            names.add(elName.get(i).text());
            links.add(elUrl.get(i).attr("href"));
            thumb = elItems.get(i).select("img").attr("src");
            if(thumb.startsWith("/")) { // this is a relative path, so add brickset.com to it
                thumb = "https://brickset.com" + thumb;
            }
            thumbnails.add(thumb);
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
