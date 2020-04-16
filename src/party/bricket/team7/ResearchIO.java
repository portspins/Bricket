package party.bricket.team7;

import java.io.File;

/** party.bricket.team7.ResearchIO: An interface for Speculator that will enable the saving
 * and loading of a ResearchResult
 * Save a ResearchResult as a file, store its data, and implement a "load" option to open saved ResearchResult files
 * Refresh method to restore the original data for a speculator
 * @author Daniel Morris
 */
public interface ResearchIO {
    /** research File.
     * stores the data of the most recently opened ResearchResult file.
     * change to store the selected ResearchResult file in its tab later.
     */
    File research = new File("");

    /** GetItemLink.
     *Retrieves the brickset link.
     */
    ResearchResult stored = new ResearchResult();

    /** loadResearch.
     * Accepts a file name String and returns the stored
     * ResearchResult. Stores the loaded file in research.
     * Need a consistent naming scheme for files.
     */
    ResearchResult loadResearch(String id);

    /** saveResearch.
     * Saves the researchResult in the Speculator into a file.
     * Need a consistent naming scheme for files
     */
    boolean saveResearch(ResearchResult s);

    /** refreshResearch.
     * (???) Resets the researchResult parameter's components to
     * the stored ResearchResult. Returns reset copy of stored
     * ResearchResult
     */
    ResearchResult refreshResearch(ResearchResult s);
}
