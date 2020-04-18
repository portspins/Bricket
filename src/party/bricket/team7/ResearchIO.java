package party.bricket.team7;

import java.io.*;
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
    private File research = new File("DefaultFile.json");
    //Possibly needs replaced with JSON implementation

    /** loadResearch.
     * Accepts a JSON file name string and adds the stored
     * ResearchResult into Speculator. Stores the loaded
     * file in research.
     * Check for fail to open exception.
     * Need a consistent naming scheme for files.
     */
    public ResearchResult loadResearch(String id)
    {
        //store string
        //open json file with string name
        //create ResearchResult with json file data
        //ResearchResult loadHusk=new ResearchResult(); //**FIX**
        //add ResearchResult to Speculator
        //return loadHusk;
        return null;
    }

    /** saveResearch.
     * Saves the researchResult provided into a JSON file.
     * Need a consistent naming scheme for files.
     */
    public boolean saveResearch(ResearchResult s)
    {
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
        return false;
    }
}
