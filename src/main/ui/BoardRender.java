package ui;

import model.GameBoard;
import model.GamePiece;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static model.State.*;

public class BoardRender extends JPanel {
    public static final Image CLEAR_CIRCLE = new ImageIcon("./res/images/gamepiece/clearCircle.png").getImage();
    public static final Image FILL_CIRCLE = new ImageIcon("./res/images/gamepiece/fillCircle.png").getImage();
    public static final Image BLANK_SQUARE = new ImageIcon("./res/images/blankSquare.png").getImage();
    public static final Image HELP_ICON = new ImageIcon("./res/images/helpIcon.png").getImage();
    public static final String OVER = "The winner is: ";
    public static final String RESET = "Press [SPACE] to start another game.";

    public final int boardLength = GameBoard.SIDE_LENGTH * BLANK_SQUARE.getWidth(this);

    private OthelloGame game;
    private List<GameSquare> board;

    public BoardRender(OthelloGame game) {
        super();
        setPreferredSize(sizeWindow());
        setMinimumSize(sizeWindow());
        setBackground(Color.WHITE);
        board = new ArrayList<>(GameBoard.SIDE_LENGTH * GameBoard.SIDE_LENGTH);
        this.game = game;
    }

    // getters:
    public List<GameSquare> getBoard() {
        return board;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateBoard();

        drawBoard(g);

        // TODO: Fix game end
        if (game.getGame().isGameOver()) {
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
            populateBoard();
            setupBoard(game.getGame().getBoard());
        }
        updatePieces();
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
        g.setColor(new Color(0, 0, 0));
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        FontMetrics fm = g.getFontMetrics();
        centreString(OVER + game.getGame().declareVictor(), g, fm, boardLength / 2);
        centreString(RESET, g, fm, boardLength / 2 + 50);
        g.setColor(saved);
    }

    // MODIFIES: g
    // EFFECTS: Centres str horizontally at a height positionY
    // Taken from SpaceInvadersRefactored at https://github.students.cs.ubc.ca/CPSC210/SpaceInvadersRefactored.git
    private void centreString(String str, Graphics g, FontMetrics fm, int positionY) {
        int width = fm.stringWidth(str);
        g.drawString(str, boardLength - width / 2, positionY);
    }

    // MODIFIES: this
    // EFFECTS: Fully populates the board with blank squares
    private void populateBoard() {
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
    private void setupBoard(Map<Integer, GamePiece> currBoard) {
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

    // MODIFIES: this
    // EFFECTS: Updates the board with all recently flipped/played pieces
    private void updatePieces() {
        if (game.getGame().getTurn().equals(CLEAR)) {
            for (GamePiece gp : game.getGame().getPlayedPieces()) {
                Image currentFrame = new ImageIcon(("./res/images/gamepiece/fillCircle.png")).getImage();
                board.get(gp.getPosition()).setPieceImage(currentFrame);
            }
        } else {
            for (GamePiece gp : game.getGame().getPlayedPieces()) {
                Image currentFrame = new ImageIcon(("./res/images/gamepiece/clearCircle.png")).getImage();
                board.get(gp.getPosition()).setPieceImage(currentFrame);
            }
        }
    }

    // EFFECTS: Returns a Dimension scaled to one-third of the user's screen.
    private Dimension sizeWindow() {
        return new Dimension(BLANK_SQUARE.getWidth(this) * GameBoard.SIDE_LENGTH,
                BLANK_SQUARE.getHeight(this) * GameBoard.SIDE_LENGTH);
    }
}
