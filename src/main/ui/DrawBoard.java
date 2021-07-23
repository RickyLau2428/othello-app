package ui;

import model.GamePiece;

import java.util.Set;

public class DrawBoard {
    public static final String WHITE_CIRCLE = "⭘";
    public static final String BLACK_CIRCLE = "●";
    public static final String LINE = "+---";
    public static final String SIDE_LINE = "|";

    private Set<GamePiece> board;

    // EFFECTS: Constructs a drawer with access to the game board
    public DrawBoard(Set<GamePiece> board) {

    }

    // EFFECTS: prints the entire board to the console
    public void printBoard() {

    }

    // EFFECTS: Returns the appropriate character to console based on contents of board at
    //          positions: " " for EMPTY, WHITE_CIRCLE for WHITE, BLACK_CIRCLE for BLACK
    private String retrievePiece(int position) {
        return null;
    }

    // EFFECTS: Prints three blank spaces to the console
    private void printTab() {

    }

    // EFFECTS: Prints number denominations along the top of the board, followed by a newline
    private void printNumber(int num) {

    }

    // EFFECTS: Prints a horizontal line spanning the board
    private void printLine() {

    }

    // EFFECTS: Prints a letter to console that is num positions away from 'A' on the ASCII table
    private void printLetter() {

    }
}
