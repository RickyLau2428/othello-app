package ui;

import model.GameBoard;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.info.InfoPanel;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// Represents a GUI app that houses the board and all information components
public class OthelloApp extends JFrame {
    private GameBoard game;
    private BoardRender boardRender;
    private Menu menu;
    private JSplitPane content;
    private InfoPanel info;
    private boolean isNextTurn;

    private String jsonCursor;
    private JsonReader reader;
    private JsonWriter writer;

    // EFFECTS: Creates the window in which the game is played
    public OthelloApp() {
        super("IoMoth's Implementation of Othello");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("./res/images/icon.png").getImage());
        setResizable(false);
        initializePanels();
        initializeJsons();
        initializeMouseListener();
        setJMenuBar(menu.getMenuBar());
        add(content);
        pack();
        setVisible(true);
    }

    // setters:
    public void setGameInPanels(GameBoard game) {
        boardRender.setGame(game);
        info.getTurnPanel().setGame(game);
        info.getScorePanel().setGame(game);
        info.getHelpButton().setGame(game);
    }

    // MODIFIES: this
    // EFFECTS: Initializes all components in the GUI
    public void initializePanels() {
        game = new GameBoard();
        boardRender = new BoardRender(game);
        info = new InfoPanel(game, boardRender);
        content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardRender, info);
        menu = new Menu(this);
    }

    // MODIFIES: this
    // EFFECTS: Initializes readers and writers for accessing memory
    public void initializeJsons() {
        jsonCursor = "./data/saves/startBoard";
        reader = new JsonReader(jsonCursor);
        writer = new JsonWriter(jsonCursor);
    }

    // MODIFIES: this
    // EFFECTS: Makes the board panel a mouse listener that can respond to clicks
    public void initializeMouseListener() {
        content.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                placePieceOnBoard(e.getX(), e.getY());
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: Places a piece on the board and updates all GUI panels
    public void placePieceOnBoard(int positionX, int positionY) {
        int position = calculateIndex(positionX, positionY);
        isNextTurn = game.placePiece(position);
        if (isNextTurn) {
            boardRender.playPlaceSound();
        }
        updatePanels();
        repaint();
    }

    // MODIFIES: this
    // EFFECTS: Updates the board state and the info panels on the GUI after each turn
    public void updatePanels() {
        game.update();
        if (isNextTurn) {
            boardRender.clearHelpIcons();
            info.getScorePanel().setTextScore(game.getFillPieceCount(), game.getClearPieceCount());
            info.getTurnPanel().updateTurnLabel();
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates information panels with current state of game
    public void updateInformation() {
        setGameInPanels(game);
        boardRender.setUpBoardImage(game.getBoard());
        info.getScorePanel().setTextScore(game.getFillPieceCount(), game.getClearPieceCount());
        info.getTurnPanel().updateTurnLabel();
    }

    // EFFECTS: Calculates the position on the board based on the mouse position on release
    private int calculateIndex(int positionX, int positionY) {
        int col = positionX / BoardRender.BLANK_SQUARE.getWidth(this);
        int row = positionY / BoardRender.BLANK_SQUARE.getHeight(this);

        return (row * GameBoard.SIDE_LENGTH) + col;
    }

    // MODIFIES: this
    // EFFECTS: Loads a saved board state from file
    public void loadSave(File source) {
        reader.setSource(source.getPath());
        try {
            game = reader.read();
            game.setValidMoves();
            boardRender.reset();
            updateInformation();
            boardRender.repaint();
            info.repaint();
        } catch (IOException e) {
            System.out.println("Unable to read from file");
        }
    }

    // MODIFIES: this
    // EFFECTS: Saves a file to the specified destination
    public void saveFile(File destination) {
        writer.setDestination(destination.getPath());
        try {
            writer.open();
            writer.write(game);
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save to " + destination.getPath());
        }
    }

    public static void main(String[] args) {
        new OthelloApp();
    }
}
