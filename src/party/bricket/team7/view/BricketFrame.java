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
        // Call the parent constructor
        super("Bricket");

        // Create a controller for the view to use
        controller = new BricketController();

        // Set the width of the search bar
        final int SEARCH_WIDTH = 20;

        // When the user clicks exit, terminate the window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        searchScroll.setPreferredSize(new Dimension(365,700));
        searchScroll.getVerticalScrollBar().setUnitIncrement(13);
        searchScroll.setBorder(BorderFactory.createEmptyBorder());
        this.setContentPane(new JLabel(new ImageIcon("src/resources/legoman1.png")));
        this.setFocusable( true );

        // Set the search button to be disabled for now
        searchButton.setEnabled(false);
        // same for save button
        saveButton.setEnabled(false);
        saveButton.setPreferredSize(new Dimension(75,20));
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

        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(SEARCH_MSG)) {
                    searchField.setText("");
                    searchButton.setEnabled(true);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if(searchField.getText().equals("")) {
                    searchField.setText(SEARCH_MSG);
                    searchButton.setEnabled(false);
                }
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == e.VK_ENTER) {
                    MouseListener[] listeners = searchButton.getMouseListeners();
                    for (MouseListener l : listeners) {
                        l.mouseClicked(null);
                    }
                }
            }
        });

        searchButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(searchButton.isEnabled()) {
                    submitSearchQuery();
                }
            }
        });

        saveButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(saveButton.isEnabled()) {
                    int ret = fc.showSaveDialog(BricketFrame.this);
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        controller.saveToFile(file.getAbsolutePath());
                    }
                }
            }
        });

        loadButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(loadButton.isEnabled()) {
                    int ret = fc.showOpenDialog(BricketFrame.this);
                    if(ret == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        saveButton.setEnabled(true);
                        ResearchResult rer = controller.loadFromFile(file.getAbsolutePath());
                        if(rer == null) {
                            // make sure loadFromFile didn't error out with versioning or otherwise
                            JOptionPane.showMessageDialog(researchResultPanel, "Error loading data from file, make sure it is not too old and all of the data is formatted correctly.");
                        } else {
                            viewResearchResult(rer, true);
                        }
                    }
                }
            }
        });

        researchResultPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_W) {
                    int index = researchResultPanel.getSelectedIndex();
                    if (index >= 0) {
                        killTab(index);
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

    public static void main(String[] args) {
        final BricketFrame main_window = new BricketFrame();
    }

    /**
     * Clears the search panel of all results
     */
    public void clearSearchResults() {
        //Get the components in the panel
        Component[] componentList = searchResultPanel.getComponents();

        //Loop through the components
        for(Component c : componentList){
            //Find the components you want to remove
            searchResultPanel.remove(c);
        }
        searchResultPanel.setBorder(null);
        searchResultPanel.setPreferredSize(new Dimension(335, 40));
    }

    /**
     * Builds and prints out the search results
     * @param itr an iterator to the list of search results found
     * @throws IOException
     */
    @Override
    public void viewSearchResults(Iterator<SearchResult> itr) throws IOException {
        SearchResult current;
        JSeparator separator;
        JPanel newResult;
        int i = 0;
        searchResultPanel.setBorder(BorderFactory.createEmptyBorder());

        while(itr.hasNext()) {
            current = itr.next();
            try {
                newResult = BricketPanelFactory.createSearchResultPanel(current);
                newResult.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Component[] comps = e.getComponent().getParent().getComponents();
                        int index = 0;
                        for (Component comp : comps) {
                            if (comp == e.getComponent()) {
                                break;
                            } else if (comp instanceof JPanel) {
                                index++;
                            }
                        }
                        submitSearchSelected(index);
                    }
                });
                searchResultPanel.add(newResult);
                searchResultPanel.setPreferredSize(new Dimension(335, (int) (searchResultPanel.getPreferredSize().getHeight() + 45)));
                separator = new JSeparator();
                separator.setMaximumSize(new Dimension(335,1));
                searchResultPanel.add(separator);
                i++;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (i == 0) {
            searchScroll.setBorder(BorderFactory.createTitledBorder("Search Results"));
            searchResultPanel.add(new JLabel("  No results found!"));
        } else if (i == 1) {
            searchScroll.setBorder(BorderFactory.createTitledBorder("Search Results - 1 result found"));
        } else if (i == 50) {
            searchScroll.setBorder(BorderFactory.createTitledBorder("Search Results - showing top 50 matches"));
        } else {
            searchScroll.setBorder(BorderFactory.createTitledBorder("Search Results - " + i + " results found"));
        }
    }

    /**
     * Submits the search query and tells the view to refresh
     */
    @Override
    public void submitSearchQuery() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                clearSearchResults();
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            viewSearchResults(controller.refreshSearch(searchField.getText()));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * Shows a new research result tab or replaces a current one
     * @param result the new research result to display
     * @param isNewResult true if a new tab is to be added
     */
    @Override
    public void viewResearchResult(ResearchResult result, boolean isNewResult) {
        if(result != null) {
            try {
                String nameAbbrev = result.getName();
                int index;
                if (nameAbbrev.length() > 15) {
                    nameAbbrev = nameAbbrev.substring(0, 12) + "...";
                }
                if(isNewResult) {
                    index = researchResultPanel.getTabCount();
                    researchResultPanel.addTab(result.getID() + " " + nameAbbrev, BricketPanelFactory.createResearchResultPanel(result, this));
                } else {
                    index = researchResultPanel.getSelectedIndex();
                    researchResultPanel.setComponentAt(index, BricketPanelFactory.createResearchResultPanel(result, this));
                    researchResultPanel.setTitleAt(index, result.getID() + " " + nameAbbrev);
                }
                researchResultPanel.setSelectedIndex(index);
                researchResultPanel.setToolTipTextAt(index, result.getID() + " " + result.getName());
            } catch (IOException ex) {
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
        controller.updateSelected(researchResultPanel.getSelectedIndex());
        controller.updatePrice(price);
        updateTab();
    }

    /**
     * Submits a new price per part to the model and refreshes the view
     * @param ppp the new price per part in dollars
     */
    @Override
    public void submitPricePerPart(double ppp) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());
        controller.updatePricePerPart(ppp);
        updateTab();
    }

    /**
     * Submits a new rating to the model as an integer between 20 and 100 and refreshes the view
     * @param rating the new rating as an integer between 20 and 100
     */
    @Override
    public void submitRating(int rating) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());
        controller.updateRating(rating);
        updateTab();
    }

    /**
     * Submits a new part count to the model and refreshes the view
     * @param count the new part count
     */
    @Override
    public void submitPartCount(int count) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());
        controller.updatePartCount(count);
        updateTab();
    }

    /**
     * Submits a new release date to the model and refreshes the view
     * @param date the new date as a string in the format MM/dd/yyyy
     * @throws ParseException
     */
    @Override
    public void submitReleaseDate(String date) throws ParseException {
        Calendar newDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        newDate.setTime(dateFormat.parse(date));
        controller.updateSelected(researchResultPanel.getSelectedIndex());
        controller.updateReleaseDate(newDate);
        updateTab();
    }

    /**
     * Submits a new retire date to the model and refreshes the view
     * @param date the new date as a string in the format MM/dd/yyyy
     * @throws ParseException
     */
    @Override
    public void submitRetireDate(String date) throws ParseException {
        Calendar newDate = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        newDate.setTime(dateFormat.parse(date));
        controller.updateSelected(researchResultPanel.getSelectedIndex());
        controller.updateRetireDate(newDate);
        updateTab();
    }

    /**
     * Resets the current tab with the original research result
     */
    @Override
    public void resetTabWithResearchResult() {
        controller.updateSelected(researchResultPanel.getSelectedIndex());
        controller.resetToOG();
        updateTab();
    }

    /**
     * Adds the research result for a search result selected
     * @param index the index of the search result chosen
     */
    @Override
    public void submitSearchSelected(int index) {
        saveButton.setEnabled(true);
        viewResearchResult(controller.selectSearchResult(index), true);
        controller.updateSelected(researchResultPanel.getSelectedIndex());
    }

    /**
     * Closes the current tab and removes that one from the model
     * @param index the index of the tab to close and remove
     */
    public void killTab(int index) {
        controller.updateSelected(researchResultPanel.getSelectedIndex());
        controller.removeResearchResult();
        researchResultPanel.remove(index);
        if (researchResultPanel.getTabCount() == 0) {
            saveButton.setEnabled(false);
        } else {
            controller.updateSelected(researchResultPanel.getSelectedIndex());
        }

    }

    /**
     * Update the current tab after the model has changed
     */
    public void updateTab() {
        viewResearchResult(controller.getResearchResult(), false);
    }

}
