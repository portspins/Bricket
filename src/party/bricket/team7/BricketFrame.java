package party.bricket.team7;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BricketFrame extends JFrame {
    final JButton searchButton;
    final JTextField searchField;
    final JPanel searchPanel;
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
        searchPanel = new JPanel();
        SEARCH_MSG = "Search by item ID or name...";

        this.setFocusable( true );

        // Set the search button to be disabled for now
        searchButton.setEnabled(false);

        // Set the search button's bounds
        searchButton.setPreferredSize(new Dimension(75, 20));

        // Set the panel's layout
        searchPanel.setLayout(new FlowLayout());

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
                    controller.searchSubmitted(searchField.getText());
                }
            }
        });

        // Set the window's layout
        this.setLayout(new BorderLayout());

        // Add the components to the panel
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                requestFocusInWindow();
            }
        });

        // Add the panel to the window
        this.add(searchPanel, BorderLayout.EAST);

        // Set the initial size of the window
        this.pack();

        // Make the window render to the screen
        this.setVisible(true);
    }

    public static void main(String[] args) {
        final BricketFrame main_window = new BricketFrame();

        // Create a new Search object with a BricksetScraper object and the query

        // Iterate through Search's SearchResults and print out the info from each

    }
}
