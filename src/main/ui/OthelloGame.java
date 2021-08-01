package ui;

import exceptions.IllegalPlayerInputException;
import model.GameBoard;
import model.State;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import static model.State.FILL;
import static model.State.CLEAR;
import static ui.DrawBoard.FILLED_CIRCLE;
import static ui.DrawBoard.CLEAR_CIRCLE;

// Represents a running match of Othello that interacts directly with the user(s)
public class OthelloGame {
    private static final String SAVE_DIRECTORY = "./data/test";
    private String jsonCursor;
    private String jsonStore = "./data/startBoard.json";

    private GameBoard game;
    private Scanner sc;
    private DrawBoard drawBoard;
    private String rawInput;
    private boolean isMenuOpen;
    private JsonReader jsonReader;
    private JsonWriter jsonWriter;

    // EFFECTS: Creates a game board
    public OthelloGame() {
        game = new GameBoard();
        sc = new Scanner(System.in);
        drawBoard = new DrawBoard(game.getBoard());
        isMenuOpen = false;
        jsonReader = new JsonReader(jsonCursor);
        jsonWriter = new JsonWriter(jsonCursor);
    }

    // MODIFIES: this
    // EFFECTS: Sets the file path to a file (potentially non-existent) in ./data
    public void setFileCursor(String fileName) {
        jsonCursor = "./data/" + fileName + ".json";
    }

    // MODIFIES: this
    // EFFECTS: Starts the game and keeps it running until the game ends, at which point it deletes the game file.
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
        deleteCurrentMatch();
    }

    // MODIFIES: this
    // EFFECTS : Asks for user input and stores it in this. If the user input did not result in a valid command or
    //           did not follow style requirements, prompts the user to enter a different command.
    //           Reports to the user if their move was valid.
    private void receiveUserInput() {
        System.out.print("Command: ");
        rawInput = sc.nextLine().trim().toLowerCase();

        boolean isPiecePlaced = false;
        do {
            while (rawInput.trim().equals("menu")) {
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
    public void displayMenu() {
        System.out.println("Welcome to the menu: Select one of the following options.");
        System.out.println("\tsave -> Save the current game to file.");
        System.out.println("\tload -> Load the previous game from file.");
        System.out.println("\tdel  -> Delete a specified game from file");
        System.out.println("\thelp -> See all currently valid moves.");
        System.out.println("\tscore -> See the current score.");
        System.out.println("\texit -> Exit the menu.");
        System.out.println("\tquit -> Quit the program.");

        System.out.print("Menu Command: ");
        rawInput = sc.nextLine().trim().toLowerCase();
    }

    // MODIFIES: this
    // EFFECTS: Calls the appropriate method depending on the menu option selected
    public void processMenuCommand() {
        switch (rawInput) {
            case "save":
            case "load":
            case "del":
                processMemoryCommands();
                break;
            case "help":
                displayValidMoves();
                break;
            case "score":
                displayScore();
                break;
            case "exit":
                exitMenu();
                break;
            case "quit":
                System.exit(-1);
            default:
                printRetryMessage();
                processMenuCommand();
        }
    }

    // MODIFIES: this
    // EFFECTS: Extension of processMenuCommands - handles save/load/delete exclusively
    private void processMemoryCommands() {
        switch (rawInput) {
            case "save":
                saveGame();
                break;
            case "load":
                loadGame();
                break;
            case "del":
                deleteGame();
                break;
            default:
                printRetryMessage();
                processMenuCommand();
        }
    }

    // MODIFIES: this
    // EFFECTS: Sets the current board to one loaded from memory
    //          Code taken from JsonSerializationDemo at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void loadGame() {
        if (!listSaves()) {
            return;
        }

        System.out.print("Please choose a game to load: ");
        String source = sc.nextLine();
        setFileCursor(source);
        jsonStore = jsonCursor;
        jsonReader.setSource(jsonStore);
        try {
            game = jsonReader.read();
            drawBoard = new DrawBoard(game.getBoard());
            System.out.println("Loaded a saved board from " + jsonStore);
            game.setValidMoves();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + jsonStore);
        }
    }

    // MODIFIES: this
    // EFFECTS: Saves the game board to file
    //          Code taken from JsonSerializationDemo at https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
    private void saveGame() {
        listSaves();
        System.out.print("Please input the name for your save file (existing or new): ");
        String destination = sc.nextLine();
        setFileCursor(destination);
        jsonWriter.setDestination(jsonCursor);
        try {
            jsonWriter.open();
            jsonWriter.write(game);
            jsonWriter.close();
            System.out.println("Saved the current game to " + jsonCursor);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to save to " + jsonCursor);
        }
    }

    // MODIFIES: this
    // EFFECTS: Deletes the save file with the name given on prompt
    private void deleteGame() {
        listSaves();
        System.out.print("Please input the name of the file you wish to delete: ");
        String toDelete = sc.nextLine();
        setFileCursor(toDelete);
        File fileToDelete = new File(jsonCursor);

        System.out.print("You are about to delete \"" + toDelete + "\". This change is irreversible. "
                + "Are you sure? [y/n] ");
        String confirmation = sc.nextLine().trim().toLowerCase();

        if ((confirmation.length() == 1 && confirmation.equals("y"))
                || (confirmation.length() == 3 && confirmation.equals("yes"))) {
            if (fileToDelete.delete()) {
                System.out.println("The file \"" + toDelete + "\" has been deleted.");
            } else {
                System.out.println("Could not delete \"" + toDelete + "\".");
            }
        }
        System.out.println("Now exiting deletion menu...");
    }

    // EFFECTS: Deletes the current match from file
    private void deleteCurrentMatch() {
        File currentMatch = new File(jsonStore);
        if (currentMatch.delete()) {
            System.out.println("Deleted the current match from memory.");
        } else {
            System.out.println("Could not delete the current match from memory.");
        }
    }

    // EFFECTS: Prints the names of save files (.json files) stored in ./data to console. Returns true if there are
    //          any saved games in memory, false otherwise.
    private boolean listSaves() {
        boolean hasSaveFiles = true;
        File saveFolder = new File(SAVE_DIRECTORY);
        File[] saves = saveFolder.listFiles();

        assert saves != null;
        if (saves.length == 1) {
            System.out.println("There are no files saved in " + SAVE_DIRECTORY);
            hasSaveFiles = false;
        } else {
            System.out.print("Currently saved games are: ");
            for (File save : saves) {
                if (save.isFile()) {
                    int nameLength = save.getName().indexOf('.');
                    String fileName = save.getName().substring(0, nameLength);
                    System.out.print(fileName + "; ");
                }
            }
            System.out.println();
        }
        return hasSaveFiles;
    }

    // MODIFIES: this
    // EFFECTS: Exits the menu, prints the current board state and prompts the user for a movement command
    private void exitMenu() {
        isMenuOpen = false;
        System.out.println("The menu has been closed. The current board is: ");
        drawBoard.printBoard();
        System.out.println("It is currently " + game.getTurn().toString().toLowerCase() + "'s turn.");
        System.out.print("Please enter a command: ");
        rawInput = sc.nextLine();
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
        rawInput = sc.nextLine().trim().toLowerCase();
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

    // EFFECTS: Prints current board state and instructions for the current turn to console.
    private void printTurnInfo() {
        drawBoard.printBoard();
        System.out.println("Input a placement command in the format <letter><number> e.g. \"A5\"");
        System.out.println("It is currently " + game.getTurn().toString().toLowerCase() + "'s turn.");
    }
}
