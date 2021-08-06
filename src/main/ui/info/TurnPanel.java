package ui.info;

import model.State;
import ui.OthelloGame;

import javax.swing.*;

import java.awt.*;

import static model.State.*;

public class TurnPanel extends Information {
    private JLabel currTurn;

    public TurnPanel(OthelloGame game, State turn) {
        super(game, "Current turn: ");
        initializeTurnLabel(turn);
    }

    public void nextTurnLabel() {
        if (currTurn.getText().equals("Clear")) {
            currTurn.setText("Fill");
        } else if (currTurn.getText().equals("Fill")) {
            currTurn.setText("Clear");
        }
    }

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
