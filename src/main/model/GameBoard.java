package model;

import org.json.JSONArray;
import persistence.Writable;
import exceptions.IllegalCursorException;
import org.json.JSONObject;

import java.util.*;

import static model.State.*;

// Represents a game of Othello played on a square board with side length SIDE_LENGTH. Each square on the board is
// represented by a number ranging from 0 to 63, increasing from left to right and from top to bottom.
public class GameBoard implements Writable {
    // INVARIANT: SIDE_LENGTH must be 8
    public static final int SIDE_LENGTH = 8;
    public static final int BOARD_SIZE = SIDE_LENGTH * SIDE_LENGTH;
    public static final int MARGIN = SIDE_LENGTH / 2 - 1;
    // Q1 through Q4 are the corners closest to the origin corresponding to their
    // quadrants on a board with length SIDE_LENGTH
    public static final int Q1 = (SIDE_LENGTH * MARGIN) + (MARGIN + 1);
    public static final int Q2 = (SIDE_LENGTH * MARGIN) + MARGIN;
    public static final int Q3 = (SIDE_LENGTH * (MARGIN + 1)) + MARGIN;
    public static final int Q4 = (SIDE_LENGTH * (MARGIN + 1)) + (MARGIN + 1);

    private Map<Integer, GamePiece> board;
    private State turn;
    private Map<Integer, Set<GamePiece>> validMoves;
    private Cursor cursor;
    private boolean isGameOver;
    private int clearPieceCount;
    private int fillPieceCount;
    private int gameOverCounter;
    private Set<GamePiece> playedPieces;

    // EFFECTS: Constructs a new game board in the starting configuration (fill goes first)
    public GameBoard() {
        // initialCapacity set to 86 as to avoid any rehashing
        board = new HashMap<>(86);
        turn = FILL;
        validMoves = new HashMap<>();
        cursor = new Cursor(0);
        isGameOver = false;
        clearPieceCount = 0;
        fillPieceCount = 0;
        gameOverCounter = 0;
        playedPieces = new HashSet<>();

        setUpGame();
        setValidMoves();
    }

    // EFFECTS: Initializes a board loaded from file
    public GameBoard(State turn, int clearPieces, int fillPieces, int gameOverCount) {
        this.turn = turn;
        clearPieceCount = clearPieces;
        fillPieceCount = fillPieces;
        gameOverCounter = gameOverCount;

        // initialCapacity set to 86 as to avoid any rehashing
        board = new HashMap<>(86);
        validMoves = new HashMap<>();
        cursor = new Cursor(0);
        isGameOver = false;

        setValidMoves();
    }

    // getters
    public Map<Integer, GamePiece> getBoard() {
        return this.board;
    }

    public Map<Integer, Set<GamePiece>> getValidMoves() {
        return this.validMoves;
    }

    public Set<Integer> getValidMoveKeys() {
        return this.validMoves.keySet();
    }

    public State getTurn() {
        return this.turn;
    }

    public int getClearPieceCount() {
        return clearPieceCount;
    }

