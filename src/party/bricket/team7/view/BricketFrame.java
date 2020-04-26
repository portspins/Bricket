package party.bricket.team7.view;

import party.bricket.team7.control.BricketController;
import party.bricket.team7.data.ResearchResult;
import party.bricket.team7.data.SearchResult;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

/**
 * A standard BricketView implementation class
 */
public final class BricketFrame extends JFrame implements BricketView {
    final private JButton searchButton;
    final private JButton saveButton;
    final private JButton loadButton;
    final private JTextField searchField;
    final private JPanel searchBarPanel;
    final private JFileChooser fc;
    final private JPanel searchResultPanel;
    final private JTabbedPane researchResultPanel;
    final private JScrollPane searchScroll;
    final private BricketController controller;
    final private String SEARCH_MSG;

    /**
     * The constructor, which starts up the view
     */
    public BricketFrame() {
        // Call the parent constructor, setting the window title
        super("Bricket");

        // Create a controller for the view to use
        controller = new BricketController();

        // Set the width of the search bar
        final int SEARCH_WIDTH = 20;

        // When the user clicks exit, terminate the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize the various window elements
        searchField = new JTextField("Search by item ID or name...", SEARCH_WIDTH);
        searchButton = new JButton("Search");
        saveButton = new JButton("Save");
        loadButton = new JButton("Load");
        fc = new JFileChooser();
        searchBarPanel = new JPanel();
        searchResultPanel = new JPanel();
        researchResultPanel = new JTabbedPane();
        searchScroll = new JScrollPane(searchResultPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        SEARCH_MSG = "Search by item ID or name...";

        // Set the scroll dimensions and speed for the search results panel
        searchScroll.setPreferredSize(new Dimension(365,700));
        searchScroll.getVerticalScrollBar().setUnitIncrement(13);
        searchScroll.setBorder(BorderFactory.createEmptyBorder());

        // Set the Lego Man Easter egg
        this.setContentPane(new JLabel(new ImageIcon("src/resources/legoman1.png")));

        // Allow window to be focused
        this.setFocusable( true );

        // Set the search button to be disabled for now
        searchButton.setEnabled(false);
        // Same for save button
        saveButton.setEnabled(false);
        saveButton.setPreferredSize(new Dimension(75,20));
        // Load button should be enabled
        loadButton.setEnabled(true);
        loadButton.setPreferredSize(new Dimension(75,20));
        // Set the search button's bounds
        searchButton.setPreferredSize(new Dimension(75, 20));

        // Set the search field's maximum width
        searchField.setPreferredSize(new Dimension(200, 20));
        searchField.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, new Color(66,66,66), new Color(66,66,66)));
        searchField.setMaximumSize(searchField.getPreferredSize());

