package model;

import java.util.HashMap;
import java.util.Set;

import static model.State.*;

public class GameBoard {
    public static final int COLUMNS = 8;
    public static final int ROWS = 8;
    public static final int BOARD_SIZE = COLUMNS * ROWS;

    private Set<GamePiece> board;
    private State turn;
    private HashMap<Integer, Set<GamePiece>> validMoves;
    private Cursor cursor;
    private boolean isGameOver;
    private int whitePieceCount;
    private int blackPieceCount;
    private int gameOverCounter;

    // EFFECTS: Constructs a new game board in the starting configuration (black goes first)
    public GameBoard() {

    }

    // getters
    public Set<GamePiece> getBoard() {
        return this.board;
    }

    public HashMap<Integer, Set<GamePiece>> getValidMoves() {
        return this.validMoves;
    }

    public State getTurn() {
        return this.turn;
    }

    public int getWhitePieceCount() {
        return 0;
    }

    public int getBlackPieceCount() {
        return 0;
    }

    public boolean isGameOver() {
        return false;
    }

    // setters:
    public void setTurn() {
    }

    // MODIFIES: this
    // EFFECTS: Sets up the board in the starting configuration
    public void setUpGame() {

    }

    // MODIFIES: this
    // EFFECTS: Counts the total number of black and white pieces on the board
    public void endGame() {

    }

    // MODIFIES: this
    // EFFECTS: Ends the game if gameOverCounter == 2
    public void checkGameOver() {

    }

    // MODIFIES: this
    // EFFECTS: If no valid moves can be made, increments gameOverCounter by 1 and advances the game to the next turn.
    //          Returns true if there are valid moves, and false otherwise.
    public boolean checkAnyValidMoves() {
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Progresses the game to the next player's turn
    public void nextTurn() {
        if (turn.equals(BLACK)) {
            turn = WHITE;
        } else if (turn.equals(WHITE)) {
            turn = BLACK;
        }
        setValidMoves();
    }

    // REQUIRES: 0 <= position < BOARD_SIZE
    // MODIFIES: this
    // EFFECTS: places a game piece at position, flips other game pieces as needed and advances the game to the
    //          next turn. Returns true if successful, false otherwise.
    public boolean placePiece() {
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Modifies validMoves such that the key represents a position on the board and the corresponding
    //          value is a set of GamePieces that are flipped on a potential play. Sets validMoves to be empty
    //          if there are no valid moves.
    public void setValidMoves() {

    }

    // MODIFIES: this
    // EFFECTS: Checks in all directions  and modifies validMoves as needed
    private void checkAllDirection(State turn) {

    }

    // MODIFIES: this
    // EFFECTS: Checks all pieces to the right of the cursor and adds any valid positions to validMoves
    public void checkDirections(State turn, int direction) {

    }

    // MODIFIES: cursor
    // EFFECTS: Calls the appropriate cursor movement method based on dir (1 calls moveCursorRight, moving
    //          clockwise, such that 8 will call moveCursorUpperRight)
    private void moveCursorDirection(int dir) {

    }

}
