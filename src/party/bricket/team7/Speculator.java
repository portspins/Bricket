package party.bricket.team7;

import java.util.*;
import java.util.Scanner;

public class Speculator {
    private Boolean isSaved;
    private BricksetItemScraper bSScraper;
    private LinkedList<ResearchResult> specs = new LinkedList<ResearchResult>();
    private ResearchResult selectedResult;

    public Speculator(){

    }

    // add a new ResearchResult to the list by constructing it from the scrapper and a passed in SearchResult
    /**Removed the 5 tab limit. Is that ok?*/
    public void addResearchResult(SearchResult res) {
        ResearchResult rResult = makeResearchResult(res);
        specs.add(rResult);
        selectedResult = rResult;
    }

    /**
     * make new ResearchResult from SearchResult
     * @param res
     * @return
     */
    private ResearchResult makeResearchResult(SearchResult res) {
        bSScraper = new BricksetItemScraper(res);
        ResearchResult result = new ResearchResult(
                res,
                bSScraper.scrapeTheme(),
                bSScraper.scrapeImgLink(),
                bSScraper.scrapeIsRetired()
        );
        result.setPartCount(bSScraper.scrapePartCount());
        result.setRating(bSScraper.scrapeRating());
        result.setReleaseDate(bSScraper.scrapeReleaseDate());
        result.setRetailPrice(bSScraper.scrapeRetailPrice());
        result.setRetireDate(bSScraper.scrapeRetiredDate());
        result.setValue(bSScraper.scrapeCurrentValue());
        result.setMinifigList(bSScraper.scrapeMinifigNames());

        return result;
    }

    //add a ResearchResult by passing it in
    public void addResearchResult(ResearchResult res){
        specs.add(res);
    }

    //Remove a ResearchResult.
    /**Conflict
     * UML has this listed as not taking any parameters and passing back a ResearchResult
     * Do we pass back the ResearchResult we took out?
     * How do you find the item to remove if you don't pass one in?
     * @param RR_removal
     * @return
     */
    private ResearchResult removeResearchResult(ResearchResult RR_removal){
       if(specs.isEmpty()){
           System.out.println("There are currently no opened sessions");
           return null;
       }
       else {
           for(ResearchResult r: specs){
               if(r.equals(RR_removal)){
                   specs.remove(r);
                   return r;
               }
               else{
                   continue;
               }
           }
       }
       return null; // will occur if item was not found
    }

    //Resets the current ResearchResult to it's original state
    /**Question
     * How are we storing the original values to revert back to?
     * @return
     */
    public ResearchResult resetResearchResult() {
        return null;
    }

    /**Question
     * Does this just check if the list is empty?
     * @return
     */
    public Boolean hasResults(){
        if(specs.isEmpty()){
            return false; //specs has no results
        } else {
            return true; //specs has results
        }
    }

    //replace a ResearchResult by passing in a SearchResult and constructing the item to be added

    /**
     * Replaces selectedResult in list as well as the variable with the ResearchResult form of SearchResult res
     * @param res
     * @return
     */
    public ResearchResult replaceResearchResult(SearchResult res){
        if(specs.isEmpty()){
            System.out.println("There are currently no opened sessions");
            return null;
        }
        ResearchResult temp = makeResearchResult(res);
        specs.set(specs.indexOf(selectedResult),temp);
        selectedResult = temp;
        return selectedResult;
    }

   //Replace a ResearchResult in the list by passing in a ResearchResult
    /**Conflict
     * UML only has this taking one ResearchResult. We need two.
     * We need one to replace and a replacement.
     * Whiched one is passed in? Is selected Session the one replacing?
     * @param RR_toReplace
     * @param RR_replacement
     * @return
     */
    private ResearchResult replaceResearchResult(ResearchResult RR_toReplace, ResearchResult RR_replacement){
        /**Is this check necessary or are we using the hasResults method instead?*/
        if(specs.isEmpty()){
            System.out.println("There are currently no opened sessions");
            return null;
        }
        else{
            for(ResearchResult r: specs){
                if(r.equals(RR_toReplace)){
                    specs.set(specs.indexOf(r), RR_replacement);
                    return r;
                }
                else{
                    continue;
                }
            }
        }
        return null;//couldn't find item to replace in list
    }

    //Set the selectedResult to an item in the list with the given index
    /**Changed the name a little here*/
    public void setSelectedResult(int index){
        /** this doesn't work
         * we need a clone method in ResearchResult if this is what we plan to do
         */
        selectedResult = specs.get(index);
    }

   //sets retail price of selectedResult to custom price of user
    public Boolean setRetailPrice(double price){
        selectedResult.setRetailPrice(price);
        return true;
    }

   //set Value of selectedResult to custom value of user
    public Boolean setValue(double price){
        selectedResult.setValue(price);
        return true;
    }

   //set Price per part of selectedResult to custom price of user
    public Boolean setPricePerPart(double price){
        selectedResult.setPricePerPart(price);
        return true;
    }

   //add a minifig to the minifig list of selectedResult
    public Boolean setMinifigNames(ArrayList<String> names){
        selectedResult.setMinifigList(names);
        return true;
    }

   //set rating of selectedResult
    public Boolean setRating(int rating){
        selectedResult.setRating(rating);
        return true;
    }

   //set the part count of selectedResult
    public Boolean setPartCount(int count){
        selectedResult.setPartCount(count);
        return true;
    }

   //set release date of selectedResult
    /**Concern
     * we may want to change the date values to just integer values and only use the years
     * @param date
     * @return
     */
    public Boolean setReleaseDate(Calendar date){
        selectedResult.setReleaseDate(date);
        return true;
    }

   //set Retire date of selectedResult
    /**same suggestion as previous.*/
    public Boolean setRetireDate(Calendar date){
        selectedResult.setRetireDate(date);
        return true;
    }

    /**Question
     * How do we want this method to work? Are we calculating the current value?
     * Or is this our future prediction value?
     * @param res
     * @return
     */
    public double calcValue(ResearchResult res){
        return 0;
    }

    /**Question
     * Does this return the current selectedResult?
     * If so, wouldn't it be better to call this getSelectedResult?
     * @return
     */
    public ResearchResult getResearchResult(){
        return selectedResult;
    }

    /**Question
     * Does this check if its saved in the files or in the list?
     * @return
     */
    public Boolean isSaved(){
        return true;
    }
}
