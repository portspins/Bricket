package party.bricket.team7;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

public final class BricketFrame extends JFrame implements BricketView {
    final JButton searchButton;
    final JTextField searchField;
    final JPanel searchBarPanel;
    final JPanel searchResultPanel;
    final JPanel researchPanel;
    final JScrollPane searchScroll;
    final BricketController controller;
    final String SEARCH_MSG;

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
        searchBarPanel = new JPanel();
        searchResultPanel = new JPanel();
        searchScroll = new JScrollPane(searchResultPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        researchPanel = new JPanel();
        SEARCH_MSG = "Search by item ID or name...";

        searchScroll.setPreferredSize(new Dimension(350,700));
        searchScroll.getVerticalScrollBar().setUnitIncrement(13);
        searchScroll.setBorder(BorderFactory.createEmptyBorder());

        this.setFocusable( true );

        // Set the search button to be disabled for now
        searchButton.setEnabled(false);

        // Set the search button's bounds
        searchButton.setPreferredSize(new Dimension(75, 20));

        // Set the search field's maximum width
        searchField.setPreferredSize(new Dimension(200, 20));
        searchField.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED, new Color(66,66,66), new Color(66,66,66)));
        searchField.setMaximumSize(searchField.getPreferredSize());

        // Set the search bar panel's layout
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));
        searchBarPanel.setBorder(BorderFactory.createEmptyBorder(12,22,12,0));

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
            }
        });

        // Set the window's layout
        this.setLayout(new BorderLayout());

        // Add the components to the panel
        searchBarPanel.add(searchField);
        searchBarPanel.add(Box.createRigidArea(new Dimension(2, 0)));
        searchBarPanel.add(searchButton);

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
        this.add(researchPanel, BorderLayout.LINE_END);

        // Set the initial size of the window
        this.setSize(new Dimension(1280,720));

        // Make the window render to the screen
        this.setVisible(true);
    }

    public static void main(String[] args) {
        final BricketFrame main_window = new BricketFrame();

        // Create a new Search object with a BricksetScraper object and the query

        // Iterate through Search's SearchResults and print out the info from each

    }

    @Override
    public String getFilename() {
        return null;
    }

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

    @Override
    public void viewSearchResults(Iterator<SearchResult> itr) throws IOException {
        SearchResult current;
        searchResultPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        int i = 0;
        while(itr.hasNext()) {
            current = itr.next();
            try {
                JPanel newResult = BricketPanelFactory.createSearchResultPanel(current);
                searchResultPanel.add(newResult);
                searchResultPanel.setPreferredSize(new Dimension(335, (int) (searchResultPanel.getPreferredSize().getHeight() + 40)));
                i++;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (i == 0) {
            searchResultPanel.add(new JLabel("  No results found!"));
        } else if (i == 1) {
            searchResultPanel.setBorder(BorderFactory.createTitledBorder("Search Results - 1 result found"));
        } else if (i == 50) {
            searchResultPanel.setBorder(BorderFactory.createTitledBorder("Search Results - showing top 50 matches"));
        } else {
            searchResultPanel.setBorder(BorderFactory.createTitledBorder("Search Results - " + i + " results found"));
        }
    }

    @Override
    public void submitSearchQuery() {

    }

    @Override
    public int getScope() {
        return 0;
    }

    @Override
    public boolean promptAddOrReplace() {
        return false;
    }

    @Override
    public boolean promptSave() {
        return false;
    }

    @Override
    public void viewResearchResult(ResearchResult results) {

    }

    @Override
    public void submitRetailPrice() {

    }

    @Override
    public void submitPricePerPart() {

    }

    @Override
    public void submitMinifigName() {

    }

    @Override
    public void submitRating() {

    }

    @Override
    public void submitPartCount() {

    }

    @Override
    public void submitReleaseDate() {

    }

    @Override
    public void submitRetireDate() {

    }

    @Override
    public void submitSearchSelected() {

    }

    @Override
    public void submitResetResult() {

    }
}
