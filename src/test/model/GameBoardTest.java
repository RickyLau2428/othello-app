package model;

import exceptions.IllegalPlayerInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.GameBoard.*;
import static model.State.*;
import static model.State.EMPTY;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameBoardTest {
    GameBoard testBoard;

    @BeforeEach
    public void setup() {
        testBoard = new GameBoard();
        clearBoard();
    }

    @Test
    public void testConstructorAndSetUpGame() {
        // Normally called in constructor - called here to populate board with
        // starting configuration after clearing the board in setup()
        testBoard.setUpGame();
        assertEquals(BLACK, testBoard.getTurn());

        assertEquals(BLACK, getPieceState(Q1));
        assertEquals(WHITE, getPieceState(Q2));
        assertEquals(BLACK, getPieceState(Q3));
        assertEquals(WHITE, getPieceState(Q4));
    }

    @Test
    public void testPrepareInputChangeNothingThrown() {
        try {
            assertEquals("A5", testBoard.sanitizeInput("a5"));
        } catch (IllegalPlayerInputException e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void testPrepareInputNoChangeNothingThrown() {
        try {
            assertEquals("B8", testBoard.sanitizeInput("B8"));
        } catch (IllegalPlayerInputException e) {
            fail("Exception not expected.");
        }
    }

    @Test
    public void testPrepareInputLengthException() {
        try {
            testBoard.sanitizeInput("test");
            fail("Exception not thrown.");
        } catch (IllegalPlayerInputException e) {

        }
    }

    @Test
    public void testTranslateInputNoExceptions() {
        assertEquals(52, testBoard.translateInput("E7"));
    }

    @Test
    public void testTranslateInputExceptionThrown() {
        assertEquals(-1, testBoard.translateInput("AJ"));
    }

    @Test
    public void testPrepareInputFirstCharLesserException() {
        try {
            testBoard.sanitizeInput("@5");
            fail("Exception not thrown.");
        } catch (IllegalPlayerInputException e) {

        }
    }

    @Test
    public void testPrepareInputFirstCharGreaterException() {
        try {
            testBoard.sanitizeInput("~6");
            fail("Exception not thrown.");
        } catch (IllegalPlayerInputException e) {

        }
    }

    @Test
    public void testPrepareInputSecondCharLesserException() {
        try {
            testBoard.sanitizeInput("A0");
            fail("Exception not thrown.");
        } catch (IllegalPlayerInputException e) {

        }
    }

    @Test
    public void testPrepareInputSecondCharGreaterException() {
        try {
            testBoard.sanitizeInput("AJ");
            fail("Exception not thrown.");
        } catch (IllegalPlayerInputException e) {

        }
    }

    @Test
    public void testNextTurnBlack() {
        testBoard.nextTurn();
        assertEquals(WHITE, testBoard.getTurn());
    }

    @Test
    public void testNextTurnWhite() {
        testBoard.setTurn(WHITE);
        testBoard.nextTurn();
        assertEquals(BLACK, testBoard.getTurn());
    }

    @Test
    public void testPlacePieceEmptyBoardFailure() {
        testBoard.setTurn(BLACK);
        assertFalse(testBoard.placePiece(7));
        assertEquals(0, getActivePiecesNum());
    }

    @Test
    public void testPlacePieceFullBoardFailure() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            setPiece(i, BLACK);
        }
        testBoard.setTurn(BLACK);

        assertFalse(testBoard.placePiece(8));
        assertEquals(64, getActivePiecesNum());
    }

    @Test
    public void testPlacePieceFailure() {
        setPiece(3, WHITE);
        setPiece(4, BLACK);
        testBoard.setTurn(BLACK);

        assertFalse(testBoard.placePiece(50));
        assertEquals(2, getActivePiecesNum());
    }

    @Test
    public void testPlacePieceMultipleSuccess() {
        for (int i = 18; i <= 21; i++) {
            setPiece(i, WHITE);
        }
        setPiece(22, BLACK);
        setPiece(23, WHITE);

        testBoard.setTurn(BLACK);
        assertTrue(testBoard.placePiece(17));
        for (int i = 17; i <= 22; i++) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(WHITE, getPieceState(23));
        assertEquals(7, getActivePiecesNum());

        // WHITE'S turn
        assertTrue(testBoard.placePiece(16));
        for (int i = 16; i <= 23; i++) {
            assertEquals(WHITE, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testLeftCaptureSingle() {
        setPiece(27, BLACK);
        setPiece(28, WHITE);
        testBoard.setTurn(WHITE);
        assertTrue(testBoard.placePiece(26));

        assertEquals(WHITE, getPieceState(26));
        assertEquals(WHITE, getPieceState(27));
        assertEquals(WHITE, getPieceState(28));
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testLeftCaptureMultiple() {
        for (int i = 1; i <= 6; i++) {
            setPiece(i, WHITE);
        }
        setPiece(7, BLACK);
        testBoard.setTurn(BLACK);

        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 7; i++) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testRightCaptureSingle() {
        setPiece(20, BLACK);
        setPiece(21, WHITE);
        testBoard.setTurn(BLACK);
        assertTrue(testBoard.placePiece(22));

        for (int i = 20; i <= 22; i++) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testRightCaptureMultiple() {
        for (int i = 57; i <= 62; i++) {
            setPiece(i, BLACK);
        }
        setPiece(56, WHITE);
        testBoard.setTurn(WHITE);
        assertTrue(testBoard.placePiece(63));

        for (int i = 56; i <= 63; i++) {
            assertEquals(WHITE, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testUpCaptureSingle() {
        setPiece(27, BLACK);
        setPiece(35, WHITE);

        testBoard.setTurn(WHITE);
        assertTrue(testBoard.placePiece(19));

        for (int i = 19; i <= 35; i += 8) {
            assertEquals(WHITE, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testUpCaptureMultiple() {
        for (int i = 8; i <= 48; i += 8) {
            setPiece(i, WHITE);
        }
        setPiece(56, BLACK);

        testBoard.setTurn(BLACK);
        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 56; i += 8) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testDownCaptureSingle() {
        setPiece(30, WHITE);
        setPiece(22, BLACK);

        testBoard.setTurn(BLACK);
        assertTrue(testBoard.placePiece(38));

        for (int i = 22; i <= 38; i += 8) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testDownCaptureMultiple() {
        for (int i = 15; i <= 55; i += 8) {
            setPiece(i, BLACK);
        }
        setPiece(7, WHITE);

        testBoard.setTurn(WHITE);
        assertTrue(testBoard.placePiece(63));

        for (int i = 7; i <= 63; i += 8) {
            assertEquals(WHITE, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testUpperRightCaptureSingle() {
        setPiece(26, WHITE);
        setPiece(19, BLACK);

        testBoard.setTurn(WHITE);
        assertTrue(testBoard.placePiece(12));

        for (int i = 12; i <= 27; i += 7) {
            assertEquals(WHITE, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testUpperRightCaptureMultiple() {
        for (int i = 14; i <= 49; i += 7) {
            setPiece(i, WHITE);
        }
        setPiece(56, BLACK);
        testBoard.setTurn(BLACK);

        assertTrue(testBoard.placePiece(7));

        for (int i = 7; i <= 56; i += 7) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testUpperLeftCaptureSingle() {
        setPiece(35, WHITE);
        setPiece(44, BLACK);
        testBoard.setTurn(BLACK);

        assertTrue(testBoard.placePiece(26));

        for (int i = 26; i <= 44; i += 9) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testUpperLeftCaptureMultiple() {
        for (int i = 9; i <= 54; i += 9) {
            setPiece(i, BLACK);
        }
        setPiece(63, WHITE);
        testBoard.setTurn(WHITE);

        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(WHITE, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testLowerRightCaptureSingle() {
        setPiece(42, WHITE);
        setPiece(51, BLACK);
        testBoard.setTurn(WHITE);

        assertTrue(testBoard.placePiece(60));

        for (int i = 42; i <= 60; i += 9) {
            assertEquals(WHITE, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testLowerRightCaptureMultiple() {
        for (int i = 9; i <= 54; i += 9) {
            setPiece(i, WHITE);
        }
        setPiece(0, BLACK);
        testBoard.setTurn(BLACK);

        assertTrue(testBoard.placePiece(63));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testCaptureAllDirectionsMultiple() {
        setPiece(0, BLACK);
        setPiece(3, BLACK);
        setPiece(6, BLACK);
        setPiece(24, BLACK);
        setPiece(31, BLACK);
        setPiece(48, BLACK);
        setPiece(59, BLACK);
        setPiece(63, BLACK);
        for (int i = 9 ; i <= 54; i += 9) {
            if (i != 27) {
                setPiece(i, WHITE);
            }
        }
        for (int i = 11; i <= 51; i += 8) {
            if (i != 27) {
                setPiece(i, WHITE);
            }
        }
        for (int i = 13; i <= 41; i +=7) {
            if (i != 27) {
                setPiece(i, WHITE);
            }
        }
        for (int i = 25; i <= 30; i++) {
            if (i != 27) {
                setPiece(i, WHITE);
            }
        }
        testBoard.setTurn(BLACK);

        assertTrue(testBoard.placePiece(27));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(BLACK, getPieceState(i));
        }
        for (int i = 3; i <= 59; i += 8) {
            assertEquals(BLACK, getPieceState(i));
        }
        for (int i = 6; i <= 48; i += 7) {
            assertEquals(BLACK, getPieceState(i));
        }
        for (int i = 24; i <= 31; i++) {
            assertEquals(BLACK, getPieceState(i));
        }
        assertEquals(28, getActivePiecesNum());
    }

    @Test
    public void testSetValidMovesFailure() {
        for (int i = 0; i <= 63; i++) {
            setPiece(i, BLACK);
        }
        testBoard.setValidMoves();
        assertTrue(testBoard.getValidMoves().isEmpty());
    }

    @Test
    public void testPassTurn() {
        // Sets up board - black has no valid moves
        for (int i = 0; i <= 23; i++) {
            if (i != 7) {
                setPiece(i, BLACK);
            }
        }
        for (int i = 31; i <= 63; i += 8) {
            setPiece(i, WHITE);
        }
        // Simulates turn passing
        testBoard.setTurn(BLACK);
        assertFalse(testBoard.checkAnyValidMoves());
        assertTrue(testBoard.checkAnyValidMoves());
        testBoard.setGameOverCounter(0);
        testBoard.checkGameOver();
        assertFalse(testBoard.isGameOver());
        assertEquals(0, testBoard.getGameOverCounter());
    }

    @Test
    public void testEndGameTie() {
        for (int i = 0; i <= 31; i++) {
            setPiece(i, BLACK);
        }
        for (int i = 32; i <= 63; i++) {
            setPiece(i, WHITE);
        }
        // Simulates turn passing
        testBoard.setTurn(BLACK);
        assertFalse(testBoard.checkAnyValidMoves());
        assertFalse(testBoard.checkAnyValidMoves());

        testBoard.checkGameOver();
        assertTrue(testBoard.isGameOver());
        assertEquals(EMPTY, testBoard.endGame());
        assertEquals(32, testBoard.getWhitePieceCount());
        assertEquals(32, testBoard.getBlackPieceCount());
    }

    @Test
    public void testEndGameWhiteVictory() {
        for (int i = 0; i <= 23; i++) {
            setPiece(i, WHITE);
        }
        for (int i = 24; i <= 56; i += 8) {
            setPiece(i, BLACK);
        }
        // Simulates turn passing
        testBoard.setTurn(BLACK);
        assertFalse(testBoard.checkAnyValidMoves());
        assertFalse(testBoard.checkAnyValidMoves());

        testBoard.checkGameOver();
        assertTrue(testBoard.isGameOver());
        assertEquals(WHITE, testBoard.endGame());
        assertEquals(24, testBoard.getWhitePieceCount());
        assertEquals(5, testBoard.getBlackPieceCount());
    }

    @Test
    public void testEndGameBlackVictory() {
        for (int i = 0; i <= 23; i++) {
            setPiece(i, BLACK);
        }
        for (int i = 31; i <= 63; i += 8) {
            setPiece(i, WHITE);
        }
        // Simulates turn passing
        testBoard.setTurn(BLACK);
        assertFalse(testBoard.checkAnyValidMoves());
        assertFalse(testBoard.checkAnyValidMoves());

        testBoard.checkGameOver();
        assertTrue(testBoard.isGameOver());
        assertEquals(BLACK, testBoard.endGame());
        assertEquals(24, testBoard.getBlackPieceCount());
        assertEquals(5, testBoard.getWhitePieceCount());
    }

    // EFFECTS: Places pieces on the test board regardless of game rules
    private void setPiece(int position, State state) {
        testBoard.getBoard().get(position).setState(state);
    }

    // EFFECTS: Returns the state of the game piece on the board
    private State getPieceState(int position) {
        return testBoard.getBoard().get(position).getState();
    }

    // EFFECTS: Returns the number of active pieces on the board (state != EMPTY)
    private int getActivePiecesNum() {
        int counter = 0;
        for (GamePiece g : testBoard.getBoard()) {
            if (!(g.getState().equals(EMPTY))) {
                counter++;
            }
        }
        return counter;
    }

    // EFFECTS: Removes all active pieces from play
    private void clearBoard() {
        for (GamePiece g : testBoard.getBoard()) {
            if (!(g.getState().equals(EMPTY))) {
                g.setState(EMPTY);
            }
        }
    }
}
