package model;

import exceptions.IllegalCursorException;

import static model.GameBoard.BOARD_SIZE;
import static model.GameBoard.SIDE_LENGTH;

// Represents a cursor on the board - retains its original position, but can also check surrounding squares.
public class Cursor {
    private int original;
    private int current;

    // EFFECTS: Initializes a cursor at the given position
    public Cursor(int position) {
        original = position;
        current = position;
    }

    // getters
    public int getCurrent() {
        return current;
    }

    public int getOriginal() {
        return original;
    }

    // setters
    public void setCurrent(int position) {
        current = position;
    }

    // MODIFIES: this
    // EFFECTS: Sets the entirety of the cursor to position. Throws IllegalCursorException if
    //          the cursor attempts to move outside of the board
    public void setPosition(int position) throws IllegalCursorException {
        if (!(0 <= position && position < BOARD_SIZE)) {
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
    // EFFECTS: Moves the cursor to the right by one unit and throws an IllegalCursorException
    //          if it attempts to move to the next row or outside the bounds of the board
    public void moveCursorRight() throws IllegalCursorException {
        int newPosition = current + 1;
        if (newPosition >= BOARD_SIZE || (newPosition % SIDE_LENGTH == 0)) {
            throw new IllegalCursorException();
        }
        current = newPosition;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the left by one unit and throws an IllegalCursorException if
    //          it attempts to move to the row above or outside the bounds of the board
    public void moveCursorLeft() throws IllegalCursorException {
        int newPosition = current - 1;
        if (newPosition < 0 || (newPosition % SIDE_LENGTH == (SIDE_LENGTH - 1))) {
            throw new IllegalCursorException();
        }
        current = newPosition;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor down by one unit and throws an IllegalCursorException if
    //          it attempts to move outside the bounds of the board
    public void moveCursorDown() throws IllegalCursorException {
        int newPosition = current + SIDE_LENGTH;
        if (newPosition >= BOARD_SIZE) {
            throw new IllegalCursorException();
        }
        current = newPosition;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor up by one unit and throws an IllegalCursorException if
    //          it attempts to move outside the bounds of the board
    public void moveCursorUp() throws IllegalCursorException {
        int newPosition = current - SIDE_LENGTH;
        if (newPosition < 0) {
            throw new IllegalCursorException();
        }
        current = newPosition;
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent upper right corner and throws an IllegalCursorException
    //          if it attempts to move outside the bounds of the board
    public void moveCursorUpperRight() throws IllegalCursorException {
        moveCursorUp();
        moveCursorRight();
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent bottom right corner and throws an IllegalCursorException
    //          if it attempts to move outside the bounds of the board
    public void moveCursorLowerRight() throws IllegalCursorException {
        moveCursorDown();
        moveCursorRight();
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent upper left corner and throws an IllegalCursorException
    //          if it attempts to move outside the bounds of the board
    public void moveCursorUpperLeft() throws IllegalCursorException {
        moveCursorUp();
        moveCursorLeft();
    }

    // MODIFIES: this
    // EFFECTS: Moves the cursor to the adjacent lower left corner and throws an IllegalCursorException
    //          if it attempts to move outside the bounds of the board
    public void moveCursorLowerLeft() throws IllegalCursorException {
        moveCursorDown();
        moveCursorLeft();
    }
}
