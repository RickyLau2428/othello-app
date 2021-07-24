package model;

import exceptions.IllegalCursorException;
import exceptions.IllegalPlayerInputException;

import java.util.*;

import static model.State.*;

// Translates player input and processes game rules
public class GameBoard {
    // INVARIANT: SIDE_LENGTH must be 8
    public static final int SIDE_LENGTH = 8;
    public static final int BOARD_SIZE = SIDE_LENGTH * SIDE_LENGTH;
    public static final int MARGIN = SIDE_LENGTH / 2 - 1;
    // Q1 through Q4 are the corners closest to the origin on a board with length SIDE_LENGTH corresponding
    // to their quadrants
    public static final int Q1 = (SIDE_LENGTH * MARGIN) + (MARGIN + 1);
    public static final int Q2 = (SIDE_LENGTH * MARGIN) + MARGIN;
    public static final int Q3 = (SIDE_LENGTH * (MARGIN + 1)) + MARGIN;
    public static final int Q4 = (SIDE_LENGTH * (MARGIN + 1)) + (MARGIN + 1);

    private List<GamePiece> board;
    // INVARIANT: turn is either BLACK or WHITE
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
        for (int i = 0; i < BOARD_SIZE; i++) {
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
        return isGameOver;
    }

    public int getGameOverCounter() {
        return gameOverCounter;
    }

    // setters
    public void setTurn(State state) {
        this.turn = state;
        setValidMoves();
    }

    public void setGameOverCounter(int num) {
        this.gameOverCounter = 0;
    }

    // MODIFIES: this
    // EFFECTS: Sets up the board in the starting configuration.
    public void setUpGame() {
        board.get(Q1).setState(BLACK);
        board.get(Q2).setState(WHITE);
        board.get(Q3).setState(BLACK);
        board.get(Q4).setState(WHITE);
    }

    // MODIFIES: this
    // EFFECTS: Counts the total number of black and white pieces on the board and updates corresponding fields.
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
    // EFFECTS: Ends the game if two consecutive turns have no valid moves.
    public void checkGameOver() {
        if (gameOverCounter == 2) {
            isGameOver = true;
        }
    }

    // MODIFIES: this
    // EFFECTS: Translates a player input command to the appropriate position
    //          on the board. Returns -1 if the input is invalid.
    public int translateInput(String input) {
        try {
            String toTranslate = sanitizeInput(input);
            int letter = toTranslate.charAt(0) - 'A';
            int num = 8 * (toTranslate.charAt(1) - '1');
            return letter + num;
        } catch (IllegalPlayerInputException e) {
            return -1;
        }
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

    // MODIFIES: this
    // EFFECTS: If no valid moves can be made, increments gameOverCounter by 1, advances the game to the next turn
    //          and returns false. Does nothing and returns true if there are any valid moves.
    public boolean checkAnyValidMoves() {
        if (validMoves.isEmpty()) {
            gameOverCounter++;
            nextTurn();
            return false;
        }
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Progresses the game to the next player's turn and stores any valid moves.
    public void nextTurn() {
        if (turn.equals(BLACK)) {
            turn = WHITE;
        } else {
            turn = BLACK;
        }
        setValidMoves();
    }

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
            List<GamePiece> potentialFlips = new ArrayList<>();
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
}
