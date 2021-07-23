package ui;

import model.GameBoard;

import java.util.Scanner;

public class OthelloGame {
    private GameBoard game;
    private Scanner scanner;
    private DrawBoard drawBoard;
    private String rawInput;

    // EFFECTS: Creates a game board
    public OthelloGame() {

    }

    // MODIFIES: this
    // EFFECTS: Starts the game and keeps it running while isGameOver is false
    public void playGame() {

    }

    // MODIFIES: this
    // EFFECTS: Translates a player input command (e.g. "A5") to the appropriate position
    //          on the board. Returns -1 if the input was invalid.
    public int translateInput(String input) {
        return 0;
    }

    // EFFECTS: Checks strings for appropriate conditions and processes it for later translation
    public String prepareInput(String input) {
        return null;
    }

    // EFFECTS: Prints the starting message to console
    private void printWelcomeMessage() {

    }

    // EFFECTS: Prints instructions for the current turn
    private void printTurnInstructions() {

    }

    public static void main(String[] args) {

    }
}
