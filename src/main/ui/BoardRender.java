package ui;

import model.GameBoard;
import model.GamePiece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static model.State.*;

public class BoardRender extends JPanel implements MouseListener {
    public static final Image CLEAR_CIRCLE = new ImageIcon("./res/images/gamepiece/clearCircle.png").getImage();
    public static final Image FILL_CIRCLE = new ImageIcon("./res/images/gamepiece/fillCircle.png").getImage();
    public static final Image BLANK_SQUARE = new ImageIcon("./res/images/blankSquare.png").getImage();
    public static final Image HELP_ICON = new ImageIcon("./res/images/helpIcon.png").getImage();

    private OthelloGame game;
    private List<GameSquare> board;

    public BoardRender(OthelloGame game) {
        super();
        setPreferredSize(sizeWindow());
        setBackground(Color.WHITE);
        board = new ArrayList<>(GameBoard.SIDE_LENGTH * GameBoard.SIDE_LENGTH);
        this.game = game;
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateBoard();

        drawBoard(g);
        g.dispose();
    }

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

    // EFFECTS: Calculates the position on the board based on the mouse position on release
    private int calculateIndex(int positionX, int positionY) {
        int col = positionX / BLANK_SQUARE.getWidth(this);
        int row = positionY / BLANK_SQUARE.getHeight(this);

        return (row * GameBoard.SIDE_LENGTH) + col;
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

    @Override
    public void mouseClicked(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int position = calculateIndex(e.getX(), e.getY());
        game.getGame().placePiece(position);
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // do nothing
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //do nothing
    }
}
