package ui.info;

import model.State;
import ui.OthelloGame;

import javax.swing.*;

import java.awt.*;

import static model.State.*;

// Container for information on current turn in GUI
public class TurnPanel extends Information {
    private JLabel currTurn;

    // EFFECTS: Initializes the turn panel with the current turn
    public TurnPanel(OthelloGame game, State turn) {
        super(game, "Current turn: ");
        initializeTurnLabel(turn);
    }

    // MODIFIES: this
    // EFFECTS: Advances the turn label to the next turn
    public void nextTurnLabel() {
        if (currTurn.getText().equals("Clear")) {
            currTurn.setText("Fill");
        } else if (currTurn.getText().equals("Fill")) {
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
        currTurn.setFont(new Font("Arial", Font.BOLD, 14));
        currTurn.setBounds(0, 0, 300, 50);
        container.add(currTurn);
    }

    @Override
    protected void initializeContainer() {
        super.initializeContainer();
        container.setLayout(null);
    }
}
