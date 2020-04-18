package party.bricket.team7;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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
        Elements elFeatureBoxes = doc.select("section.featurebox");
        return elFeatureBoxes.get(0).select("dt:contains(Set Type) + dd").html();
    }

    /**
     * scrapes theme in format "theme: subtheme" from right-hand side of item page
     * @return theme of item
     */
    public String scrapeTheme() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        String subtheme = elFeatureBoxes.get(0).select("dt:contains(Subtheme) + dd").html();
        String theme = elFeatureBoxes.get(0).select("dt:contains(Theme) + dd").select("a[href]").get(0).text();
        if(!subtheme.isEmpty()) {
            subtheme = elFeatureBoxes.get(0).select("dt:contains(Subtheme) + dd").select("a[href]").get(0).text();
            theme += ": " + subtheme;
        }
        return theme;
    }

    /**
     * scrapes if the item is retired or not based on if it is still being sold
     * @return boolean true if it is retired and false if it is still being sold
     */
    public boolean scrapeIsRetired() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Element availability = elFeatureBoxes.get(2);
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

    public ArrayList<String> scrapeMinifigNames() {
        ArrayList<String> figs = new ArrayList<String>();
        Document figDoc = null;
        Elements featureBoxes = doc.select("section.featurebox");
        Elements figMan = featureBoxes.get(0).select("dt:contains(Minifigs) + dd");
        if(figMan.isEmpty()) {
            return figs;
        }
        String figLink = figMan.select("a[href]").attr("href");
        figLink = "https://brickset.com" + figLink;
        try {
            figDoc = Jsoup.connect(figLink).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements figItems = figDoc.select("li.item");
        for(int i = 0; i < figItems.size(); i++) {
            figs.add(figItems.select("h1 > a[href]").text());
        }
        return figs;
    }

    public int scrapePartCount() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Elements elPieces = elFeatureBoxes.get(0).select("dt:contains(Pieces) + dd");
        Integer count = 0;
        String strCount = elPieces.select("a[href]").text();
        if(strCount.isEmpty()) {
            strCount = elPieces.text();
        }
        count = Integer.parseInt(strCount);
        return count;
    }

    public double scrapeRating() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        String strRating = elFeatureBoxes.get(0).select("div.rating").attr("title");
        if(strRating.isEmpty()) {
            return -1.0;
        }
        return Double.parseDouble(strRating);
    }

    public double scrapeCurrentValue() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Elements elCurrentValues = elFeatureBoxes.get(0).select("dt:contains(Current Value) + dd");
        elCurrentValues = elCurrentValues.select("a.plain[href]");
        if(elCurrentValues.isEmpty()) {
            return -1.0;
        }
        String strCurrentValue = elCurrentValues.get(0).text();
        strCurrentValue = strCurrentValue.substring(strCurrentValue.lastIndexOf("$")+1);
        return Double.parseDouble(strCurrentValue);
    }

    public double scrapeRetailPrice() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Elements elRRP = elFeatureBoxes.get(0).select("dt:contains(RRP) + dd");
        if(elRRP.isEmpty()) {
            return -1.0;
        }
        String strRRP = elRRP.text();
        strRRP = strRRP.substring(strRRP.lastIndexOf("$")+1);
        return Double.parseDouble(strRRP);
    }

    private Calendar makeCalendarFromDateString(String date) {
        Calendar retired = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        retired.setTimeInMillis(0);
        String strDay = date.substring(0,date.indexOf(" "));
        String strMon = date.substring(date.indexOf(" ")+1,date.lastIndexOf(" "));
        String strYear = date.substring(date.lastIndexOf(" ") + 1);
        int month = -1;
        int year = Integer.parseInt("20" + strYear);
        int day = Integer.parseInt(strDay);
        switch(strMon) { // forgive me
            case "Jan":
                month = 0;
                break;
            case "Feb":
                month = 1;
                break;
            case "Mar":
                month = 2;
                break;
            case "Apr":
                month = 3;
                break;
            case "May":
                month = 4;
                break;
            case "Jun":
                month = 5;
                break;
            case "Jul":
                month = 6;
                break;
            case "Aug":
                month = 7;
                break;
            case "Sep":
                month = 8;
                break;
            case "Oct":
                month = 9;
                break;
            case "Nov":
                month = 10;
                break;
            case "Dec":
                month = 11;
                break;
            default:
                return retired;
        }
        retired.set(year,month,day);
        return retired;
    }

    public Calendar scrapeRetiredDate() {
        Calendar retired = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        retired.setTimeInMillis(0);
        System.out.println(retired.toString());
        if(!scrapeIsRetired()) { // not retired, so no use in trying to do all of this
            return retired;
        }
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Element availability = elFeatureBoxes.get(2);
        String date = availability.select("dt:contains(United States) + dd").text();
        if (date.isEmpty()) {
            return retired;
        }
        // get rid of price
        int lastSpace = date.lastIndexOf(" ");
        int hyphen = date.lastIndexOf("-");
        date = date.substring(hyphen + 2, lastSpace);
        if(date.isEmpty()) {
            return retired;
        }
        retired = makeCalendarFromDateString(date);
        return retired;
    }
}
