package ui.info;

import model.GameBoard;
import model.State;

import javax.swing.*;

import java.awt.*;

import static model.State.*;
import static ui.info.InfoPanel.SECTION_HEIGHT;
import static ui.info.InfoPanel.SECTION_WIDTH;

// Container for information on current turn in GUI
public class TurnPanel extends Information {
    private JLabel currTurn;

    // EFFECTS: Initializes the turn panel with the current turn
    public TurnPanel(GameBoard game) {
        super(game, "Current turn: ");
        initializeTurnLabel(game.getTurn());
    }

    // MODIFIES: this
    // EFFECTS: Updates the turn label with the current turn
    public void updateTurnLabel() {
        if (game.getTurn().equals(FILL)) {
            currTurn.setText("Fill");
        } else if (game.getTurn().equals(CLEAR)) {
            currTurn.setText("Clear");
        }
    }

    // MODIFIES: this
    // EFFECTS: Creates a turn label in an appropriate location with Arial font
    private void initializeTurnLabel(State state) {
        if (state.equals(CLEAR)) {
            currTurn = new JLabel("Clear");
        } else if (state.equals(FILL)) {
            currTurn = new JLabel("Fill");
        }
        currTurn.setFont(new Font("Arial", Font.BOLD, 20));
        currTurn.setBounds(2, 0, SECTION_WIDTH, SECTION_HEIGHT / 2);
        container.add(currTurn);
    }

    @Override
    protected void initializeContainer() {
        super.initializeContainer();
        container.setLayout(null);
    }
}
