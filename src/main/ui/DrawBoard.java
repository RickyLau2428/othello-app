package ui;

import model.GameBoard;
import model.GamePiece;

import java.util.List;

import static model.GameBoard.SIDE_LENGTH;

// Represents a rendering of a current board state on a console
public class DrawBoard {
    public static final String CLEAR_CIRCLE = "⭘";
    public static final String FILLED_CIRCLE = "●";
    public static final String LINE = "+---";
    public static final String SIDE_LINE = "|";

    private final List<GamePiece> board;

    // EFFECTS: Constructs a drawer with access to the game board
    public DrawBoard(List<GamePiece> board) {
        this.board = board;
    }

    // EFFECTS: prints the entire board to the console
    public void printBoard() {
        printLetters();
        for (int i = 0; i < GameBoard.SIDE_LENGTH; i++) {
            printLine();
            printNumber(i);
            for (int j = 0; j < SIDE_LENGTH; j++) {
                System.out.print(SIDE_LINE + " " + retrievePiece(GameBoard.SIDE_LENGTH * i + j) + " ");
            }
            System.out.println(SIDE_LINE);
        }
        printLine();
    }

    // EFFECTS: Returns the appropriate character to console based on contents of board at
    //          positions: " " for EMPTY, FILL_CIRCLE for FILL, CLEAR_CIRCLE for CLEAR
    private String retrievePiece(int position) {
        switch (board.get(position).getState()) {
            case CLEAR:
                return CLEAR_CIRCLE;
            case FILL:
                return FILLED_CIRCLE;
            default:
                return " ";
        }
    }

    // EFFECTS: Prints number denominations along the side of the board
    private void printNumber(int num) {
        System.out.print(" " + (num + 1) + " ");
    }

    // EFFECTS: Prints a horizontal line spanning the board
    private void printLine() {
        printTab();
        for (int i = 0; i < SIDE_LENGTH; i++) {
            System.out.print(LINE);
        }
        System.out.println("+");
    }

    // EFFECTS: Prints letters starting from 'A' across the top of the board
    private void printLetters() {
        printTab();
        int start = 'A';
        char toPrint;
        for (int i = 0; i < SIDE_LENGTH; i++) {
            toPrint = (char) (start + i);
            System.out.print("  " + toPrint + " ");
        }
        System.out.println();
    }

    // EFFECTS: Prints three blank spaces to the console
    private void printTab() {
        System.out.print("   ");
    }
}
