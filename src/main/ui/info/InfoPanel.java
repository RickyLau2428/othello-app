package ui.info;

import ui.BoardRender;
import ui.OthelloGame;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    public static final int SECTION_WIDTH = 300;
    public static final int SECTION_HEIGHT = 100;
    private OthelloGame game;
    private ScorePanel scorePanel;
    private TurnPanel turnPanel;
    private HelpButton hb;

    public InfoPanel(OthelloGame game, BoardRender br) {
        super();
        initializeContainer();
        this.game = game;
        turnPanel = new TurnPanel(this.game, game.getGame().getTurn());
        scorePanel = new ScorePanel(this.game);
        hb = new HelpButton(this.game, br);
        add(turnPanel);
        add(scorePanel);
        add(hb);
        setVisible(true);
    }

    // getters:
    public ScorePanel getScorePanel() {
        return this.scorePanel;
    }

    public TurnPanel getTurnPanel() {
        return this.turnPanel;
    }

    private void initializeContainer() {
        Dimension size = new Dimension(SECTION_WIDTH, 3 * SECTION_HEIGHT);
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(size);
        setMinimumSize(size);
    }
}
