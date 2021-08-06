package ui;

import model.GameBoard;
import ui.info.InfoPanel;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static model.State.CLEAR;
import static model.State.FILL;

public class OthelloApp extends JFrame {
    private OthelloGame game;
    private BoardRender boardRender;
    private JMenuBar menuBar;
    private JSplitPane content;
    private InfoPanel info;
    private boolean isNextTurn;
    private JMenu menu;

    // EFFECTS: Creates the window in which the game is played
    public OthelloApp() {
        super("IoMoth's Implementation of Othello");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("./res/images/icon.png").getImage());
        menuBar = new JMenuBar();
        menu = new JMenu("A Menu");
        game = new OthelloGame();
        boardRender = new BoardRender(game);
        info = new InfoPanel(game, boardRender);
        content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardRender, info);
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int position = calculateIndex(e.getX(), e.getY());
                isNextTurn = game.getGame().placePiece(position);
                updatePanels();
                repaint();
            }
        });
        add(menuBar);
        add(content);
        pack();
        setVisible(true);
    }

    // EFFECTS: Calculates the position on the board based on the mouse position on release
    private int calculateIndex(int positionX, int positionY) {
        int col = positionX / BoardRender.BLANK_SQUARE.getWidth(this);
        int row = positionY / BoardRender.BLANK_SQUARE.getHeight(this);

        return (row * GameBoard.SIDE_LENGTH) + col;
    }

    public void updatePanels() {
        if (isNextTurn) {
            boardRender.clearHelpIcons();
            info.getScorePanel().setTextScore(FILL, game.getGame().getFillPieceCount());
            info.getScorePanel().setTextScore(CLEAR, game.getGame().getClearPieceCount());
            info.getTurnPanel().nextTurnLabel();
        }
    }

    public static void main(String[] args) {
        OthelloGame match = new OthelloGame();
        new OthelloApp();
        match.playGame();
    }
}
