package ui.info;

import model.GameBoard;
import ui.BoardRender;

import javax.swing.*;
import java.awt.*;

// Represents a container that houses the score, turn and get help utilities
public class InfoPanel extends JPanel {
    public static final int SECTION_WIDTH = 480;
    public static final int SECTION_HEIGHT = 160;

    private GameBoard game;
    private ScorePanel scorePanel;
    private TurnPanel turnPanel;
    private HelpButton helpButton;

    // EFFECTS: Creates a panel that houses and display information on turn, score and for getting help
    public InfoPanel(GameBoard game, BoardRender br) {
        super();
        initializeContainer();
        this.game = game;
        turnPanel = new TurnPanel(this.game);
        scorePanel = new ScorePanel(this.game);
        helpButton = new HelpButton(this.game, br);
        add(turnPanel);
        add(scorePanel);
        add(helpButton);
        setVisible(true);
    }

    // getters:
    public ScorePanel getScorePanel() {
        return this.scorePanel;
    }

    public TurnPanel getTurnPanel() {
        return this.turnPanel;
    }

    public HelpButton getHelpButton() {
        return this.helpButton;
    }

    // MODIFIES: this
    // EFFECTS: Sets the dimensions and layout of the housing panel
    private void initializeContainer() {
        Dimension size = new Dimension(SECTION_WIDTH, 3 * SECTION_HEIGHT);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(size);
        setMinimumSize(size);
    }
}
