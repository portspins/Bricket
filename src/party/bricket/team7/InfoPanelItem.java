package party.bricket.team7;

import javax.swing.*;
import java.awt.*;

public class InfoPanelItem extends JPanel {
    private JLabel label;
    private Component info;

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
        if (value.equals("-1") || value.equals("12/31/1969") || value.equals("")) {
            add(new JLabel("Not Available"), 1);
        } else {
            if (editable) {
                JTextField editField = new JTextField(value);
                editField.setMaximumSize(editField.getPreferredSize());
                add(editField,1);
            } else {
                add(new JLabel(value), 1);
            }
        }
    }
}
