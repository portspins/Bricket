package party.bricket.team7;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.io.IOException;

/**
 * BricksetItemScraper is an html scraper for values on individual item pages on brickset.cocm
 * @author Dayton Hasty
 */
public class BricksetItemScraper {

    private String url;
    private Document doc;

    /**
     * Constructor for scraping an individual item page
     * @param result the desired item's SearchResult
     */
    BricksetItemScraper(SearchResult result) {
        url = "https://brickset.com" + result.getItemLink();
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
        // will be in first <section class='featurebox'>.....
        Elements elFeatureboxes = doc.select("section.featurebox");
        return elFeatureboxes.get(0).select("dt:contains(Set Type) + dd").html();
    }

    /**
     * scrapes theme in format "theme: subtheme" from right-hand side of item page
     * @return theme of item
     */
    public String scrapeTheme() {
        Elements elFeatureboxes = doc.select("section.featurebox");
        String subtheme = elFeatureboxes.get(0).select("dt:contains(Subtheme) + dd").html();
        String theme = elFeatureboxes.get(0).select("dt:contains(Theme) + dd").select("a[href]").get(0).text();
        if(!subtheme.isEmpty()) {
            subtheme = elFeatureboxes.get(0).select("dt:contains(Subtheme) + dd").select("a[href]").get(0).text();
            theme += ": " + subtheme;
        }
        return theme;
    }

    /**
     * scrapes if the item is retired or not based on if it is still being sold
     * @return boolean true if it is retired and false if it is still being sold
     */
    public boolean scrapeIsRetired() {
        Elements elFeatureboxes = doc.select("section.featurebox");
        Element availability = elFeatureboxes.get(2);
        String date = availability.select("dt:contains(United States) + dd").text();
        if (date.isEmpty()) {
            return true;
        }
        // get rid of price
        int lastSpace = date.lastIndexOf(" ");
        int hyphen = date.lastIndexOf("-");
        date = date.substring(hyphen + 2, lastSpace);
        if (date.equals("now")) {
            return false;
        }
        return true;
    }

    /**
     * scrapes the display image link from item page
     * @return returns string of URL to image
     */
    public String scrapeImgLink() {
        Elements contentClass = doc.select("div.content");
        String src = contentClass.select("img").attr("src");
        return src;
    }
}
