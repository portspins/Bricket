package party.bricket.team7;

import java.util.*;
import java.util.Scanner;

public class Speculator {
    private BricksetItemScraper bSScraper;
    //can't set size limit on array lists. Will either need to some up with our own mechanism to limit it or use a regular array
    //to store the researchResults
    private LinkedList<ResearchResult> specs = new LinkedList<ResearchResult>();

    public void Speculator(BricksetItemScraper bSS){
        /**why did we decide to import a scraper instead of creating it within the class??*/
        bSScraper = bSS;//doesn't work. need to clone

    }

    /**Create a new researchResult using an instantiation of the Item Scraper and the selected searchResult
     * and add it to the array list of research results
     */
    private void addResearchResult(SearchResult res) {
        //Since we only want them to have 5 sessions, we need to add a flag to limit the user. DONT NEED LIMIT ANYMORE
        if(specs.size() < 5) {
            bSScraper = new BricksetItemScraper(res);//How to merge current version with version on computer (personal question)
            ResearchResult rResult = new ResearchResult(
                    res,
                    bSScraper.scrapeTheme(),
                    bSScraper.scrapeImgLink(), //Need to add method to BricksetItemScraper to get the image link
                    bSScraper.scrapeIsRetired()
            );

            specs.add(rResult);
        }
        else{
            System.out.println("Already have 5 Research Sessions open. Would you like to delete a session and store this one?");
            Scanner input = new Scanner(System.in);
            String Answer="";// I think we can turn this into a button in the controller which would make the loop below useless
            while(!Answer.equals("yes") || !Answer.equals("no")) {
                Answer = input.nextLine();
                Answer = Answer.toLowerCase();
                if (Answer == "yes") {
                    //prompt user to indicate which session he wants to replace
                }
                else if (Answer == "no") {
                    // exits save window and returns to speculator session
                }
                else {
                    System.out.println("Please answer yes or no");
                }
            }
        }
    }

    /**Method uses equals method in ResearchResult to find item to remove
     *
     * @param RR_removal
     * @return
     */
    private boolean removeResearchResult(ResearchResult RR_removal){
       if(specs.isEmpty()){
           System.out.println("There are currently no opened sessions");
           return false;
       }
       //might want to check this to make sure its not redundant
       //The way I (Andrew) saw it was if we don't pass the object to remove by direct reference then we will need to identify it by value
       else {
           for(ResearchResult r: specs){
               if(r.equals(RR_removal)){
                   specs.remove(r);
                   return true;
               }
               else{
                   continue;
               }
           }
       }
       return false; // will occur if it was not found
    }

    /**Replaces a ResearchResult within the list by passing in a ResearchResult to replace a ResearchResult currently in the list
     *
     * @param RR_toReplace
     * @param RR_replacement
     * @return
     */
    private boolean replaceResearchResult(ResearchResult RR_toReplace, ResearchResult RR_replacement){
        if(specs.isEmpty()){
            System.out.println("There are currently no opened sessions");
            return false;
        }
        else{
            for(ResearchResult r: specs){
                if(r.equals(RR_toReplace)){
                    specs.set(specs.indexOf(r), RR_replacement);
                    return true;
                }
                else{
                    continue;
                }
            }
        }
        return false;
    }

    //need to add methods to ResearchResult to save the original values
    private void modifyResearchResult(ResearchResult RR_mod, Double testRetail, Double testPrice, Double testPartPrice, Date testReleaseDate, Date testRetiredDate){
        /**Search the list for the set based on the set name
         * Not entirely sure how else we wanted to format this part
         */
        /**Price calculations
         * Need original price, retired date, piece count, maybe theme
         */
    }

    /**Refreshes ResearchResult to its original state
     */
    public void resetResearchResult(){

    }
    /**load a ResearchResult from ResearchIO*/
    public void loadFromFile(){

    }


}
