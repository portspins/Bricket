package party.bricket.team7;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
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
        result.setBorder(BorderFactory.createEmptyBorder(2,2,2,7));
        return result;
    }

    // Add stuff from constructor

    public static JPanel createResearchResultPanel(ResearchResult res) throws IOException {
        JPanel result = new JPanel();
        JPanel setPhotoPanel = new JPanel();
        JPanel editable = new JPanel();
        JLabel setPhoto = new JLabel();
        URL url = new URL(res.getImageLink());
        Image image = ImageIO.read(url);
        Image newImage = image.getScaledInstance(420, 350,  java.awt.Image.SCALE_SMOOTH);

        JLabel name = new JLabel(res.getID() + " " + res.getName());
        name.setBorder(BorderFactory.createEmptyBorder(7,10,0,0));
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        editable.setLayout(new BoxLayout(editable, BoxLayout.Y_AXIS));
        setPhotoPanel.setLayout(new BoxLayout(setPhotoPanel, BoxLayout.Y_AXIS));
        setPhotoPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        editable.add(new JLabel("Theme: " + res.getTheme()));
        result.setLayout(new BorderLayout());
        setPhoto.setIcon(new ImageIcon(newImage));
        result.add(name, BorderLayout.PAGE_START);
        setPhotoPanel.add(setPhoto);
        result.add(setPhotoPanel, BorderLayout.LINE_START);
        result.add(editable, BorderLayout.LINE_END);
        result.setMaximumSize(new Dimension(1200, 100));
        result.setBorder(BorderFactory.createEmptyBorder(2,2,2,7));
        return result;
    }


}
