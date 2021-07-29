package ui;

import exceptions.IllegalPlayerInputException;
import model.GameBoard;
import model.State;
import persistence.JsonReader;

import java.io.IOException;
import java.util.Scanner;

import static model.State.FILL;
import static model.State.CLEAR;
import static ui.DrawBoard.FILLED_CIRCLE;
import static ui.DrawBoard.CLEAR_CIRCLE;

// Represents a running match of Othello that interacts directly with the user(s)
public class OthelloGame {
    private static final String JSON_STORE = "./data/testReaderEmptyBoard.json";
    private GameBoard game;
    private Scanner sc;
    private DrawBoard drawBoard;
    private String rawInput;
    private boolean isMenuOpen;
    private JsonReader jsonReader;

    // EFFECTS: Creates a game board
    public OthelloGame() {
        game = new GameBoard();
        sc = new Scanner(System.in);
        drawBoard = new DrawBoard(game.getBoard());
        isMenuOpen = false;
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this
    // EFFECTS: Starts the game and keeps it running until the game ends
    public void playGame() {
        printWelcomeMessage();
        game.setValidMoves();
        while (!game.isGameOver()) {
            if (!game.checkAnyValidMoves()) {
                game.nextTurn();
                game.checkGameOver();
                continue;
            }
            game.setGameOverCounter(0);
            printTurnInfo();
            receiveUserInput();
        }
        printEndMessage();
    }

    // MODIFIES: this
    // EFFECTS : Asks for user input and stores it in this. If the user input did not result in a valid move or
    //           did not follow style requirements, prompts the user to enter a different command.
    //           Reports to the user if their move was valid.
    private void receiveUserInput() {
        System.out.print("Command: ");
        rawInput = sc.nextLine();

        boolean isPiecePlaced = false;
        do {
            if (rawInput.equalsIgnoreCase("menu")) {
                isMenuOpen = true;
                while (isMenuOpen) {
                    displayMenu();
                    processMenuCommand();
                }
            }

            try {
                isPiecePlaced = game.placePiece(game.translateInput(rawInput));
                if (!isPiecePlaced) {
                    System.out.println("Player input was not a valid move. Please try again.");
                    printRetryMessage();
                }
            } catch (IllegalPlayerInputException e) {
                System.out.println("Player input did not match style requirements. Please try again.");
                printRetryMessage();
            }
        } while (!isPiecePlaced);

        System.out.println("Valid move processed. Next turn: ");
    }

    // MODIFIES: this
    // EFFECTS: Displays the game menu and prompts the user to select an option
    private void displayMenu() {
        System.out.println("Welcome to the menu: Select one of the following options.");
        System.out.println("\tsave -> Save the current game to file.");
        System.out.println("\tload -> Load the previous game from file.");
        System.out.println("\thelp -> See all currently valid moves.");
        System.out.println("\tscore -> See the current score.");
        System.out.println("\texit -> Exit the menu.");
        System.out.println("\tquit -> Quit the program.");

        System.out.print("Command:  ");
        rawInput = sc.nextLine();
    }

    // MODIFIES: this
    // EFFECTS: Calls the appropriate method depending on the menu option selected
    private void processMenuCommand() {
        switch (rawInput.toLowerCase()) {
            case "save":
                break;
            case "load":
                loadGame();
                break;
            case "help":
                displayValidMoves();
                break;
            case "score":
                displayScore();
                break;
            case "exit":
                isMenuOpen = false;
                System.out.println("The menu has been closed. The current board is: ");
                drawBoard.printBoard();
                System.out.print("Please enter a movement command: ");
                rawInput = sc.nextLine();
                break;
            case "quit":
                System.exit(-1);
            default: printRetryMessage();
        }
    }

    // MODIFIES: this
    // EFFECTS: Sets the current board state to one loaded from JSON_STORE
    //          Code taken from JsonSerializationDemo at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void loadGame() {
        try {
            game = jsonReader.read();
            drawBoard = new DrawBoard(game.getBoard());
            System.out.println("Loaded a saved board from " + JSON_STORE);
            System.out.println(game.getClearPieceCount());
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // EFFECTS: Prints the current score to console
    private void displayScore() {
        System.out.println("The current score is " + game.getFillPieceCount() + " for fill and "
                + game.getClearPieceCount() + " for clear.");
    }

    // EFFECTS: Prints all valid moves to console
    private void displayValidMoves() {
        System.out.print("Currently valid moves are: ");
        for (int pos : game.getValidMoves().keySet()) {
            System.out.print(game.indexToCommand(pos) + "; ");
        }
        System.out.println();
    }

    // MODIFIES: this
    // EFFECTS: Prompts the user to re-enter a valid command and stores it in this
    private void printRetryMessage() {
        System.out.print("Please enter a valid command: ");
        rawInput = sc.nextLine();
    }

    // MODIFIES: this
    // EFFECTS: Ends the game and prints the final board state, the victor and the final score to console
    private void printEndMessage() {
        System.out.println("The game is now over. Calculating score...");
        System.out.println("The final board state was: ");
        drawBoard.printBoard();
        State victor = game.declareVictor();

        System.out.println("The superior Othello player is...");
        if (victor.equals(CLEAR)) {
            System.out.println("Clear! Congratulations!");
        } else if (victor.equals(FILL)) {
            System.out.println("Fill! Congratulations!");
        } else {
            System.out.println("Neither of you! It's a tie - congrats to both players!");
        }
        System.out.println("The final score was " + game.getClearPieceCount() + " for clear, and "
                + game.getFillPieceCount() + " for fill.");
    }

    // EFFECTS: Prints the starting message to console
    private void printWelcomeMessage() {
        System.out.println("Welcome to IoMoth's Othello implementation! To reduce ambiguity, black and "
                + "white pieces are labeled as fill and clear respectively. Fill game pieces are represented by "
                + FILLED_CIRCLE + " and clear pieces are represented by " + CLEAR_CIRCLE + ".");
        System.out.println("Access the menu at any time by typing \"menu\".");
        System.out.println("Have fun!");
    }

    // EFFECTS: Prints board state and instructions for the current turn to console
    private void printTurnInfo() {
        drawBoard.printBoard();
        System.out.println("Input a placement command in the format <letter><number> e.g. \"A5\"");
        if (game.getTurn().equals(FILL)) {
            System.out.println("It is currently fill's turn.");
        } else if (game.getTurn().equals(CLEAR)) {
            System.out.println("It is currently clear's turn.");
        }
    }

    public static void main(String[] args) {
        OthelloGame match = new OthelloGame();
        match.playGame();
    }
}
