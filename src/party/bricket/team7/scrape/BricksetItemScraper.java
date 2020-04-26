package party.bricket.team7.scrape;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import party.bricket.team7.data.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * BricksetItemScraper is an html scraper for values on individual item pages on brickset.cocm
 * @author Dayton Hasty
 */
public class BricksetItemScraper {

    private final String url;
    private Document doc; // store doc in memory so we don't have to pull new data each time

    /**
     * Constructor for scraping an individual item page
     * @param result the desired item's SearchResult
     */
    public BricksetItemScraper(SearchResult result) {
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
        Elements elFeatureBoxes = doc.select("section.featurebox"); // narrow down to featurebox classes
        String subtheme = elFeatureBoxes.get(0).select("dt:contains(Subtheme) + dd").html(); // attempt to get subtheme
        String theme = elFeatureBoxes.get(0).select("dt:contains(Theme) + dd").select("a[href]").get(0).text(); // get theme
        if(!subtheme.isEmpty()) { // sometimes there is not a subtheme, if there isn't we want to skip the combination here
            subtheme = elFeatureBoxes.get(0).select("dt:contains(Subtheme) + dd").select("a[href]").get(0).text();
            theme += " (" + subtheme + ")";
        }
        return theme;
    }

    /**
     * scrapes if the item is retired or not based on if it is still being sold
     * @return boolean true if it is retired and false if it is still being sold
     */
    public boolean scrapeIsRetired() {
        Elements elFeatureBoxes = doc.select("section.featurebox"); // narrow down to featurebox classes
        Element availability = elFeatureBoxes.get(2); // availability is in the 3rd featurebox
        String date = availability.select("dt:contains(United States) + dd").text(); // get US availability
        if (date.isEmpty() || date.equals("-")) { // if there is no availability for the US or there is but it is '-', assume retired
            return true;
        }
        // get rid of price
        int lastSpace = date.lastIndexOf(" "); // get last space
        int hyphen = date.lastIndexOf("-");    // and hyphen
        date = date.substring(hyphen + 2, lastSpace); // now use space/hyphen locations to get date string
        if (date.equals("now")) { // still available, not retired
            return false;
        }
        return true;
    }

    /**
     * scrapes the display image link from item page
     * @return string of URL to image
     */
    public String scrapeImgLink() {
        Elements contentClass = doc.select("div.content"); // get initial content section that
        String src = contentClass.select("img").attr("src"); // get src attribute from <img> tag
        if(src.startsWith("/")) { // this is a relative path, so add brickset.com to it
            src = "https://brickset.com" + src;
        }
        return src;
    }

    /**
     * scrapes names of included minifigs, if there are any
     * @return ArrayList of minifig names from external page
     */
    public ArrayList<String> scrapeMinifigNames() {
        ArrayList<String> figs = new ArrayList<String>();
        Document figDoc = null; // need a new Document because we are going to have to jump to a different page
        Elements featureBoxes = doc.select("section.featurebox");
        Elements figMan = featureBoxes.get(0).select("dt:contains(Minifigs) + dd");
        if(figMan.isEmpty()) { // if no minifigs listed, then no need to continue. return empty list
            return figs;
        }
        String figLink = figMan.select("a[href]").attr("href"); // get the external link to the minifig list
        figLink = "https://brickset.com" + figLink; // its a relative link, so need to add brickset.com
        try {
            figDoc = Jsoup.connect(figLink).get();
        } catch (IOException e) {
            e.printStackTrace();
            return figs;
        }
        Elements figItems = figDoc.select("li.item"); // each one is in a list with the class 'item'
        String figName;
        for(int i = 0; i < figItems.size(); i++) { // iterate through list of items
            // get name and remove unnecessary data
            figName = figItems.get(i).select("h1 > a[href]").text().replaceAll("\\(.*\\)", "").trim();
            String[] figNames = figName.split(" - "); //
            String[] shortFigNames = figNames[0].split(",");
            figs.add(shortFigNames[0]);
        }
        return figs;
    }

    /**
     * scrapes the number of pieces in a set
     * @return integer containing part count in set
     */
    public int scrapePartCount() {
        Elements elFeatureBoxes = doc.select("section.featurebox"); // need Pieces data from first featurebox
        Elements elPieces = elFeatureBoxes.get(0).select("dt:contains(Pieces) + dd");
        int count = 0;
        String strCount = elPieces.select("a[href]").text(); // part count is usually a link
        if(strCount.isEmpty()) {
            strCount = elPieces.text(); // but sometimes it is just normal unlinked text
            if(strCount.isEmpty()) { // if we can't even get that, we still need to return a value
                strCount = "-1";
            }
        }
        count = Integer.parseInt(strCount); // str to int
        return count;
    }

