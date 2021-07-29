package model;

import org.json.JSONObject;
import persistence.Writable;

import static model.State.*;

// Represents a game piece on the board
public class GamePiece implements Writable {
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
    // EFFECTS: Changes the piece's state from FILL to CLEAR or vice versa. Does nothing
    //          if state is EMPTY.
    public void flip() {
        if (state.equals(FILL)) {
            state = CLEAR;
        } else {
            state = FILL;
        }
    }

    @Override
    // EFFECTS: Returns this as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("position", position);
        json.put("state", state);

        return json;
    }
}
