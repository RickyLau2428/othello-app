package ui;

import javax.swing.*;
import java.awt.*;

public class OthelloApp extends JFrame {
    private OthelloGame game;
    private BoardRender boardRender;
    private JMenuBar menuBar;
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
        add(menuBar);
        add(boardRender);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        OthelloGame match = new OthelloGame();
        new OthelloApp();
        match.playGame();
    }
}