    /**
     * scrapes the rating from users on the website
     * @return double with current user rating of item
     */
    public double scrapeRating() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        String strRating = elFeatureBoxes.get(0).select("div.rating").attr("title"); // rating in title attribute
        if(strRating.isEmpty()) {
            return 0;
        }
        return Double.parseDouble(strRating); // str to double
    }

    /**
     * scrapes the current value (new, if no new then used) in dollars
     * @return double of current US dollar price of item
     */
    public double scrapeCurrentValue() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Elements elCurrentValues = elFeatureBoxes.get(0).select("dt:contains(Current Value) + dd");
        elCurrentValues = elCurrentValues.select("a.plain[href]");
        if(elCurrentValues.isEmpty()) {
            return -1.0; // still need to return a value
        }
        String strCurrentValue = elCurrentValues.get(0).text();
        strCurrentValue = strCurrentValue.substring(strCurrentValue.lastIndexOf("$")+1); // get rid of dollar sign
        return Double.parseDouble(strCurrentValue); // str to double
    }

    /**
     * scrapes the retail price of the item in US dollars
     * @return double price of item
     */
    public double scrapeRetailPrice() {
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Elements elRRP = elFeatureBoxes.get(0).select("dt:contains(RRP) + dd");
        if(elRRP.isEmpty()) { // sometimes there is not an RRP, so still ret a value
            return -1.0;
        }
        String strRRP = elRRP.text();
        if(!strRRP.contains("$")) { // if there isn't a $ value, it is empty or exclusively euros
            return -1.0;
        }
        if(strRRP.contains("£") && strRRP.contains("$") && strRRP.contains("€")) // sometimes all three currencies are listed
            strRRP = strRRP.substring(strRRP.lastIndexOf("$")+1,strRRP.lastIndexOf("/")-1); // get dollar value in list of currencies
        else
            strRRP = strRRP.substring(strRRP.lastIndexOf("$")+1); // remove dollar value
        return Double.parseDouble(strRRP); // str to double
    }

    /**
     * private method to convert Brickset date format into Calendar date
     * @param date Brickset date formatted string
     * @return Calendar item
     */
    private Calendar makeCalendarFromDateString(String date) {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago")); // local time
        cal.setTimeInMillis(0); // set to epoch as default "bad" value
        // date is in format DAY MONTH YEAR
        String strDay = date.substring(0,date.indexOf(" "));
        String strMon = date.substring(date.indexOf(" ")+1,date.lastIndexOf(" "));
        String strYear = date.substring(date.lastIndexOf(" ") + 1);
        int month = -1;
        int year = -1;
        if(!strYear.matches("19\\d\\d")) { // year is not in 19xx format, should be for 2000s
            year = Integer.parseInt("20" + strYear); // year is
        } else {
            year = Integer.parseInt(strYear); // year is in 19xx format
        }
        int day = Integer.parseInt(strDay);
        switch(strMon) { // forgive me, this is the best way I could think of to get the month value from a string
            case "Jan":
                month = Calendar.JANUARY;
                break;
            case "Feb":
                month = Calendar.FEBRUARY;
                break;
            case "Mar":
                month = Calendar.MARCH;
                break;
            case "Apr":
                month = Calendar.APRIL;
                break;
            case "May":
                month = Calendar.MAY;
                break;
            case "Jun":
                month = Calendar.JUNE;
                break;
            case "Jul":
                month = Calendar.JULY;
                break;
            case "Aug":
                month = Calendar.AUGUST;
                break;
            case "Sep":
                month = Calendar.SEPTEMBER;
                break;
            case "Oct":
                month = Calendar.OCTOBER;
                break;
            case "Nov":
                month = Calendar.NOVEMBER;
                break;
            case "Dec":
                month = Calendar.DECEMBER;
                break;
            default:
                return cal;
        }
        cal.set(year,month,day); // set calendar date to parsed values
        return cal;
    }

    /**
     * scrapes the date retired, if it is retired
     * @return Calendar item containing retire date
     */
    public Calendar scrapeRetiredDate() {
        Calendar retired = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago")); // local HSV time
        retired.setTimeInMillis(0); // set to epoch for default "bad" value
        if(!scrapeIsRetired()) { // not retired, so no use in trying to do all of this
            return retired;
        }
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Element availability = elFeatureBoxes.get(2); // retire date is in 3rd featurebox
        String date = availability.select("dt:contains(United States) + dd").text();
        if (date.isEmpty() || date.equals("-")) { // if bad date, return epoch
            return retired;
        }
        // get rid of price
        int lastSpace = date.lastIndexOf(" ");
        int hyphen = date.lastIndexOf("-");
        date = date.substring(hyphen + 2, lastSpace); // date between hyphen and last space
        if(date.isEmpty()) { // return epoch if no retire date
            return retired;
        }
        retired = makeCalendarFromDateString(date); // convert Brickset date string to Calendar date
        return retired;
    }

    /**
     * scrapes release date of item
     * @return Calendar item containing release date
     */
    public Calendar scrapeReleaseDate() {
        // this one is similar to scrapeRetireDate since the HTML is similar
        Calendar release = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        release.setTimeInMillis(0); // epoch for "bad" value
        Elements elFeatureBoxes = doc.select("section.featurebox");
        Element availability = elFeatureBoxes.get(2); // in 3rd featurebox
        String date = availability.select("dt:contains(United States) + dd").text();
        if (date.isEmpty() || date.equals("-")) {
            return release;
        }
        int hyphen = date.lastIndexOf("-");
        date = date.substring(0,hyphen-1);
        if(date.isEmpty()) { // make sure date is valid
            return release;
        }
        release = makeCalendarFromDateString(date); // convert Brickset date string to Calendar
        return release;
    }
}
