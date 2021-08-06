package ui;

import model.GameBoard;
import model.GamePiece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static model.State.*;

// Represents a board shown on the GUI
public class BoardRender extends JPanel {
    public static final Image CLEAR_CIRCLE = new ImageIcon("./res/images/gamepiece/clearCircle.png").getImage();
    public static final Image FILL_CIRCLE = new ImageIcon("./res/images/gamepiece/fillCircle.png").getImage();
    public static final Image BLANK_SQUARE = new ImageIcon("./res/images/blankSquare.png").getImage();
    public static final Image HELP_ICON = new ImageIcon("./res/images/helpIcon.png").getImage();
    public static final String OVER = "The winner is: ";

    public final int boardLength = GameBoard.SIDE_LENGTH * BLANK_SQUARE.getWidth(this);

    private GameBoard game;
    private List<GameSquare> board;

    // EFFECTS: Initializes a board to render with the appropriate size and colour
    public BoardRender(GameBoard game) {
        super();
        this.game = game;
        setPreferredSize(sizeWindow());
        setMinimumSize(sizeWindow());
        setBackground(Color.WHITE);
        board = new ArrayList<>(GameBoard.SIDE_LENGTH * GameBoard.SIDE_LENGTH);
    }

    // getters:
    public List<GameSquare> getBoard() {
        return board;
    }

    // setters:
    public void setGame(GameBoard game) {
        this.game = game;
    }

    // MODIFIES: this and g
    // EFFECTS: Updates the GUI board with the current game state and draws it on g. Switches to the game over screen
    //          if the game is over.
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateBoard();

        drawBoard(g);

        if (game.isGameOver()) {
            gameOver(g);
        }
        g.dispose();
    }

    // MODIFIES: g
    // EFFECTS: Prints the current board to the GUI
    private void drawBoard(Graphics g) {
        for (GameSquare gs : board) {
            g.drawImage(gs.getPieceImage(), gs.getPositionX(), gs.getPositionY(), this);
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the board to draw with the game's current state
    private void updateBoard() {
        if (board.isEmpty()) {
            populateGameSquares();
        }
        setUpBoardImage(game.getBoard());
    }

    // MODIFIES: this
    // EFFECTS: Completely wipes the board of all current images (used only in loading a board from file)
    public void reset() {
        for (int i = 0; i < GameBoard.BOARD_SIZE; i++) {
            board.get(i).setPieceImage(BLANK_SQUARE);
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes help icons from the board, if any
    public void clearHelpIcons() {
        for (GameSquare gs : board) {
            if (gs.getPieceImage().equals(HELP_ICON)) {
                gs.setPieceImage(BLANK_SQUARE);
            }
        }
    }

    // MODIFIES: g
    // EFFECTS: Displays the winner of the match and replay instructions onto g
    // Taken from SpaceInvadersRefactored at https://github.students.cs.ubc.ca/CPSC210/SpaceInvadersRefactored.git
    private void gameOver(Graphics g) {
        Color saved = g.getColor();

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, boardLength, boardLength);

        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", Font.BOLD, 35));
        FontMetrics fm = g.getFontMetrics();
        centreString(OVER + game.declareVictor(), g, fm, boardLength / 2);
        g.setColor(saved);
    }

    // MODIFIES: g
    // EFFECTS: Centres str horizontally at a height positionY
    // Taken from SpaceInvadersRefactored at https://github.students.cs.ubc.ca/CPSC210/SpaceInvadersRefactored.git
    private void centreString(String str, Graphics g, FontMetrics fm, int positionY) {
        int width = fm.stringWidth(str);
        g.drawString(str, (boardLength - width) / 2, positionY);
    }

    // MODIFIES: this
    // EFFECTS: Fully populates the board with blank squares
    public void populateGameSquares() {
        int width = getWidth();
        int height = getHeight();

        int squareWidth = width / GameBoard.SIDE_LENGTH;
        int squareHeight = height / GameBoard.SIDE_LENGTH;

        for (int row = 0; row < GameBoard.SIDE_LENGTH; row++) {
            for (int col = 0; col < GameBoard.SIDE_LENGTH; col++) {
                board.add(new GameSquare(BLANK_SQUARE, col * squareWidth, row * squareHeight));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Edits the viewing board with the appropriate images depending on position
    public void setUpBoardImage(Map<Integer, GamePiece> currBoard) {
        for (int i = 0; i < GameBoard.BOARD_SIZE; i++) {
            if (currBoard.containsKey(i)) {
                if (currBoard.get(i).getState().equals(CLEAR)) {
                    board.get(i).setPieceImage(CLEAR_CIRCLE);
                } else {
                    board.get(i).setPieceImage(FILL_CIRCLE);
                }
            }
        }
    }

    // EFFECTS: Returns a Dimension scaled to one-third of the user's screen.
    private Dimension sizeWindow() {
        return new Dimension(BLANK_SQUARE.getWidth(this) * GameBoard.SIDE_LENGTH,
                BLANK_SQUARE.getHeight(this) * GameBoard.SIDE_LENGTH);
    }
}
