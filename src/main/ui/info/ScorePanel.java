package ui.info;

import model.GameBoard;

import javax.swing.*;
import java.awt.*;

// Container for information on current score in GUI
public class ScorePanel extends Information {
    public static final String CLEAR_SCORE_STARTER = "Clear's Score: ";
    public static final String FILL_SCORE_STARTER = "Fill's Score: ";

    private JLabel fillScore;
    private JLabel clearScore;

    // EFFECTS: Initializes a score panel that has a title and scores
    public ScorePanel(GameBoard game) {
        super(game, "Current score: ");
        initializeScores();
    }

    // MODIFIES: this
    // EFFECTS: Updates the text rendering of state's score to be count
    public void setTextScore(int fillPieceScore, int clearPieceScore) {
        fillScore.setText(FILL_SCORE_STARTER + fillPieceScore);
        clearScore.setText(CLEAR_SCORE_STARTER + clearPieceScore);
    }

    // MODIFIES: this
    // EFFECTS: Initializes the scores with the values at start
    private void initializeScores() {
        fillScore = new JLabel(CLEAR_SCORE_STARTER + 2);
        clearScore = new JLabel(FILL_SCORE_STARTER + 2);
        fillScore.setFont(new Font("Arial", Font.PLAIN, 18));
        clearScore.setFont(new Font("Arial", Font.PLAIN, 18));
        container.setOpaque(true);
        container.setBackground(Color.WHITE);
        container.add(fillScore);
        container.add(clearScore);
    }

    // MODIFIES: this
    // EFFECTS: Creates the container that will hold both clear and fill scores
    @Override
    protected void initializeContainer() {
        super.initializeContainer();
        container.setLayout(new GridLayout(0, 2));
    }
}
