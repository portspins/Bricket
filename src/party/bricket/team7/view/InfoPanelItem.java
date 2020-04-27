package party.bricket.team7.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A panel for a research result information item
 * @author Matthew Hise
 */

public class InfoPanelItem extends JPanel {
    private JLabel label;
    private JTextField editField;

    /**
     * Constructor
     */
    InfoPanelItem() {
        super();    // Call the JPanel constructor
        setLayout(new GridLayout(1,2, 8,8));    // Set up a 2 column row with spacing
        setMaximumSize(new Dimension(360, 27));           // Set the maximum size for the info panel
    }

    /**
     * Parametrized constructor
     * @param label the label defining what information is presented
     * @param value the initial value corresponding to the label
     * @param editable true if the value should be able to be edited
     */
    InfoPanelItem(String label, String value, boolean editable) {
        this();                     // Call the default constructor
        setLabel(label);            // Set the label
        setInfo(value, editable);   // Set the corresponding info, passing in if it is editable
    }

    /**
     * Set the label for the panel
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = new JLabel(label);         // Create a new JLabel with the desired text
        add(this.label, 0);               // Add it to the first column of the row
    }

    /**
     * Set the value and allow it to be edited if requested
     * @param value the initial value
     * @param editable true if the value should be able to be edited
     */
    public void setInfo(String value, boolean editable) {
        final JLabel valLabel = new JLabel(value);       // Create a new JLabel with the initial value set
        if (editable) {  // If this info is editable
            editField = new JTextField(value);                        // Create a new JTextField with the initial value set
            editField.setMaximumSize(editField.getPreferredSize());   // Set its maximum size
            editField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {   // When the field loses focus, toggle back to the JLabel
                    super.focusLost(e);
                    add(valLabel,1);    // Add the JLabel back
                    remove(editField);         // In place of the JTextField
                    revalidate();              // Refresh the view
                    repaint();
                }
            });
            valLabel.setForeground(new Color(0, 128, 255));     // Make the JLabel text blue since it is editable
            valLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {  // Add listener so when JLabel clicked, JTextField appears
                    super.mouseClicked(e);
                    editField.setText(valLabel.getText());  // Set the JTextField's initial text to the JLabel's current text
                    add(editField,1);                 // Add the edit field
                    remove(valLabel);                       // In place of the info
                    editField.requestFocus();               // Give it focus
                    if (editField.getText().equals("Not Available")) {  // If the factor had no valid initial value
                        editField.setText("");      // Clear the edit field's initial value
                    }
                    revalidate();              // Refresh the view
                    repaint();
                }
            });
        }

        value = value.replaceAll("[^-?0-9A-Za-z./]", "");     // Change the value to only numbers and dates
        System.out.println(value);
        try {       // This will succeed if the date is invalid or the value is a number
            if (value.equals("12/31/1969") || Double.parseDouble(value) <= 0) {     // If an invalid date or a negative number or zero
                valLabel.setText("Not Available");  // Set the text to reflect the invalidity
            }
        } catch (NumberFormatException ex) {    // If this exception is thrown, nothing needs to be done
            // Date already checked so just continue
        }
        add(valLabel, 1);   // Add the info to the second column of the row
    }

    /**
     * Get the editable textfield object
     * @return the editfield object
     */
    public JTextField getEditField() {
        return editField;
    }

}
