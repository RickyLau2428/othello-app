package ui.info;

import model.GameBoard;

import javax.swing.*;
import java.awt.*;

import static ui.info.InfoPanel.*;

// Abstract class for score and turn information panels
public abstract class Information extends JPanel {
    protected GameBoard game;
    protected JLabel title;
    protected JPanel container;

    // EFFECTS: Creates a container that has a title on top of an to-be-specified information panel
    public Information(GameBoard game, String titleText) {
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

    // setters:
    public void setGame(GameBoard game) {
        this.game = game;
    }

    // MODIFIES: this
    // EFFECTS: Creates a title showing text in Sans Serif font surrounded by a border
    protected void initializeTitle(String text) {
        title = new JLabel(text);
        title.setFont(new Font("SansSerif", Font.BOLD, 25));
        title.setBounds(0, 0, SECTION_WIDTH, SECTION_HEIGHT / 2);
        title.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    // MODIFIES: this
    // EFFECTS: Creates a container that holds relevant information to the panel
    protected void initializeContainer() {
        container = new JPanel();
        container.setBounds(0, SECTION_HEIGHT / 2, SECTION_WIDTH, SECTION_HEIGHT / 2);
        container.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
