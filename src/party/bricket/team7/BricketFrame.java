package party.bricket.team7;

import javax.imageio.ImageIO;
import javax.swing.*;
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

        searchScroll.setPreferredSize(new Dimension(315,700));
        searchScroll.getVerticalScrollBar().setUnitIncrement(12);

        this.setFocusable( true );

        // Set the search button to be disabled for now
        searchButton.setEnabled(false);

        // Set the search button's bounds
        searchButton.setPreferredSize(new Dimension(75, 20));

        // Set the search field's maximum width
        searchField.setMaximumSize(searchField.getPreferredSize());

        // Set the search bar panel's layout
        searchBarPanel.setLayout(new BoxLayout(searchBarPanel, BoxLayout.X_AXIS));

        // Set the search result panel's layout
        searchResultPanel.setLayout(new BoxLayout(searchResultPanel, BoxLayout.Y_AXIS));
        searchResultPanel.setPreferredSize(new Dimension(300, 40));

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
        searchResultPanel.setPreferredSize(new Dimension(300, 40));
    }

    @Override
    public void viewSearchResults(Iterator<SearchResult> itr) throws IOException {
        JPanel result;
        JLabel name;
        JLabel thumb;
        URL url;
        Image image;
        Image newImage;
        SearchResult current;
        searchResultPanel.setBorder(BorderFactory.createTitledBorder("Search Results"));
        while(itr.hasNext()) {
            current = itr.next();
            result = new JPanel();
            thumb = new JLabel();
            url = new URL(current.getThumbnailLink());
            image = ImageIO.read(url);
            newImage = image.getScaledInstance(50, 40,  java.awt.Image.SCALE_SMOOTH);
            name = new JLabel("  " + current.getId() + " " + current.getName());
            result.setLayout(new BorderLayout());
            thumb.setIcon(new ImageIcon(newImage));
            result.add(thumb, BorderLayout.LINE_START);
            result.add(name, BorderLayout.CENTER);
            result.add(new JLabel(current.getReleaseYear().toString() + " "), BorderLayout.LINE_END);
            result.setMaximumSize(new Dimension(300, 50));
            result.setAlignmentY(Component.TOP_ALIGNMENT);
            result.setBorder(BorderFactory.createEmptyBorder(0,2,3,0));
            searchResultPanel.add(result);
            searchResultPanel.setPreferredSize(new Dimension(300, (int) (searchResultPanel.getPreferredSize().getHeight() + 40)));
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
