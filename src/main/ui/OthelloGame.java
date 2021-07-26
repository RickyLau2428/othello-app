package ui;

import exceptions.IllegalPlayerInputException;
import model.GameBoard;
import model.State;

import java.util.Scanner;

import static model.State.FILL;
import static model.State.CLEAR;
import static ui.DrawBoard.FILLED_CIRCLE;
import static ui.DrawBoard.CLEAR_CIRCLE;

// Represents a running match of Othello that interacts directly with the user(s)
public class OthelloGame {
    private GameBoard game;
    private Scanner sc;
    private DrawBoard drawBoard;
    private String rawInput;

    // EFFECTS: Creates a game board
    public OthelloGame() {
        game = new GameBoard();
        sc = new Scanner(System.in);
        drawBoard = new DrawBoard(game.getBoard());
    }

    // MODIFIES: this
    // EFFECTS: Starts the game and keeps it running until the game ends
    public void playGame() {
        printWelcomeMessage();
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
            try {
                isPiecePlaced = game.placePiece(game.translateInput(rawInput));
                if (!isPiecePlaced) {
                    System.out.println("Input was not a valid move. Please try again.");
                    printRetryMessage();
                }
            } catch (IllegalPlayerInputException e) {
                System.out.println("Player input did not match requirements. Please try again.");
                printRetryMessage();
            }
        } while (!isPiecePlaced);

        System.out.println("Valid move processed. Next turn: ");
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
        State victor = game.endGame();

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
