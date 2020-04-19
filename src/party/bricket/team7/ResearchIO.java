package party.bricket.team7;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    //Possibly needs replaced with JSON implementation
    ResearchIO() {
        research = null;
    }

    /** loadResearch.
     * Accepts a JSON file name string and adds the stored
     * ResearchResult into Speculator. Stores the loaded
     * file in research.
     * Check for fail to open exception.
     * Need a consistent naming scheme for files.
     */
    public SearchResult loadResearch(String path)
    {
        //store string
        //open json file with string name
        //create ResearchResult with json file data
        //ResearchResult loadHusk = new ResearchResult(); //**FIX**
        //add ResearchResult to Speculator
        //return loadHusk;
        String data = null;
        StringBuilder contentBuilder = new StringBuilder();
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
        return res;
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
        toSave.put("minifigList", s.getMinifigList()); //Needs to get entire ArrayList
        toSave.put("partCount", s.getPartCount());
        toSave.put("releaseDate", s.getReleaseDate());
        toSave.put("retireDate", s.getRetireDate());

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
