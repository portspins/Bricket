package party.bricket.team7;

import java.util.*;
import java.util.Scanner;

public class Speculator {
    private Boolean isSaved;
    private BricksetItemScraper bSScraper;
    private LinkedList<ResearchResult> specs = new LinkedList<ResearchResult>();
    private LinkedList<ResearchResult> OGSpecs = new LinkedList<ResearchResult>();//Used to store the original version of an item
    private ResearchResult selectedResult;

    public Speculator(){

    }

    // add a new ResearchResult to the list by constructing it from the scrapper and a passed in SearchResult
    public void addResearchResult(SearchResult res) {
        ResearchResult rResult = makeResearchResult(res);
        ResearchResult rResultClone = (ResearchResult) rResult.clone();
        specs.add(rResult);
        //needs to check if item is already stored in original state
        if(hasOGResult(rResultClone)){
            selectedResult = rResult;
            return;
        }
        OGSpecs.add(rResultClone);
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

    //add a ResearchResult by passing a ResearchResult object in
    public void addResearchResult(ResearchResult res){
        ResearchResult cloned = (ResearchResult) res.clone();
        specs.add(res);
        if(hasOGResult(res)) {
            selectedResult = res;
            return;
        }
        OGSpecs.add(cloned);
        selectedResult = res;
    }

    //Remove a current selectedResult
    public ResearchResult removeResearchResult(){
       if(specs.isEmpty()){
           System.out.println("There are currently no opened sessions");
           return null;
       }
       specs.remove(selectedResult);

       if(hasResult(selectedResult)){
           return selectedResult;
       }
       else {
           OGSpecs.remove(findOGResult(selectedResult));
       }

       return selectedResult;

    }

    //Resets the current ResearchResult to it's original state
    public Boolean resetResearchResult() {

        selectedResult.setRetailPrice(findOGResult(selectedResult).getRetailPrice());
        selectedResult.setValue(findOGResult(selectedResult).getValue());
        selectedResult.setMinifigList(findOGResult(selectedResult).getMinifigList());
        selectedResult.setRating(findOGResult(selectedResult).getRating()/20.0);
        selectedResult.setPartCount(findOGResult(selectedResult).getPartCount());
        selectedResult.setReleaseDate(findOGResult(selectedResult).getReleaseDate());
        selectedResult.setRetireDate(findOGResult(selectedResult).getRetireDate());

        return true;
    }

    //Check to see if the list is empty
    public Boolean hasResults(){
        if(specs.isEmpty()){
            return false; //specs has no results
        } else {
            return true; //specs has results
        }
    }

    //Used to check if there is an original version of res item in OGSpecs
    private Boolean hasOGResult(ResearchResult res){
        for(ResearchResult r: OGSpecs){
            if(r.equals(res)){
                return true;
            }
            else{
                continue;
            }
        }
        return false;
    }

    private ResearchResult findOGResult(ResearchResult res){
        if(!hasOGResult(res)){
            return null;
        }
        for(ResearchResult r: OGSpecs){
            if(r.equals(res)){
                return r;
            }
            else{
                continue;
            }
        }
        return null;
    }

    //Used to check if there is version of res item in specs
    private Boolean hasResult(ResearchResult res){
        for(ResearchResult r: specs){
            if(r.equals(res)){
                return true;
            }
            else{
                continue;
            }
        }
        return false;
    }

    //replace a ResearchResult by passing in a SearchResult and constructing the item to be added
    public ResearchResult replaceResearchResult(SearchResult res){
        if(specs.isEmpty()){
            System.out.println("There are currently no opened sessions");
            return null;
        }
        ResearchResult temp = makeResearchResult(res);
        specs.set(specs.indexOf(selectedResult),temp);

        if(!hasResult(selectedResult)){
            OGSpecs.remove(findOGResult(selectedResult));
        }

        if(!hasOGResult(temp)){
            OGSpecs.add(temp);
        }

        selectedResult = temp;
        return selectedResult;
    }

   //Replace a ResearchResult in the list by passing in a ResearchResult
    private ResearchResult replaceResearchResult(ResearchResult RR_replacement){
        if(specs.isEmpty()){
            System.out.println("There are currently no opened sessions");
            return null;
        }
        specs.set(specs.indexOf(selectedResult), RR_replacement);

        if(!hasResult(selectedResult)){
            OGSpecs.remove(findOGResult(selectedResult));
        }

        if(!hasOGResult(RR_replacement)){
            OGSpecs.add(RR_replacement);
        }

        selectedResult = RR_replacement;
        return selectedResult;
    }

    //Set the selectedResult to an item in the list with the given index
    public void setSelectedResult(int index){
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
        selectedResult.setRating(rating / 20.0);
        return true;
    }

   //set the part count of selectedResult
    public Boolean setPartCount(int count){
        selectedResult.setPartCount(count);
        return true;
    }

   //set release date of selectedResult
    public Boolean setReleaseDate(Calendar date){
        selectedResult.setReleaseDate(date);
        return true;
    }

   //set Retire date of selectedResult
    public Boolean setRetireDate(Calendar date){
        selectedResult.setRetireDate(date);
        return true;
    }

    /**
     * The Algorithm™
     *
     * @return
     */
    public double calcValue() {
        /*
         * algorithm will basically work like this
         * peak price = rrp + retireDifference/10000000 + releaseDifference/10000000 + value/1000 + rating/1000 + partCount/100 + minifigCount/10
         */
        Random rand = new Random();
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        Calendar nullCalendar = Calendar.getInstance(TimeZone.getTimeZone("America/Chicago"));
        nullCalendar.setTimeInMillis(0);

        Double peakPrice = 0.0;

        Calendar retired = selectedResult.getRetireDate();
        Calendar release = selectedResult.getReleaseDate();
        boolean isRetired = selectedResult.isRetired();
        Integer retireDifference = null; // in milliseconds
        Integer releaseDifference = null; // same here
        Integer partCount = selectedResult.getPartCount();
        Integer rating = selectedResult.getRating();
        Double rrp = selectedResult.getRetailPrice();
        Double value = selectedResult.getValue();
        Integer miniFigCount = selectedResult.getMinifigList().size();
        // unknown retired date
        if(isRetired) {
            if (!retired.equals(nullCalendar)) {
                retireDifference = today.compareTo(retired);
            }
        }
        if(!release.equals(nullCalendar)) {
            releaseDifference = today.compareTo(release);
        }
        if (rrp != -1.0) {
            peakPrice += rrp;
            if(value == -1.0) {
                peakPrice += rrp/1000.0;
            }
        }
        if(value != -1.0) {
            peakPrice += value/1000.0;
            if(rrp == -1.0) {
                peakPrice += value;
            }
        }
        if(value == -1.0 && rrp == -1.0) {
            if(partCount != -1) {
                peakPrice += partCount*0.075;
            }
            else if(rating != -20) {
                peakPrice += rating/1000.0 + 3.14159265359;
            } else {
                // in this case value/rrp/partCount are not known, so we use pi as the base + random number
                peakPrice += 3.14159265359 + rand.nextDouble();
            }
        }
        if(retireDifference != null) {
            peakPrice += retireDifference/10000000.0;
        }
        if(releaseDifference != null) {
            peakPrice += releaseDifference/10000000.0;
        }
        if(partCount != -1) {
            peakPrice += partCount/100.0;
        }
        if(miniFigCount > 0) {
            peakPrice += miniFigCount/10.0;
        }

        selectedResult.setPeakPrice(peakPrice);
        return peakPrice;
    }

    //Returns current selectedResult
    public ResearchResult getResearchResult(){
        calcValue();
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
