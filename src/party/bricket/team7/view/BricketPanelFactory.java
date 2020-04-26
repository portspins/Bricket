package party.bricket.team7.view;

import party.bricket.team7.data.ResearchResult;
import party.bricket.team7.data.SearchResult;

import javax.imageio.ImageIO;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import static java.lang.Math.abs;

/**
 * The fields that may be edited
 */
enum editFieldNum {
    RELEASE_DATE, RETIRE_DATE, RETAIL_PRICE, PRICE_PER_PART, RATING, PART_COUNT
}

/**
 * A factory to construct panels needed by the view
 */
public abstract class BricketPanelFactory {

    /**
     * A factory method to create search result item listings
     * @param res the search result object
     * @return the panel constructed
     * @throws IOException
     */
    public static JPanel createSearchResultPanel(SearchResult res) throws IOException {
        JPanel result = new JPanel();       // Create the panels for the search result and its thumbnail
        JLabel thumb = new JLabel();
        URL url = new URL(res.getThumbnailLink());      // Get the URL for the thumbnail
        Image image = ImageIO.read(url);                // Make an image from it
        Image newImage = image.getScaledInstance(50, 40,  java.awt.Image.SCALE_SMOOTH); // Scale it
        JLabel name = new JLabel("  " + res.getId() + " " + res.getName());     // Put together the ID and name
        result.setLayout(new BorderLayout());                                        // Set the search result panel's layout
        thumb.setIcon(new ImageIcon(newImage));                                      // Attach an icon made from the thumbnail
        result.add(thumb, BorderLayout.LINE_START);                                  // Add the thumbnail to the west side of panel
        result.add(name, BorderLayout.CENTER);                                       // Add the name to the center of the panel
        result.add(new JLabel(res.getReleaseYear().toString() + " "), BorderLayout.LINE_END);   // Add the release year to the east side
        result.setMaximumSize(new Dimension(335, 50));                  // Set the panel's maximum size
        result.setAlignmentY(Component.TOP_ALIGNMENT);                                // Set the panel to align to the top
        result.setBorder(BorderFactory.createEmptyBorder(2,2,2,7)); // Give the panel some margin
        return result;
    }

