package model;

import static model.State.*;

// Holds information for each game piece on the board (i.e. its position and current status)
public class GamePiece {
    private State state;
    private final int position;

    // EFFECTS: Initializes a game piece with the given state and position
    public GamePiece(int position, State state) {
        this.position = position;
        this.state = state;
    }

    // getters
    public int getPosition() {
        return this.position;
    }

    public State getState() {
        return this.state;
    }

    // setters
    public void setState(State state) {
        this.state = state;
    }

    // MODIFIES: this
    // EFFECTS: Changes this.state from FILL to CLEAR or vice versa. Does nothing
    //          if state is EMPTY.
    public void flip() {
        if (state.equals(FILL)) {
            state = CLEAR;
        } else {
            state = FILL;
        }
    }
}
