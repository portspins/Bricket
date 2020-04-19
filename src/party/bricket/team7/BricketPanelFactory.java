package party.bricket.team7;

import javax.imageio.ImageIO;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;

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

    public static JPanel createResearchResultPanel(ResearchResult res, BricketView view) throws IOException {
        JPanel result = new JPanel();
        JPanel setPhotoPanel = new JPanel();
        JPanel infoPanel = new JPanel();
        JLabel setPhoto = new JLabel();
        int elCounter = 0;
        URL url = new URL(res.getImageLink());
        Image image = ImageIO.read(url);
        Image newImage = image.getScaledInstance(410,350, Image.SCALE_SMOOTH);

        JLabel name = new JLabel(res.getID() + " " + res.getName());
        name.setBorder(BorderFactory.createEmptyBorder(7,10,0,0));
        name.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        setPhotoPanel.setLayout(new BoxLayout(setPhotoPanel, BoxLayout.Y_AXIS));
        setPhotoPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        infoPanel.add(new InfoPanelItem("ID Number:", res.getID(), false));
        infoPanel.add(new InfoPanelItem("Name:", res.getName(), false));
        infoPanel.add(new InfoPanelItem("Theme:", res.getTheme(), false));
        Calendar releaseDate = res.getReleaseDate();
        String relDate = (releaseDate.get(Calendar.MONTH) + 1) + "/" + releaseDate.get(Calendar.DAY_OF_MONTH) + "/" + releaseDate.get(Calendar.YEAR);
        InfoPanelItem releasePanel = new InfoPanelItem("Date Released:", relDate, true);
        infoPanel.add(releasePanel);
        JTextField releaseEdit = releasePanel.getEditField();
        releaseEdit.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        view.submitReleaseDate(releaseEdit.getText());
                    } catch (ParseException ex) {
                        try {
                            view.submitReleaseDate(relDate);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                    }
                }
            }
        });
        Calendar retireDate = res.getRetireDate();
        infoPanel.add(new InfoPanelItem("Date Retired:", (retireDate.get(Calendar.MONTH) + 1) + "/" + retireDate.get(Calendar.DAY_OF_MONTH) + "/" + retireDate.get(Calendar.YEAR), true));
        infoPanel.add(new InfoPanelItem("Part Count:", Integer.toString(res.getPartCount()), true));
        result.setLayout(new BorderLayout());
        setPhoto.setIcon(new ImageIcon(newImage));
        result.add(name, BorderLayout.PAGE_START);
        setPhotoPanel.add(setPhoto);
        result.add(setPhotoPanel, BorderLayout.LINE_START);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20,0,0,10));
        result.add(infoPanel, BorderLayout.LINE_END);
        result.setMaximumSize(new Dimension(1200, 100));
        result.setBorder(BorderFactory.createEmptyBorder(2,2,2,7));
        result.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                result.requestFocus();
            }
        });
        return result;
    }


}
