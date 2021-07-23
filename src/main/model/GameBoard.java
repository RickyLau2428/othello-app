package model;

import exceptions.IllegalCursorException;
import java.util.*;

import static model.State.*;

public class GameBoard {
    public static final int COLUMNS = 8;
    public static final int ROWS = 8;
    public static final int BOARD_SIZE = COLUMNS * ROWS;

    private List<GamePiece> board;
    private State turn;
    private HashMap<Integer, List<GamePiece>> validMoves;
    private Cursor cursor;
    private boolean isGameOver;
    private int whitePieceCount;
    private int blackPieceCount;
    private int gameOverCounter;

    // EFFECTS: Constructs a new game board in the starting configuration (black goes first)
    public GameBoard() {
        board = new LinkedList<>();
        for (int i = 0; i <= 63; i++) {
            board.add(new GamePiece(i, EMPTY));
        }
        setUpGame();
        turn = BLACK;
        validMoves = new HashMap<>();
        cursor = new Cursor(0);
        isGameOver = false;
        whitePieceCount = 0;
        blackPieceCount = 0;
        gameOverCounter = 0;
        setValidMoves();
    }

    // getters
    public List<GamePiece> getBoard() {
        return this.board;
    }

    public HashMap<Integer, List<GamePiece>> getValidMoves() {
        return this.validMoves;
    }

    public State getTurn() {
        return this.turn;
    }

    public int getWhitePieceCount() {
        return whitePieceCount;
    }

    public int getBlackPieceCount() {
        return blackPieceCount;
    }

    public boolean isGameOver() {
        return this.isGameOver;
    }

    // setters:
    public void setTurn(State state) {
        this.turn = state;
        setValidMoves();
    }

    // MODIFIES: this
    // EFFECTS: Sets up the board in the starting configuration
    public void setUpGame() {
        board.get(27).setState(WHITE);
        board.get(28).setState(BLACK);
        board.get(35).setState(BLACK);
        board.get(36).setState(WHITE);
    }

    // MODIFIES: this
    // EFFECTS: Counts the total number of black and white pieces on the board.
    //          Returns the winner of the match, or EMPTY if it is a tie.
    public State endGame() {
        for (GamePiece piece : board) {
            if (piece.getState().equals(WHITE)) {
                whitePieceCount++;
            } else if (piece.getState().equals(BLACK)) {
                blackPieceCount++;
            }
        }
        if (whitePieceCount > blackPieceCount) {
            return WHITE;
        } else if (blackPieceCount > whitePieceCount) {
            return BLACK;
        } else {
            return EMPTY;
        }
    }

    // MODIFIES: this
    // EFFECTS: Ends the game if gameOverCounter == 2
    public void checkGameOver() {
        if (gameOverCounter == 2) {
            isGameOver = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: If no valid moves can be made, increments gameOverCounter by 1 and advances the game to the next turn.
    //          Returns true if there are valid moves, and false otherwise.
    public boolean checkAnyValidMoves() {
        if (validMoves.isEmpty()) {
            gameOverCounter++;
            nextTurn();
            return false;
        }
        return true;
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
    public boolean placePiece(int position) {
        if (validMoves.containsKey(position)) {
            board.get(position).setState(turn);
            for (GamePiece piece : validMoves.get(position)) {
                piece.flip();
            }
            nextTurn();
            return true;
        }
        return false;
    }

    // MODIFIES: this
    // EFFECTS: Modifies validMoves such that the key represents a position on the board and the corresponding
    //          value is a set of GamePieces that are flipped on a potential play. Sets validMoves to be empty
    //          if there are no valid moves.
    public void setValidMoves() {
        validMoves = new HashMap<>();

        for (GamePiece piece : board) {
            if (piece.getState().equals(EMPTY) || !(piece.getState().equals(turn))) {
                continue;
            } else {
                try {
                    cursor.setPosition(piece.getPosition());
                    checkAllDirections(turn);
                } catch (IllegalCursorException e) {
                    cursor.reset();
                }
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Checks in all directions  and modifies validMoves as needed
    private void checkAllDirections(State turn) {
        for (int i = 0; i <= 8; i++) {
            checkDirections(turn, i);
        }
    }

    // MODIFIES: this
    // EFFECTS: Checks all pieces leading from cursor and adds any valid positions to validMoves
    public void checkDirections(State turn, int direction) {
        try {
            List<GamePiece> potentialFlips = new ArrayList<>();
            moveCursorDirection(direction);
            GamePiece toCheck = board.get(cursor.getCurrent());

            while (!(toCheck.getState().equals(turn)) && !(toCheck.getState().equals(EMPTY))) {
                potentialFlips.add(toCheck);
                moveCursorDirection(direction);
                toCheck = board.get(cursor.getCurrent());
            }

            if (toCheck.getState().equals(EMPTY) && !(potentialFlips.isEmpty())) {
                if (validMoves.containsKey(toCheck.getPosition())) {
                    validMoves.get(toCheck.getPosition()).addAll(potentialFlips);
                } else {
                    validMoves.put(toCheck.getPosition(), potentialFlips);
                }
            }
            cursor.reset();
        } catch (IllegalCursorException e) {
            cursor.reset();
        }
    }

    // MODIFIES: cursor
    // EFFECTS: Calls the appropriate cursor movement method based on dir (1 calls moveCursorRight, moving
    //          clockwise, such that 8 will call moveCursorUpperRight)
    private void moveCursorDirection(int dir) throws IllegalCursorException {
        switch (dir) {
            case 1:
                cursor.moveCursorRight();
                break;
            case 2:
                cursor.moveCursorLowerRight();
                break;
            case 3:
                cursor.moveCursorDown();
                break;
            case 4:
                cursor.moveCursorLowerLeft();
                break;
            case 5:
                cursor.moveCursorLeft();
                break;
            case 6:
                cursor.moveCursorUpperLeft();
                break;
            case 7:
                cursor.moveCursorUp();
                break;
            case 8:
                cursor.moveCursorUpperRight();
                break;
        }
    }
}
