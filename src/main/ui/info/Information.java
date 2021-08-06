package ui.info;

import ui.OthelloGame;

import javax.swing.*;
import java.awt.*;

public abstract class Information extends JPanel {
    protected OthelloGame game;
    protected JLabel title;
    protected JPanel container;

    public Information(OthelloGame game, String titleText) {
        this.game = game;
        setPreferredSize(new Dimension(InfoPanel.SECTION_WIDTH, InfoPanel.SECTION_HEIGHT));
        setBackground(Color.WHITE);
        initializeTitle(titleText);
        initializeContainer();
        setLayout(null);
        add(title);
        add(container);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Creates a title showing text in Sans Serif font surrounded by a border
    protected void initializeTitle(String text) {
        title = new JLabel(text);
        title.setFont(new Font("SansSerif", Font.BOLD, 16));
        title.setBounds(0, 0, 300, 50);
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    // MODIFIES: this
    // EFFECTS: Creates a container that holds relevant information to the panel
    protected void initializeContainer() {
        container = new JPanel();
        container.setBounds(0, 50, 300, 50);
        container.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