    public int getFillPieceCount() {
        return fillPieceCount;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public int getGameOverCounter() {
        return gameOverCounter;
    }

    // setters
    public void setGameOverCounter(int num) {
        this.gameOverCounter = num;
    }

    // MODIFIES: this
    // EFFECTS: Sets the current turn and generates all currently valid moves
    public void setTurn(State state) {
        this.turn = state;
        setValidMoves();
    }

    // MODIFIES: this
    // EFFECTS: Updates the state of the board and checks if the game is over.
    public void update() {
        if (!checkAnyValidMoves()) {
            checkGameOver();
            if (gameOverCounter == 1) {
                update();
            }
        } else {
            setGameOverCounter(0);
        }
    }

    // MODIFIES: this
    // EFFECTS: Sets the piece counter for the given state to num.
    public void setPieceCount(State state, int num) {
        if (state.equals(FILL)) {
            fillPieceCount = num;
        } else {
            clearPieceCount = num;
        }
    }

    // MODIFIES: this
    // EFFECTS: Sets up the board in the starting configuration.
    public void setUpGame() {
        board.put(Q1, new GamePiece(Q1, FILL));
        board.put(Q2, new GamePiece(Q2, CLEAR));
        board.put(Q3, new GamePiece(Q3, FILL));
        board.put(Q4, new GamePiece(Q4, CLEAR));
        clearPieceCount = 2;
        fillPieceCount = 2;
    }

    // MODIFIES: this
    // EFFECTS: Returns the winner of the match, or null if it is a tie.
    public String declareVictor() {
        String victor = null;
        if (clearPieceCount > fillPieceCount) {
            victor = "clear";
        } else if (fillPieceCount > clearPieceCount) {
            victor = "fill";
        }
        return victor;
    }

    // MODIFIES: this
    // EFFECTS: Ends the game if two consecutive turns have no valid moves.
    public void checkGameOver() {
        if (gameOverCounter == 2) {
            isGameOver = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: If no valid moves can be made, increments gameOverCounter by 1, advances the game to the next turn
    //          and returns false. Does nothing and returns true if there are valid moves.
    public boolean checkAnyValidMoves() {
        boolean anyValidMoves = true;
        if (validMoves.isEmpty()) {
            gameOverCounter++;
            nextTurn();
            anyValidMoves = false;
        }
        return anyValidMoves;
    }

    // MODIFIES: this
    // EFFECTS: Advances the game to the next player's turn and stores any valid moves.
    public void nextTurn() {
        if (turn.equals(FILL)) {
            turn = CLEAR;
        } else {
            turn = FILL;
        }
        setValidMoves();
    }

    // MODIFIES: this
    // EFFECTS: If valid, places a game piece at position, flips other game pieces as needed and updates the fill
    //          and clear piece counters. Advances the game to the next turn.
    //          Returns true if successful, false otherwise.
    public boolean placePiece(int position) {
        boolean isPiecePlaced = false;
        if (validMoves.containsKey(position)) {
            GamePiece toPlace = new GamePiece(position, turn);
            board.put(position, toPlace);
            for (GamePiece piece : validMoves.get(position)) {
                piece.flip();
            }

            if (turn.equals(FILL)) {
                fillPieceCount += (validMoves.get(position).size() + 1);
                clearPieceCount -= validMoves.get(position).size();
            } else {
                fillPieceCount -= validMoves.get(position).size();
                clearPieceCount += (validMoves.get(position).size() + 1);
            }

            playedPieces = validMoves.get(position);
            playedPieces.add(toPlace);
            nextTurn();
            isPiecePlaced = true;
        }

        return isPiecePlaced;
    }

    // MODIFIES: this
    // EFFECTS: Scans the entire board and adds any valid moves to validMoves
    public void setValidMoves() {
        // Clears validMoves for every new board state
        validMoves = new HashMap<>();

        for (GamePiece piece : board.values()) {
            if (piece.getState().equals(turn)) {
                cursor.setPosition(piece.getPosition());
                checkAllDirections(turn);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Checks for valid moves in all directions, using the current player's pieces as anchor points.
    private void checkAllDirections(State turn) {
        for (int i = 1; i <= 8; i++) {
            checkDirection(turn, i);
        }
    }

    // MODIFIES: this
    // EFFECTS: Checks all pieces leading from cursor and adds any valid moves to validMoves. Resets the cursor
    //          if it attempts to move illegally.
    public void checkDirection(State turn, int direction) {
        try {
            Set<GamePiece> potentialFlips = new HashSet<>();
            moveCursorDirection(direction);
            GamePiece toCheck = board.get(cursor.getCurrent());

            // The entry being checked must be of the opposite state
            while (toCheck != null && !(toCheck.getState().equals(turn))) {
                potentialFlips.add(toCheck);
                moveCursorDirection(direction);
                toCheck = board.get(cursor.getCurrent());
            }

            // The entry being checked must hold an empty piece, and there are opposing pieces that would be flipped
            // if a piece is placed at that entry
            if (toCheck == null && !(potentialFlips.isEmpty())) {
                if (validMoves.containsKey(cursor.getCurrent())) {
                    // Adds the potential flips to the corresponding key if it already exists in validMoves
                    validMoves.get(cursor.getCurrent()).addAll(potentialFlips);
                } else {
                    // Adds a key-value pair to validMoves if the key does not already exist
                    validMoves.put(cursor.getCurrent(), potentialFlips);
                }
            }
            // Resets the cursor for the next iteration of checkDirections
            cursor.reset();
        } catch (IllegalCursorException e) {
            cursor.reset();
        }
    }

    // MODIFIES: this
    // EFFECTS: Calls the appropriate cursor movement method based on dir (1 calls moveCursorRight, moving
    //          clockwise, such that 8 will call moveCursorUpperRight).
    private void moveCursorDirection(int dir) throws IllegalCursorException {
        switch (dir) {
            case 1:
                cursor.moveCursorRight();
                break;
            case 2:
                cursor.moveCursorDown();
                break;
            case 3:
                cursor.moveCursorLeft();
                break;
            case 4:
                cursor.moveCursorUp();
                break;
            default:
                moveCursorDirectionDiagonal(dir);

        }
    }

    // MODIFIES: this
    // EFFECTS: Calls the appropriate cursor movement method based on dir (only called on diagonal
    //          directions) - will only be called for dir = 5 to dir = 8.
    private void moveCursorDirectionDiagonal(int dir) throws IllegalCursorException {
        switch (dir) {
            case 5:
                cursor.moveCursorLowerRight();
                break;
            case 6:
                cursor.moveCursorLowerLeft();
                break;
            case 7:
                cursor.moveCursorUpperRight();
                break;
            default:
                cursor.moveCursorUpperLeft();
        }
    }

    // EFFECTS: Returns this as a JSON Object
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("turn", turn);
        json.put("clearPieceCount", clearPieceCount);
        json.put("fillPieceCount", fillPieceCount);
        json.put("gameOverCount", gameOverCounter);
        json.put("pieces", piecesToJson());
        return json;
    }

    // EFFECTS: Returns this.board as a JSON Array
    private JSONArray piecesToJson() {
        JSONArray jsonArray = new JSONArray();

        for (GamePiece piece : board.values()) {
            jsonArray.put(piece.toJson());
        }

        return jsonArray;
    }
}
