package model;

import exceptions.IllegalCursorException;

// Handles movement through the board
public class Cursor {
    private int original;
    private int current;

    // EFFECTS: Initializes a cursor at the given position
    public Cursor(int position) {

    }

    // getters:
    public int getCurrent() {
        return 0;
    }

    public int getOriginal() {
        return 0;
    }

    public void setCurrent(int postion) {

    }

    // MODIFIES: this
    // EFFECTS: Sets both this.original and this.current to position
    public void setPosition(int position) throws IllegalCursorException {
    }

    // MODIFIES: this
    // EFFECTS: Resets this.current to the original position
    public void reset() {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the right by one unit
    public void moveCursorRight() throws IllegalCursorException {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the left by one unit
    public void moveCursorLeft() throws IllegalCursorException {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor down by one unit
    public void moveCursorDown() throws IllegalCursorException {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor up by one unit
    public void moveCursorUp() throws IllegalCursorException {

    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent upper right corner
    public void moveCursorUpperRight() throws IllegalCursorException {
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent bottom right corner
    public void moveCursorLowerRight() throws IllegalCursorException {
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent upper left corner
    public void moveCursorUpperLeft() throws IllegalCursorException {
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent lower left corner
    public void moveCursorLowerLeft() throws IllegalCursorException {
    }
}
