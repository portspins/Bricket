package party.bricket.team7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InfoPanelItem extends JPanel {
    private JLabel label;
    private Component info;
    private JTextField editField;

    InfoPanelItem() {
        super();
        setLayout(new GridLayout(1,2, 8,8));
        setMaximumSize(new Dimension(360, 20));
    }

    InfoPanelItem(String label, String value, boolean editable) {
        this();
        setLabel(label);
        setInfo(value, editable);
    }

    public void setLabel(String label) {
        this.label = new JLabel(label);
        add(this.label, 0);
    }

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
        if (value.equals("-1") || value.equals("12/31/1969") || value.equals("-0")) {
            valLabel.setText("Not Available");
        }
        add(valLabel, 1);
    }

    public JTextField getEditField() {
        return editField;
    }

}
