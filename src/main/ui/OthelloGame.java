package ui;

import model.GameBoard;
import model.State;

import java.util.Scanner;

import static model.State.BLACK;
import static model.State.WHITE;
import static ui.DrawBoard.BLACK_CIRCLE;
import static ui.DrawBoard.WHITE_CIRCLE;

// Runs game and calls print methods
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

            drawBoard.printBoard();
            printTurnInstructions();
            System.out.print("Command: ");
            rawInput = sc.nextLine();

            while (!(game.placePiece(game.translateInput(rawInput)))) {
                System.out.println("Please enter a valid command: ");
                System.out.println("Command: ");
                rawInput = sc.nextLine();
            }
            System.out.println("Valid move processed. Next turn: ");
        }
        System.out.println("The game is now over. Calculating score...");
        State victor = game.endGame();
        printEndMessage(victor);
    }

    // EFFECTS: Prints the victor of the match and the final score to console
    private void printEndMessage(State victor) {
        System.out.println("The superior Othello player is...");
        if (victor.equals(WHITE)) {
            System.out.println("White! Congratulations!");
        } else if (victor.equals(BLACK)) {
            System.out.println("Black! Congratulations!");
        } else {
            System.out.println("Neither of you! It's a tie - congrats to both players!");
        }
        System.out.println("The final score was " + game.getWhitePieceCount() + " for white, and "
                + game.getBlackPieceCount() + " for black.");
    }

    // EFFECTS: Prints the starting message to console
    private void printWelcomeMessage() {
        System.out.println("Welcome to IoMoth's Othello implementation! Black game pieces are represented by "
                + BLACK_CIRCLE + " and " + WHITE_CIRCLE + " for White game pieces.");
        System.out.println("Have fun!");
    }

    // EFFECTS: Prints instructions for the current turn
    private void printTurnInstructions() {
        System.out.println("Input a placement command in the format <letter><number> e.g. \"A5\"");
        if (game.getTurn().equals(BLACK)) {
            System.out.println("It is currently Black's turn.");
        } else if (game.getTurn().equals(WHITE)) {
            System.out.println("It is currently White's turn.");
        }
    }

    public static void main(String[] args) {
        OthelloGame game = new OthelloGame();
        game.playGame();
    }
}
