package model;

import org.json.JSONArray;
import persistence.Writable;
import exceptions.IllegalCursorException;
import exceptions.IllegalPlayerInputException;
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

    private List<GamePiece> board;
    private State turn;
    private HashMap<Integer, List<GamePiece>> validMoves;
    private Cursor cursor;
    private boolean isGameOver;
    private int clearPieceCount;
    private int fillPieceCount;
    private int gameOverCounter;

    // EFFECTS: Constructs a new game board in the starting configuration (fill goes first)
    public GameBoard() {
        board = new LinkedList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            board.add(new GamePiece(i, EMPTY));
        }
        turn = FILL;
        validMoves = new HashMap<>();
        cursor = new Cursor(0);
        isGameOver = false;
        clearPieceCount = 0;
        fillPieceCount = 0;
        gameOverCounter = 0;

        setUpGame();
        setValidMoves();
    }

    // EFFECTS: Initializes a board loaded from file
    public GameBoard(State turn, int clearPieces, int fillPieces, int gameOverCount) {
        this.turn = turn;
        clearPieceCount = clearPieces;
        fillPieceCount = fillPieces;
        gameOverCounter = gameOverCount;

        board = new LinkedList<>();
        validMoves = new HashMap<>();
        cursor = new Cursor(0);
        isGameOver = false;

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
    // EFFECTS: Sets the piece counter for the given state to num. If state is EMPTY, does nothing.
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
        board.get(Q1).setState(FILL);
        board.get(Q2).setState(CLEAR);
        board.get(Q3).setState(FILL);
        board.get(Q4).setState(CLEAR);
        clearPieceCount = 2;
        fillPieceCount = 2;
    }

    // MODIFIES: this
    // EFFECTS: Returns the winner of the match, or EMPTY if it is a tie.
    public State declareVictor() {
        State victor = EMPTY;
        if (clearPieceCount > fillPieceCount) {
            victor = CLEAR;
        } else if (fillPieceCount > clearPieceCount) {
            victor = FILL;
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

    // EFFECTS: Translates a player input command to the appropriate position
    //          on the board and returns the result. Throws IllegalPlayerInputException if
    //          the input is invalid.
    public int translateInput(String input) throws IllegalPlayerInputException {
        String toTranslate = sanitizeInput(input);
        int letter = toTranslate.charAt(0) - 'A';
        int num = 8 * (toTranslate.charAt(1) - '1');
        return letter + num;
    }

    // EFFECTS: Checks strings for appropriate conditions and processes it for later translation.
    //          Throws an IllegalPlayerInputException if input is invalid.
    public String sanitizeInput(String input) throws IllegalPlayerInputException {
        String processed = input.toUpperCase();
        if (processed.length() != 2
                || (processed.charAt(0) < 'A' || processed.charAt(0) > 'H')
                || (processed.charAt(1) < '1' || processed.charAt(1) > '8')) {
            throw new IllegalPlayerInputException();
        }
        return processed;
    }

    // EFFECTS: Translates an index position on the board to a player input command.
    public String indexToCommand(int position) {
        char letter = (char) ((position % 8) + 'A');
        char num = (char) ((position / 8) + '1');

        return String.valueOf(letter) + num;
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
            board.get(position).setState(turn);
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

        for (GamePiece piece : board) {
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
            List<GamePiece> potentialFlips = new LinkedList<>();
            moveCursorDirection(direction);
            GamePiece toCheck = board.get(cursor.getCurrent());

            // The entry being checked must be of the opposite state
            while (!(toCheck.getState().equals(turn)) && !(toCheck.getState().equals(EMPTY))) {
                potentialFlips.add(toCheck);
                moveCursorDirection(direction);
                toCheck = board.get(cursor.getCurrent());
            }

            // The entry being checked must hold an empty piece, and there are opposing pieces that would be flipped
            // if a piece is placed at that entry
            if (toCheck.getState().equals(EMPTY) && !(potentialFlips.isEmpty())) {
                if (validMoves.containsKey(toCheck.getPosition())) {
                    // Adds the potential flips to the corresponding key if it already exists in validMoves
                    validMoves.get(toCheck.getPosition()).addAll(potentialFlips);
                } else {
                    // Adds a key-value pair to validMoves if the key does not already exist
                    validMoves.put(toCheck.getPosition(), potentialFlips);
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

        for (GamePiece piece : board) {
            jsonArray.put(piece.toJson());
        }

        return jsonArray;
    }
}
