package model;

// Handles movement through the board
public class Cursor {
    private int original;
    private int current;

    // EFFECTS: Initializes a cursor at the given position
    public Cursor() {

    }

    // getters:
    public int getCurrent() {
        return 0;
    }

    // MODIFIES: this
    // EFFECTS: Sets both this.original and this.current to position
    public void setPosition(int position) {
    }

    // MODIFIES: this
    // EFFECTS: Resets this.current to the original position
    public void reset() {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the right by one unit
    public void moveCursorRight() {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the left by one unit
    public void moveCursorLeft() {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor down by one unit
    public void moveCursorDown() {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor up by one unit
    public void moveCursorUp() {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent upper right corner
    public void moveCursorUpperRight() {
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent bottom right corner
    public void moveCursorLowerRight() {
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent upper left corner
    public void moveCursorUpperLeft() {
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent lower left corner
    public void moveCursorLowerLeft() {
    }
}
