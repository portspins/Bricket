package party.bricket.team7;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;

public abstract class BricketPanelFactory {

    public static JPanel createSearchResultPanel(SearchResult res) throws IOException {
        JPanel result = new JPanel();
        JLabel thumb = new JLabel();
        URL url = new URL(res.getThumbnailLink());
        Image image = ImageIO.read(url);
        Image newImage = image.getScaledInstance(50, 40,  java.awt.Image.SCALE_SMOOTH);
        JLabel name = new JLabel("  " + res.getId() + " " + res.getName());
        result.setLayout(new BorderLayout());
        thumb.setIcon(new ImageIcon(newImage));
        result.add(thumb, BorderLayout.LINE_START);
        result.add(name, BorderLayout.CENTER);
        result.add(new JLabel(res.getReleaseYear().toString() + " "), BorderLayout.LINE_END);
        result.setMaximumSize(new Dimension(335, 50));
        result.setAlignmentY(Component.TOP_ALIGNMENT);
        result.setBorder(BorderFactory.createEmptyBorder(2,2,2,0));
        return result;
    }

    // Add stuff from constructor



}
