package party.bricket.team7;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.stream.Stream;

import org.json.*;


/** party.bricket.team7.ResearchIO: A class extended by Speculator that will enable
 * the saving and loading of a ResearchResult.
 * Save a ResearchResult as a file, store its data, and implement a "load" option
 * to open saved ResearchResult files.
 * Refresh method to restore the original data for a speculator
 * @author Daniel Morris
 */
public class ResearchIO {

    /** research File.
     * stores the data of the most recently opened ResearchResult file.
     * change to store the selected ResearchResult file in its tab later.
     */
    private File research;
    ResearchIO() {
        research = null;
    }

    private String getSubstringWithLength(String main, String substring, int len) {
        return main.substring(main.indexOf(substring) + substring.length(), Math.min(main.indexOf(substring) + substring.length() + len, main.length()));
    }

    private ArrayList<String> parseMinifigs(String list) {
        ArrayList<String> ret = new ArrayList<String>();
        list = list.replace("[","");
        list = list.replace("]","");
        String[] splitUp = list.split("\\s*,\\s*");
        ret.addAll(Arrays.asList(splitUp));
        return ret;
    }

    /** loadResearch.
     * Creates and builds a string of data from provided
     * path that provides the information needed to
     * produce a searchResult.
     */
    public ResearchResult loadResearch(String path)
    {

        //open json file with string name
        //create ResearchResult with json file data
        //ResearchResult loadHusk = new ResearchResult(); //**FIX**
        //add ResearchResult to Speculator
        //return loadHusk;
        // store string
        String data = null;
        StringBuilder contentBuilder = new StringBuilder();
        Calendar retireDate = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        retireDate.setTimeInMillis(0);
        Calendar releaseDate = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        releaseDate.setTimeInMillis(0);
        try {
            Stream<String> stream = Files.lines(Paths.get(path));
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
            data = contentBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(data.isEmpty()) {
            return null;
        }

        JSONObject fmt = new JSONObject(data);
        // really only think here that is important is the bricksetLink
        SearchResult res = new SearchResult(fmt.get("id").toString(),fmt.get("name").toString(),2000,fmt.get("bricksetLink").toString(),fmt.get("imageLink").toString());
        //parse information into a researchResult
        ResearchResult restored = new ResearchResult(res,fmt.get("theme").toString(),fmt.get("imageLink").toString(),fmt.getBoolean("retiredFlag"));

        String retireStr = fmt.get("retireDate").toString();
        int retireYear = Integer.parseInt(getSubstringWithLength(retireStr, ",YEAR=", 4));
        int retireMonth = Integer.parseInt(getSubstringWithLength(retireStr,",MONTH=",2).replace(",",""));
        int retireDay = Integer.parseInt(getSubstringWithLength(retireStr,"DAY_OF_MONTH=",2).replace(",",""));
        retireDate.set(retireYear,retireMonth,retireDay);

        String releaseStr = fmt.get("releaseDate").toString();
        int releaseYear = Integer.parseInt(getSubstringWithLength(releaseStr,",YEAR=",4));
        int releaseMonth = Integer.parseInt(getSubstringWithLength(releaseStr,",MONTH=",2).replace(",",""));
        int releaseDay = Integer.parseInt(getSubstringWithLength(releaseStr,"DAY_OF_MONTH=",2).replace(",",""));

        releaseDate.set(releaseYear,releaseMonth,releaseDay);

        restored.setReleaseDate(releaseDate);
        restored.setRetireDate(retireDate);
        restored.setPartCount(Integer.parseInt(fmt.get("partCount").toString()));
        restored.setValue(Double.parseDouble(fmt.get("value").toString()));
        restored.setRating(Double.parseDouble(fmt.get("rating").toString())/20.0);
        restored.setRetailPrice(Double.parseDouble(fmt.get("retailPrice").toString()));
        restored.setMinifigList((ArrayList<String>)parseMinifigs(fmt.get("minifigList").toString()).clone());
        restored.setPeakPrice(Double.parseDouble(fmt.get("peakPrice").toString()));
        //set commands to fill out ResearchResult
        return restored;
    }

    /** saveResearch.
     * Saves the researchResult provided into a JSON file.
     * Need a consistent naming scheme for files.
     */
    public boolean saveResearch(ResearchResult s,String path)
    {
        research = new File(path);
        //create JSON Object
        JSONObject toSave = new JSONObject();
        //store ResearchResult data in JSON Object
        toSave.put("id", s.getID());
        toSave.put("name", s.getName());
        toSave.put("theme", s.getTheme());
        toSave.put("bricksetLink", s.getBricksetLink());
        toSave.put("imageLink", s.getImageLink());
        toSave.put("retiredFlag", s.isRetired());

        //Editable data
        toSave.put("retailPrice", s.getRetailPrice());
        toSave.put("value", s.getValue());
        toSave.put("pricePerPart", s.getPricePerPart());
        toSave.put("rating", s.getRating());
        toSave.put("minifigList", s.getMinifigList().toString()); //Needs to get entire ArrayList
        toSave.put("partCount", s.getPartCount());
        toSave.put("releaseDate", s.getReleaseDate());
        toSave.put("retireDate", s.getRetireDate());
        toSave.put("peakPrice",s.getPeakPrice());

        //create JSON file with constant format
        try {
            FileWriter save = new FileWriter(research);
            //Write JSON Object into file
            save.write(toSave.toString());
            save.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //return boolean, true if saving was successful, false if not.
        research = null;
        return false;
    }
}
