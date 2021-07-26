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
        assertEquals(FILL, testBoard.getTurn());

        assertEquals(FILL, getPieceState(Q1));
        assertEquals(CLEAR, getPieceState(Q2));
        assertEquals(FILL, getPieceState(Q3));
        assertEquals(CLEAR, getPieceState(Q4));
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
    public void testNextTurnFill() {
        testBoard.nextTurn();
        assertEquals(CLEAR, testBoard.getTurn());
    }

    @Test
    public void testNextTurnClear() {
        testBoard.setTurn(CLEAR);
        testBoard.nextTurn();
        assertEquals(FILL, testBoard.getTurn());
    }

    @Test
    public void testPlacePieceEmptyBoardFailure() {
        testBoard.setTurn(FILL);
        assertFalse(testBoard.placePiece(7));
        assertEquals(0, getActivePiecesNum());
    }

    @Test
    public void testPlacePieceFullBoardFailure() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            setPiece(i, FILL);
        }
        testBoard.setTurn(FILL);

        assertFalse(testBoard.placePiece(8));
        assertEquals(64, getActivePiecesNum());
    }

    @Test
    public void testPlacePieceFailure() {
        setPiece(3, CLEAR);
        setPiece(4, FILL);
        testBoard.setTurn(FILL);

        assertFalse(testBoard.placePiece(50));
        assertEquals(2, getActivePiecesNum());
    }

    @Test
    public void testPlacePieceMultipleSuccess() {
        for (int i = 18; i <= 21; i++) {
            setPiece(i, CLEAR);
        }
        setPiece(22, FILL);
        setPiece(23, CLEAR);

        testBoard.setTurn(FILL);
        assertTrue(testBoard.placePiece(17));
        for (int i = 17; i <= 22; i++) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(CLEAR, getPieceState(23));
        assertEquals(7, getActivePiecesNum());

        // CLEAR'S turn
        assertTrue(testBoard.placePiece(16));
        for (int i = 16; i <= 23; i++) {
            assertEquals(CLEAR, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testLeftCaptureSingle() {
        setPiece(27, FILL);
        setPiece(28, CLEAR);
        testBoard.setTurn(CLEAR);
        assertTrue(testBoard.placePiece(26));

        assertEquals(CLEAR, getPieceState(26));
        assertEquals(CLEAR, getPieceState(27));
        assertEquals(CLEAR, getPieceState(28));
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testLeftCaptureMultiple() {
        for (int i = 1; i <= 6; i++) {
            setPiece(i, CLEAR);
        }
        setPiece(7, FILL);
        testBoard.setTurn(FILL);

        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 7; i++) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testRightCaptureSingle() {
        setPiece(20, FILL);
        setPiece(21, CLEAR);
        testBoard.setTurn(FILL);
        assertTrue(testBoard.placePiece(22));

        for (int i = 20; i <= 22; i++) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testRightCaptureMultiple() {
        for (int i = 57; i <= 62; i++) {
            setPiece(i, FILL);
        }
        setPiece(56, CLEAR);
        testBoard.setTurn(CLEAR);
        assertTrue(testBoard.placePiece(63));

        for (int i = 56; i <= 63; i++) {
            assertEquals(CLEAR, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testUpCaptureSingle() {
        setPiece(27, FILL);
        setPiece(35, CLEAR);

        testBoard.setTurn(CLEAR);
        assertTrue(testBoard.placePiece(19));

        for (int i = 19; i <= 35; i += 8) {
            assertEquals(CLEAR, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testUpCaptureMultiple() {
        for (int i = 8; i <= 48; i += 8) {
            setPiece(i, CLEAR);
        }
        setPiece(56, FILL);

        testBoard.setTurn(FILL);
        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 56; i += 8) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testDownCaptureSingle() {
        setPiece(30, CLEAR);
        setPiece(22, FILL);

        testBoard.setTurn(FILL);
        assertTrue(testBoard.placePiece(38));

        for (int i = 22; i <= 38; i += 8) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testDownCaptureMultiple() {
        for (int i = 15; i <= 55; i += 8) {
            setPiece(i, FILL);
        }
        setPiece(7, CLEAR);

        testBoard.setTurn(CLEAR);
        assertTrue(testBoard.placePiece(63));

        for (int i = 7; i <= 63; i += 8) {
            assertEquals(CLEAR, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testUpperRightCaptureSingle() {
        setPiece(26, CLEAR);
        setPiece(19, FILL);

        testBoard.setTurn(CLEAR);
        assertTrue(testBoard.placePiece(12));

        for (int i = 12; i <= 27; i += 7) {
            assertEquals(CLEAR, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testUpperRightCaptureMultiple() {
        for (int i = 14; i <= 49; i += 7) {
            setPiece(i, CLEAR);
        }
        setPiece(56, FILL);
        testBoard.setTurn(FILL);

        assertTrue(testBoard.placePiece(7));

        for (int i = 7; i <= 56; i += 7) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testUpperLeftCaptureSingle() {
        setPiece(35, CLEAR);
        setPiece(44, FILL);
        testBoard.setTurn(FILL);

        assertTrue(testBoard.placePiece(26));

        for (int i = 26; i <= 44; i += 9) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testUpperLeftCaptureMultiple() {
        for (int i = 9; i <= 54; i += 9) {
            setPiece(i, FILL);
        }
        setPiece(63, CLEAR);
        testBoard.setTurn(CLEAR);

        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(CLEAR, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testLowerRightCaptureSingle() {
        setPiece(42, CLEAR);
        setPiece(51, FILL);
        testBoard.setTurn(CLEAR);

        assertTrue(testBoard.placePiece(60));

        for (int i = 42; i <= 60; i += 9) {
            assertEquals(CLEAR, getPieceState(i));
        }
        assertEquals(3, getActivePiecesNum());
    }

    @Test
    public void testLowerRightCaptureMultiple() {
        for (int i = 9; i <= 54; i += 9) {
            setPiece(i, CLEAR);
        }
        setPiece(0, FILL);
        testBoard.setTurn(FILL);

        assertTrue(testBoard.placePiece(63));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(8, getActivePiecesNum());
    }

    @Test
    public void testCaptureAllDirectionsMultiple() {
        setPiece(0, FILL);
        setPiece(3, FILL);
        setPiece(6, FILL);
        setPiece(24, FILL);
        setPiece(31, FILL);
        setPiece(48, FILL);
        setPiece(59, FILL);
        setPiece(63, FILL);
        for (int i = 9 ; i <= 54; i += 9) {
            if (i != 27) {
                setPiece(i, CLEAR);
            }
        }
        for (int i = 11; i <= 51; i += 8) {
            if (i != 27) {
                setPiece(i, CLEAR);
            }
        }
        for (int i = 13; i <= 41; i +=7) {
            if (i != 27) {
                setPiece(i, CLEAR);
            }
        }
        for (int i = 25; i <= 30; i++) {
            if (i != 27) {
                setPiece(i, CLEAR);
            }
        }
        testBoard.setTurn(FILL);

        assertTrue(testBoard.placePiece(27));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(FILL, getPieceState(i));
        }
        for (int i = 3; i <= 59; i += 8) {
            assertEquals(FILL, getPieceState(i));
        }
        for (int i = 6; i <= 48; i += 7) {
            assertEquals(FILL, getPieceState(i));
        }
        for (int i = 24; i <= 31; i++) {
            assertEquals(FILL, getPieceState(i));
        }
        assertEquals(28, getActivePiecesNum());
    }

    @Test
    public void testSetValidMovesFailure() {
        for (int i = 0; i <= 63; i++) {
            setPiece(i, FILL);
        }
        testBoard.setValidMoves();
        assertTrue(testBoard.getValidMoves().isEmpty());
    }

    @Test
    public void testPassTurn() {
        // Sets up board - fill has no valid moves
        for (int i = 0; i <= 23; i++) {
            if (i != 7) {
                setPiece(i, FILL);
            }
        }
        for (int i = 31; i <= 63; i += 8) {
            setPiece(i, CLEAR);
        }
        // Simulates turn passing
        testBoard.setTurn(FILL);
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
            setPiece(i, FILL);
        }
        for (int i = 32; i <= 63; i++) {
            setPiece(i, CLEAR);
        }
        // Simulates turn passing
        testBoard.setTurn(FILL);
        assertFalse(testBoard.checkAnyValidMoves());
        assertFalse(testBoard.checkAnyValidMoves());

        testBoard.checkGameOver();
        assertTrue(testBoard.isGameOver());
        assertEquals(EMPTY, testBoard.endGame());
        assertEquals(32, testBoard.getClearPieceCount());
        assertEquals(32, testBoard.getFillPieceCount());
    }

    @Test
    public void testEndGameClearVictory() {
        for (int i = 0; i <= 23; i++) {
            setPiece(i, CLEAR);
        }
        for (int i = 24; i <= 56; i += 8) {
            setPiece(i, FILL);
        }
        // Simulates turn passing
        testBoard.setTurn(FILL);
        assertFalse(testBoard.checkAnyValidMoves());
        assertFalse(testBoard.checkAnyValidMoves());

        testBoard.checkGameOver();
        assertTrue(testBoard.isGameOver());
        assertEquals(CLEAR, testBoard.endGame());
        assertEquals(24, testBoard.getClearPieceCount());
        assertEquals(5, testBoard.getFillPieceCount());
    }

    @Test
    public void testEndGameFillVictory() {
        for (int i = 0; i <= 23; i++) {
            setPiece(i, FILL);
        }
        for (int i = 31; i <= 63; i += 8) {
            setPiece(i, CLEAR);
        }
        // Simulates turn passing
        testBoard.setTurn(FILL);
        assertFalse(testBoard.checkAnyValidMoves());
        assertFalse(testBoard.checkAnyValidMoves());

        testBoard.checkGameOver();
        assertTrue(testBoard.isGameOver());
        assertEquals(FILL, testBoard.endGame());
        assertEquals(24, testBoard.getFillPieceCount());
        assertEquals(5, testBoard.getClearPieceCount());
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
