package model;

import exceptions.IllegalCursorException;

// Handles movement through the board
public class Cursor {
    private int original;
    private int current;

    // EFFECTS: Initializes a cursor at the given position
    public Cursor(int position) {
        original = position;
        current = position;
    }

    // getters:
    public int getCurrent() {
        return current;
    }

    public int getOriginal() {
        return original;
    }

    public void setCurrent(int position) {
        current = position;
    }

    // MODIFIES: this
    // EFFECTS: Sets both this.original and this.current to position
    public void setPosition(int position) throws IllegalCursorException {
        if (!(0 <= position && position < 64)) {
            throw new IllegalCursorException();
        }
        original = position;
        current = position;
    }

    // MODIFIES: this
    // EFFECTS: Resets current to the original position
    public void reset() {
        current = original;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the right by one unit
    public void moveCursorRight() throws IllegalCursorException {
        int newPosition = current + 1;
        if (newPosition > 63 || (newPosition % 8 == 0)) {
            throw new IllegalCursorException();
        }
        current = newPosition;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the left by one unit
    public void moveCursorLeft() throws IllegalCursorException {
        int newPosition = current - 1;
        if (newPosition < 0 || (newPosition % 8 == 7)) {
            throw new IllegalCursorException();
        }
        current = newPosition;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor down by one unit
    public void moveCursorDown() throws IllegalCursorException {
        int newPosition = current + 8;
        if (newPosition >= 64) {
            throw new IllegalCursorException();
        }
        current = newPosition;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor up by one unit
    public void moveCursorUp() throws IllegalCursorException {
        int newPosition = current - 8;
        if (newPosition < 0) {
            throw new IllegalCursorException();
        }
        current = newPosition;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent upper right corner
    public void moveCursorUpperRight() throws IllegalCursorException {
        moveCursorUp();
        moveCursorRight();
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent bottom right corner
    public void moveCursorLowerRight() throws IllegalCursorException {
        moveCursorDown();
        moveCursorRight();
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent upper left corner
    public void moveCursorUpperLeft() throws IllegalCursorException {
        moveCursorUp();
        moveCursorLeft();
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent lower left corner
    public void moveCursorLowerLeft() throws IllegalCursorException {
        moveCursorDown();
        moveCursorLeft();
    }
}