    /**
     * A factory method to create research result panels
     * @param res the research result object
     * @param view the view being used
     * @return the panel constructed
     * @throws IOException
     */
    public static JPanel createResearchResultPanel(ResearchResult res, BricketView view) throws IOException {
        // Create all the panels used to build the overall panel
        JPanel result = new JPanel();
        JPanel setPhotoPanel = new JPanel();
        JPanel infoPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JButton resetButton = new JButton();
        JLabel setPhoto = new JLabel();

        String currencyString = NumberFormat.getCurrencyInstance().format(abs(res.getPeakPrice()));     // Properly format the peak price
        currencyString = currencyString.replaceAll("\\.00", "");
        JLabel predictedPriceLabel = new JLabel("<html>Estimated Peak Retirement Value: " + currencyString + "</html>");  // Create peak price label
        predictedPriceLabel.setMaximumSize(new Dimension(395, 60));                                              // Set its maximum size
        predictedPriceLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));                                            // Change the font
        predictedPriceLabel.setBorder(BorderFactory.createEmptyBorder(10,15,0,0));                     // Give it some margin
        URL url = new URL(res.getImageLink());                                                  // Get the image URL
        Image image = ImageIO.read(url);                                                        // Make an image from it
        Image newImage = image.getScaledInstance(410,350, Image.SCALE_SMOOTH);    // Scale it to a medium resolution that should work for most images

        JLabel name = new JLabel(res.getID() + " " + res.getName());                                // Create a new label for the ID and name
        name.setBorder(BorderFactory.createEmptyBorder(7,10,0,0));               // Give it some margin
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));                                     // Change its font
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));                                 // Set the layout for the panel containing the set info
        setPhotoPanel.setLayout(new BoxLayout(setPhotoPanel, BoxLayout.Y_AXIS));                         // And set the layout for the image panel
        setPhotoPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));   // Give the image some margin

        // Add the constant set info
        infoPanel.add(new InfoPanelItem("ID Number:", res.getID(), false));
        infoPanel.add(new InfoPanelItem("Name:", res.getName(), false));
        infoPanel.add(new InfoPanelItem("Theme:", res.getTheme(), false));
        String retireVal = "No";    // Assume not retired
        if (res.isRetired())        // Check if retired
            retireVal = "Yes";      // If it is, change what will be displayed
        infoPanel.add(new InfoPanelItem("Retired:", retireVal, false));

        // Format the current value and display it
        currencyString = NumberFormat.getCurrencyInstance().format(res.getValue());
        currencyString = currencyString.replaceAll("\\.00", "");
        infoPanel.add(new InfoPanelItem("Current Value:", currencyString, false));

        // Display the minifig data
        ArrayList<String> figs = res.getMinifigList();     // Get the list of minifigs
        if (figs.size() == 0) {                            // If this set has no minifigs
            infoPanel.add(new InfoPanelItem("Minifigs:", "No minifigs", false));
        } else {    // Set has minifigs
            infoPanel.add(new InfoPanelItem("Minifigs:", figs.get(0), false));  // Add the first minifig panel, which will have a label
            for (int i = 1; i < res.getMinifigList().size(); i++) { // For the rest of the minifigs, if any
                infoPanel.add(new InfoPanelItem("", figs.get(i), false));      // Add them without a label
            }
        }

        // Initialize the reset button and add it
        resetButton.setText("Reset");
        resetButton.setEnabled(true);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(1000,0)));
        buttonPanel.add(resetButton);

        // Add the release date as an editable factor
        Calendar releaseDate = res.getReleaseDate();        // Get the date
        String relDate = (releaseDate.get(Calendar.MONTH) + 1) + "/" + releaseDate.get(Calendar.DAY_OF_MONTH) + "/" + releaseDate.get(Calendar.YEAR);  // Format it
        InfoPanelItem releasePanel = new InfoPanelItem("Date Released:", relDate, true);    // Create an editable panel with it
        infoPanel.add(releasePanel);                                    // Add that panel
        JTextField releaseEdit = releasePanel.getEditField();           // Get the editable field
        addEditListener(releaseEdit, view, editFieldNum.RELEASE_DATE);  // And add its listener

        // Add the retire date as an editable factor
        Calendar retireDate = res.getRetireDate();          // Get the date
        // Create an editable panel with it
        InfoPanelItem retirePanel = new InfoPanelItem("Date Retired:", (retireDate.get(Calendar.MONTH) + 1) + "/" + retireDate.get(Calendar.DAY_OF_MONTH) + "/" + retireDate.get(Calendar.YEAR), true);
        infoPanel.add(retirePanel);                                     // Add that panel
        JTextField retireEdit = retirePanel.getEditField();             // Get the editable field
        addEditListener(retireEdit, view, editFieldNum.RETIRE_DATE);    // And add its listener

        // Add the rating as an editable factor
        InfoPanelItem ratingPanel = new InfoPanelItem("Rating:", res.getRating() + "%", true);
        infoPanel.add(ratingPanel);
        JTextField ratingEdit = ratingPanel.getEditField();         // Get its editable field
        addEditListener(ratingEdit, view, editFieldNum.RATING);     // And add its listener

        // Add the retail price as an editable factor
        currencyString = NumberFormat.getCurrencyInstance().format(res.getRetailPrice());   // Format the price
        currencyString = currencyString.replaceAll("\\.00", "");
        InfoPanelItem pricePanel = new InfoPanelItem("Retail Price:", currencyString, true);
        infoPanel.add(pricePanel);
        JTextField priceEdit = pricePanel.getEditField();               // Get its editable field
        addEditListener(priceEdit, view, editFieldNum.RETAIL_PRICE);    // And add its listener

        // Add the part count as an editable factor
        InfoPanelItem partPanel = new InfoPanelItem("Part Count:", Integer.toString(res.getPartCount()), true);
        infoPanel.add(partPanel);
        JTextField partEdit = partPanel.getEditField();             // Get its editable field
        addEditListener(partEdit, view, editFieldNum.PART_COUNT);   // And add its listener

        // Add the price per part as an editable factor
        currencyString = NumberFormat.getCurrencyInstance().format(res.getPricePerPart());      // Format the price
        InfoPanelItem perPartPanel = new InfoPanelItem("Price Per Part:", currencyString, true);
        infoPanel.add(perPartPanel);
        JTextField pppEdit = perPartPanel.getEditField();               // Get its editable field
        addEditListener(pppEdit, view, editFieldNum.PRICE_PER_PART);    // And add its listener

        // Add the listener to make the reset button reset the result when clicked
        resetButton.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(resetButton.isEnabled()) {
                    view.resetTabWithResearchResult();
                }
            }
        });

        result.setLayout(new BorderLayout());           // Set the layout of the overall panel
        setPhoto.setIcon(new ImageIcon(newImage));      // Create an icon from the set image and add it to the set photo label
        result.add(name, BorderLayout.PAGE_START);      // Add the name to the top of the panel
        setPhotoPanel.add(setPhoto);                    // Add the set photo label to the set photo panel
        setPhotoPanel.add(predictedPriceLabel);         // Also add the peak price to the set photo panel
        result.add(setPhotoPanel, BorderLayout.LINE_START);  // Add the set photo panel to the west side of the result panel
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,10));    // Give the info panel some margin
        result.add(infoPanel, BorderLayout.CENTER);                                                 // Add the info panel to the center
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));   // Give the button panel some margin on bottom
        result.add(buttonPanel, BorderLayout.PAGE_END);                                             // Add the button panel to the bottom
        result.setMaximumSize(new Dimension(800, 100));                               // Set the maximum size of the result panel
        result.setBorder(BorderFactory.createEmptyBorder(2,2,2,7));         // Give the result panel some margin
        result.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {  // Make it so the result panel can take focus from editable fields and make them exit edit mode
                super.mouseClicked(e);
                result.requestFocus();
            }
        });
        return result;
    }

    // Adds the listener to each edit field so when the Enter key is pressed, the value is submitted
    //
    private static void addEditListener(JTextField editField, BricketView view, editFieldNum num) {
        switch (num) {
            case RATING:
                editField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            // If enter is pressed for this field, submit the rating, with anything but numbers removed, as a positive integer
                            view.submitRating(abs(Integer.parseInt(editField.getText().replaceAll("[^0-9]", ""))));
                        }
                    }
                });
                break;
            case PART_COUNT:
                editField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            // If enter is pressed for this field, submit the part count, with anything but numbers removed, as a positive integer
                            view.submitPartCount(abs(Integer.parseInt(editField.getText().replaceAll("[^0-9]", ""))));
                        }
                    }
                });
                break;
            case RETIRE_DATE:
                editField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            // If enter is pressed for this field, submit the retire date
                            try {
                                view.submitRetireDate(editField.getText());
                            } catch (ParseException ex) {  // If the date entered is invalid, leave the value unchanged and exit edit mode
                                editField.getParent().requestFocus();
                            }
                        }
                    }
                });
                break;
            case RELEASE_DATE:
                editField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            // If enter is pressed for this field, submit the release date
                            try {
                                view.submitReleaseDate(editField.getText());
                            } catch (ParseException ex) {  // If the date entered is invalid, leave the value unchanged and exit edit mode
                                editField.getParent().requestFocus();
                            }
                        }
                    }
                });
                break;
            case RETAIL_PRICE:
                editField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            // If enter is pressed for this field, submit the retail price
                            view.submitRetailPrice(abs(Double.parseDouble(editField.getText().replaceAll("[^-?0-9.]", ""))));
                        }
                    }
                });
                break;
            case PRICE_PER_PART:
                editField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        super.keyPressed(e);
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            // If enter is pressed for this field, submit the price per part
                            view.submitPricePerPart(abs(Double.parseDouble(editField.getText().replaceAll("[^-?0-9.]", ""))));
                        }
                    }
                });
                break;
        }
    }
}