        // Set the search bar panel's layout
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(12,25,12,0));

        // Set the search result panel's layout
        searchResultPanel.setLayout(new BoxLayout(searchResultPanel, BoxLayout.Y_AXIS));
        searchResultPanel.setPreferredSize(new Dimension(335, 40));

        // Add the focus listeners for the search field
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(SEARCH_MSG)) { // If the search field is gaining focus for the first time
                    searchField.setText("");    // Clear its text
                    searchButton.setEnabled(true);  // And enable the search button
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(searchField.getText().equals("")) {  // If it loses focus while blank
                    searchField.setText(SEARCH_MSG);    // Display the search message
                    searchButton.setEnabled(false);     // And disable the search button
                }
            }
        });

        // Add the key listener for the search field
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == e.VK_ENTER) { // If the Enter key was pressed
                    // Perform as if the search button was pressed
                    MouseListener[] listeners = searchButton.getMouseListeners();
                    for (MouseListener l : listeners) {
                        l.mouseClicked(null);
                    }
                }
            }
        });

        // Add the click listener for the search button
        searchButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(searchButton.isEnabled()) {  // If the button is enabled
                    submitSearchQuery();        // Submit the search field's value
                }
            }
        });

        // Add the click listener for the save button
        saveButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(saveButton.isEnabled()) {    // If the button is enabled
                    int ret = fc.showSaveDialog(BricketFrame.this); // Allow user to choose where to save file
                    if (ret == JFileChooser.APPROVE_OPTION) {              // If the user clicked save
                        File file = fc.getSelectedFile();                  // Try to open the file to write to
                        if(!controller.saveToFile(file.getAbsolutePath())) {    // If there is an error
                            // Display error message
                            JOptionPane.showMessageDialog(researchResultPanel, "Error saving to " + file.getAbsolutePath() + ". Please choose a different location.");
                        }
                    }
                }
            }
        });

        // Add the click listener for the load button
        loadButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(loadButton.isEnabled()) {    // If the load button is enabled
                    int ret = fc.showOpenDialog(BricketFrame.this); // Let the user choose file to load from
                    if(ret == JFileChooser.APPROVE_OPTION) {    // If they choose open
                        File file = fc.getSelectedFile();       // Get the file
                        ResearchResult rer = controller.loadFromFile(file.getAbsolutePath());   // Try to load a research result from file
                        if(rer == null) {   // If it failed to load in a result from the file due to versioning or otherwise
                            // Display the error message
                            JOptionPane.showMessageDialog(researchResultPanel, "Error loading data from file, make sure it is not too old and all of the data is formatted correctly.");
                        } else {    // If the research result loaded successfully
                            viewResearchResult(rer, true);  // Display it in a new tab
                            saveButton.setEnabled(true);                // Enable the save button
                        }
                    }
                }
            }
        });

        // Add the listener so a research tab can be closed
        researchResultPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W) {     // If Ctrl+W pressed while holding down tab
                    int index = researchResultPanel.getSelectedIndex(); // Get the index of the selected tab
                    if (index >= 0) {   // If it is valid
                        killTab(index); // Close the tab
                    }
                }
            }
        });

        // Set the window's layout
        this.setLayout(new BorderLayout());

        // Add the components to the panel
        searchBarPanel.add(searchField);
        searchBarPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        searchBarPanel.add(searchButton);
        searchBarPanel.add(Box.createRigidArea(new Dimension(815,0)));
        searchBarPanel.add(loadButton);
        searchBarPanel.add(Box.createRigidArea(new Dimension(10,0)));
        searchBarPanel.add(saveButton);

        // Allow window to focus upon mouse press in window
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                requestFocusInWindow();
            }
        });

        // Add the search bar panel to the window
        this.add(searchBarPanel, BorderLayout.PAGE_START);

        // Add the search result panel to the window
        this.add(searchScroll, BorderLayout.LINE_START);

        // Add the research panel to the window
        this.add(researchResultPanel, BorderLayout.CENTER);

        // Set the initial size of the window
        this.setSize(new Dimension(1280,720));

        // Make the window render to the screen
        this.setVisible(true);
    }

    /**
     * Starting point in the application
     * @param args the arguments passed in from command line
     */
    public static void main(String[] args) {
        final BricketFrame main_window = new BricketFrame();
    }

    /**
     * Clears the search panel of all results
     */
    public void clearSearchResults() {
        // Get the components in the search panel
        Component[] componentList = searchResultPanel.getComponents();

        // Loop through the components and remove them
        for(Component c : componentList){
            searchResultPanel.remove(c);
        }

        // Reset size of the search panel
        searchResultPanel.setBorder(null);
        searchResultPanel.setPreferredSize(new Dimension(335, 40));
    }

    /**
     * Builds and prints out the search results
     * @param itr an iterator to the list of search results found
     */
    @Override
    public void viewSearchResults(Iterator<SearchResult> itr) {
        SearchResult current;
        JSeparator separator;
        JPanel newResult;
        int i = 0;
        searchResultPanel.setBorder(BorderFactory.createEmptyBorder());

        while(itr.hasNext()) {      // While there are search results left in the list
            current = itr.next();   // Get the next result in the list
            try {
                newResult = BricketPanelFactory.createSearchResultPanel(current);   // Create a panel for this result
                newResult.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {       // Add a click listener to it for when a result is clicked
                        Component[] comps = e.getComponent().getParent().getComponents();   // Get the search results
                        int index = 0;

                        // Get the one that was clicked
                        for (Component comp : comps) {
                            if (comp == e.getComponent()) {
                                break;
                            } else if (comp instanceof JPanel) {
                                index++;
                            }
                        }
                        submitSearchSelected(index);    // Submit the search result selected so the research result is created and opens in a new tab
                    }
                });
                searchResultPanel.add(newResult);   // Add this search result's panel to the panel of search results
                searchResultPanel.setPreferredSize(new Dimension(335, (int) (searchResultPanel.getPreferredSize().getHeight() + 45)));  // Add to the height of the results panel
                // Add a separator after the result
                separator = new JSeparator();
                separator.setMaximumSize(new Dimension(335,1));
                searchResultPanel.add(separator);
                i++;    // Increment the counter of search results
            } catch (IOException ex) {  // Handle the IOException if it is thrown
                ex.printStackTrace();
            }
        }
        if (i == 0) {   // If no results found
            // Change the search result panel to reflect that and output a JLabel stating that no results were found
            searchScroll.setBorder(BorderFactory.createTitledBorder("Search Results"));
            searchResultPanel.add(new JLabel("  No results found!"));
        } else if (i == 1) {    // If one result was found
            // Change the titled border to reflect singular result found
            searchScroll.setBorder(BorderFactory.createTitledBorder("Search Results - 1 result found"));
        } else if (i == 50) {   // If list of search results was at max capacity
            // Change the titled border to reflect that the top 50 results are being shown
            searchScroll.setBorder(BorderFactory.createTitledBorder("Search Results - showing top 50 matches"));
        } else {    // Otherwise change the titled border to reflect how many results (1 < i < 50) were found
            searchScroll.setBorder(BorderFactory.createTitledBorder("Search Results - " + i + " results found"));
        }
    }

    /**
     * Submits the search query and tells the view to refresh
     */
    @Override
    public void submitSearchQuery() {
        new Thread(new Runnable() {     // Start up a new thread
            @Override
            public void run() {
                clearSearchResults();   // Clear the past search results
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        viewSearchResults(controller.refreshSearch(searchField.getText())); // Get the search results and display them
                    }
                });
            }
        }).start(); // Start the runnable thread
    }

    /**
     * Shows a new research result tab or replaces a current one
     * @param result the new research result to display
     * @param isNewResult true if a new tab is to be added
     */
    @Override
    public void viewResearchResult(ResearchResult result, boolean isNewResult) {
        if(result != null) {    // Ensure the result reference is not null
            try {
                String nameAbbrev = result.getName();   // Get the set name
                int index;

                // Abbreviate the name if necessary
                if (nameAbbrev.length() > 15) {
                    nameAbbrev = nameAbbrev.substring(0, 12) + "...";
                }

                if(isNewResult) { // If this is to be displayed as a new tab
                    index = researchResultPanel.getTabCount(); // Set the index to where the result will be
                    // Create the new research result panel and add the tab
                    researchResultPanel.addTab(result.getID() + " " + nameAbbrev, BricketPanelFactory.createResearchResultPanel(result, this));
                } else {    // If this is to replace the current tab
                    index = researchResultPanel.getSelectedIndex(); // Set the index to the currently selected index
                    researchResultPanel.setComponentAt(index, BricketPanelFactory.createResearchResultPanel(result, this));     // Replace the current tab
                    researchResultPanel.setTitleAt(index, result.getID() + " " + nameAbbrev);   // Update the title
                }
                researchResultPanel.setSelectedIndex(index);    // Set the selected index to wherever the new or modified result is
                researchResultPanel.setToolTipTextAt(index, result.getID() + " " + result.getName());   // Update the tool tip
            } catch (IOException ex) {  // If there was an IOException print the error
                ex.printStackTrace();
            }
        }
    }

    /**
     * Submits a new retail price to the model and refreshes the view
     * @param price the new retail price in dollars
     */
    @Override
    public void submitRetailPrice(double price) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
        controller.updatePrice(price);  // Then submit the new price
        updateTab();    // Update the tab to reflect the updated research result
    }

    /**
     * Submits a new price per part to the model and refreshes the view
     * @param ppp the new price per part in dollars
     */
    @Override
    public void submitPricePerPart(double ppp) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
        controller.updatePricePerPart(ppp);  // Then submit the new price per part
        updateTab();    // Update the tab to reflect the updated research result
    }

    /**
     * Submits a new rating to the model as an integer between 20 and 100 and refreshes the view
     * @param rating the new rating as an integer between 20 and 100
     */
    @Override
    public void submitRating(int rating) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
        controller.updateRating(rating);  // Then submit the new rating
        updateTab();    // Update the tab to reflect the updated research result
    }

    /**
     * Submits a new part count to the model and refreshes the view
     * @param count the new part count
     */
    @Override
    public void submitPartCount(int count) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
        controller.updatePartCount(count);  // Then submit the new part count
        updateTab();    // Update the tab to reflect the updated research result
    }

    /**
     * Submits a new release date to the model and refreshes the view
     * @param date the new date as a string in the format MM/dd/yyyy
     * @throws ParseException
     */
    @Override
    public void submitReleaseDate(String date) throws ParseException {
        // Parse the date string and create a Calendar object from it
        Calendar newDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        newDate.setTime(dateFormat.parse(date));

        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
        controller.updateReleaseDate(newDate);  // Then submit the new release date
        updateTab();    // Update the tab to reflect the updated research result
    }

    /**
     * Submits a new retire date to the model and refreshes the view
     * @param date the new date as a string in the format MM/dd/yyyy
     * @throws ParseException
     */
    @Override
    public void submitRetireDate(String date) throws ParseException {
        // Parse the date string and create a Calendar object from it
        Calendar newDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        newDate.setTime(dateFormat.parse(date));

        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
        controller.updateRetireDate(newDate);  // Then submit the new retire date
        updateTab();    // Update the tab to reflect the updated research result
    }

    /**
     * Resets the current tab with the original research result
     */
    @Override
    public void resetTabWithResearchResult() {
        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
        controller.resetToOG();  // Then tell the controller to reset speculator's selected result
        updateTab();    // Update the tab to reflect the updated research result
    }

    /**
     * Adds the research result for a search result selected
     * @param index the index of the search result chosen
     */
    @Override
    public void submitSearchSelected(int index) {
        saveButton.setEnabled(true);    // Now that a result is open, enable the save button
        viewResearchResult(controller.selectSearchResult(index), true);     // Display the research result in a new tab
        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
    }

    /**
     * Closes the current tab and removes that one from the model
     * @param index the index of the tab to close and remove
     */
    public void killTab(int index) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());  // Tell the controller to set speculator's selected index to the selected tab
        controller.removeResearchResult();  // Then tell the controller to remove speculator's selected result
        researchResultPanel.remove(index);  // Close the tab
        if (researchResultPanel.getTabCount() == 0) {   // If there are no tabs open anymore
            saveButton.setEnabled(false);   // Disable the save button
        } else {    // Otherwise tell the controller to set speculator's selected index to the newly selected tab
            controller.updateSelected(researchResultPanel.getSelectedIndex());
        }

    }

    /**
     * Update the current tab after the model has changed
     */
    public void updateTab() {
        viewResearchResult(controller.getResearchResult(), false);  // Get the speculator's selected result from controller and display it in the same tab
    }

}
