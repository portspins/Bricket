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
    private Component info;
    private JTextField editField;

    /**
     * Constructor
     */
    InfoPanelItem() {
        super();
        setLayout(new GridLayout(1,2, 8,8));
        setMaximumSize(new Dimension(360, 27));
    }

    /**
     * Parametrized constructor
     * @param label the label defining what information is presented
     * @param value the initial value corresponding to the label
     * @param editable true if the value should be able to be edited
     */
    InfoPanelItem(String label, String value, boolean editable) {
        this();
        setLabel(label);
        setInfo(value, editable);
    }

    /**
     * Set the label for the panel
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = new JLabel(label);
        add(this.label, 0);
    }

    /**
     * Set the value and allow it to be edited if requested
     * @param value the initial value
     * @param editable true if the value should be able to be edited
     */
    public void setInfo(String value, boolean editable) {
        final JLabel valLabel = new JLabel(value);
        if (editable) {
            editField = new JTextField(value);
            editField.setMaximumSize(editField.getPreferredSize());
            editField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusLost(FocusEvent e) {
                    super.focusLost(e);
                    add(valLabel,1);
                    remove(editField);
                    revalidate();
                    repaint();
                }
            });
            valLabel.setForeground(new Color(0, 128, 255));
            valLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    editField.setText(valLabel.getText());
                    add(editField,1);
                    editField.requestFocus();
                    if (editField.getText().equals("Not Available")) {
                        editField.setText("");
                    }
                    remove(valLabel);
                    revalidate();
                    repaint();
                }
            });
        }

        value = value.replaceAll("[^-?0-9./]", "");
        System.out.println(value);
        try {
            if (value.equals("12/31/1969") || Double.parseDouble(value) <= 0) {
                valLabel.setText("Not Available");
            }
        } catch (NumberFormatException ex) {
            // Date already checked so just continue
        }
        add(valLabel, 1);
    }

    public JTextField getEditField() {
        return editField;
    }

}
