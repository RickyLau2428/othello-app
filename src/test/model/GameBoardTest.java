package model;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static model.GameBoard.*;
import static model.State.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

// JUnit test class for GameBoard
public class GameBoardTest extends BoardTest {
    GameBoard testBoard;

    @BeforeEach
    public void setup() {
        testBoard = new GameBoard();
        clearBoard(testBoard);
    }

    @Test
    public void testConstructorAndSetUpGame() {
        // Normally called in constructor - called here to populate board with
        // starting configuration after clearing the board in setup()
        testBoard.setUpGame();
        assertEquals(FILL, testBoard.getTurn());


        assertEquals(FILL, getPieceState(testBoard, Q1));
        assertEquals(CLEAR, getPieceState(testBoard, Q2));
        assertEquals(FILL, getPieceState(testBoard, Q3));
        assertEquals(CLEAR, getPieceState(testBoard, Q4));
    }

    @Test
    public void testConstructorWithParameters() {
        testBoard = new GameBoard(FILL, 0, 0, 0);

        assertEquals(FILL, testBoard.getTurn());
        assertEquals(0, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getGameOverCounter());

        assertTrue(testBoard.getBoard().isEmpty());
        assertTrue(testBoard.getValidMoves().isEmpty());
        assertFalse(testBoard.isGameOver());
    }

    @Test
    public void testGetValidMoveKeys() {
        setPiece(testBoard, 0, FILL);
        setPiece(testBoard, 1, CLEAR);
        testBoard.setValidMoves();

        Set<Integer> testSet = testBoard.getValidMoveKeys();

        assertEquals(1, testSet.size());
        assertTrue(testSet.contains(2));
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
        countPieces(testBoard);

        assertFalse(testBoard.placePiece(7));
        assertEquals(0, getActivePiecesNum(testBoard));
        assertEquals(0, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testPlacePieceFullBoardFailure() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            setPiece(testBoard, i, FILL);
        }
        testBoard.setTurn(FILL);
        countPieces(testBoard);

        assertFalse(testBoard.placePiece(8));
        assertEquals(64, getActivePiecesNum(testBoard));
        assertEquals(64, testBoard.getFillPieceCount());
    }

    @Test
    public void testPlacePieceFailure() {
        setPiece(testBoard, 3, CLEAR);
        setPiece(testBoard, 4, FILL);
        testBoard.setTurn(FILL);
        countPieces(testBoard);

        assertFalse(testBoard.placePiece(50));
        assertEquals(2, getActivePiecesNum(testBoard));
        assertEquals(1, testBoard.getClearPieceCount());
        assertEquals(1, testBoard.getFillPieceCount());
    }

    @Test
    public void testPlacePieceMultipleSuccess() {
        for (int i = 18; i <= 21; i++) {
            setPiece(testBoard, i, CLEAR);
        }
        setPiece(testBoard, 22, FILL);
        setPiece(testBoard, 23, CLEAR);
        countPieces(testBoard);

        testBoard.setTurn(FILL);
        assertTrue(testBoard.placePiece(17));
        for (int i = 17; i <= 22; i++) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(CLEAR, getPieceState(testBoard, 23));
        assertEquals(7, getActivePiecesNum(testBoard));
        assertEquals(6, testBoard.getFillPieceCount());
        assertEquals(1, testBoard.getClearPieceCount());

        // CLEAR'S turn
        assertTrue(testBoard.placePiece(16));
        for (int i = 16; i <= 23; i++) {
            assertEquals(CLEAR, getPieceState(testBoard, i));
        }
        assertEquals(8, getActivePiecesNum(testBoard));
        assertEquals(8, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
    }

    @Test
    public void testLeftCaptureSingle() {
        setPiece(testBoard, 27, FILL);
        setPiece(testBoard, 28, CLEAR);
        testBoard.setTurn(CLEAR);
        assertTrue(testBoard.placePiece(26));
        countPieces(testBoard);

        assertEquals(CLEAR, getPieceState(testBoard, 26));
        assertEquals(CLEAR, getPieceState(testBoard, 27));
        assertEquals(CLEAR, getPieceState(testBoard, 28));
        assertEquals(3, getActivePiecesNum(testBoard));
        assertEquals(3, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
    }

    @Test
    public void testLeftCaptureMultiple() {
        for (int i = 1; i <= 6; i++) {
            setPiece(testBoard, i, CLEAR);
        }
        setPiece(testBoard, 7, FILL);
        testBoard.setTurn(FILL);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 7; i++) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(8, getActivePiecesNum(testBoard));
        assertEquals(8, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testRightCaptureSingle() {
        setPiece(testBoard, 20, FILL);
        setPiece(testBoard, 21, CLEAR);
        testBoard.setTurn(FILL);
        assertTrue(testBoard.placePiece(22));
        countPieces(testBoard);

        for (int i = 20; i <= 22; i++) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(3, getActivePiecesNum(testBoard));
        assertEquals(3, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testRightCaptureMultiple() {
        for (int i = 57; i <= 62; i++) {
            setPiece(testBoard, i, FILL);
        }
        setPiece(testBoard, 56, CLEAR);
        testBoard.setTurn(CLEAR);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(63));

        for (int i = 56; i <= 63; i++) {
            assertEquals(CLEAR, getPieceState(testBoard, i));
        }
        assertEquals(8, getActivePiecesNum(testBoard));
        assertEquals(8, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
    }

    @Test
    public void testUpCaptureSingle() {
        setPiece(testBoard, 27, FILL);
        setPiece(testBoard, 35, CLEAR);

        testBoard.setTurn(CLEAR);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(19));

        for (int i = 19; i <= 35; i += 8) {
            assertEquals(CLEAR, getPieceState(testBoard, i));
        }
        assertEquals(3, getActivePiecesNum(testBoard));
        assertEquals(3, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
    }

    @Test
    public void testUpCaptureMultiple() {
        for (int i = 8; i <= 48; i += 8) {
            setPiece(testBoard, i, CLEAR);
        }
        setPiece(testBoard, 56, FILL);

        testBoard.setTurn(FILL);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 56; i += 8) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(8, getActivePiecesNum(testBoard));
        assertEquals(8, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testDownCaptureSingle() {
        setPiece(testBoard, 30, CLEAR);
        setPiece(testBoard, 22, FILL);

        testBoard.setTurn(FILL);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(38));

        for (int i = 22; i <= 38; i += 8) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(3, getActivePiecesNum(testBoard));
        assertEquals(3, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testDownCaptureMultiple() {
        for (int i = 15; i <= 55; i += 8) {
            setPiece(testBoard, i, FILL);
        }
        setPiece(testBoard, 7, CLEAR);

        testBoard.setTurn(CLEAR);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(63));

        for (int i = 7; i <= 63; i += 8) {
            assertEquals(CLEAR, getPieceState(testBoard, i));
        }
        assertEquals(8, getActivePiecesNum(testBoard));
        assertEquals(8, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
    }

    @Test
    public void testUpperRightCaptureSingle() {
        setPiece(testBoard, 26, CLEAR);
        setPiece(testBoard, 19, FILL);

        testBoard.setTurn(CLEAR);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(12));

        for (int i = 12; i <= 27; i += 7) {
            assertEquals(CLEAR, getPieceState(testBoard, i));
        }
        assertEquals(3, getActivePiecesNum(testBoard));
        assertEquals(3, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
    }

    @Test
    public void testUpperRightCaptureMultiple() {
        for (int i = 14; i <= 49; i += 7) {
            setPiece(testBoard, i, CLEAR);
        }
        setPiece(testBoard, 56, FILL);
        countPieces(testBoard);

        testBoard.setTurn(FILL);

        assertTrue(testBoard.placePiece(7));

        for (int i = 7; i <= 56; i += 7) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(8, getActivePiecesNum(testBoard));
        assertEquals(8, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testUpperLeftCaptureSingle() {
        setPiece(testBoard, 35, CLEAR);
        setPiece(testBoard, 44, FILL);
        testBoard.setTurn(FILL);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(26));

        for (int i = 26; i <= 44; i += 9) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(3, getActivePiecesNum(testBoard));
        assertEquals(3, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testUpperLeftCaptureMultiple() {
        for (int i = 9; i <= 54; i += 9) {
            setPiece(testBoard, i, FILL);
        }
        setPiece(testBoard, 63, CLEAR);
        countPieces(testBoard);
        testBoard.setTurn(CLEAR);

        assertTrue(testBoard.placePiece(0));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(CLEAR, getPieceState(testBoard, i));
        }
        assertEquals(8, getActivePiecesNum(testBoard));
        assertEquals(8, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
    }

    @Test
    public void testLowerRightCaptureSingle() {
        setPiece(testBoard, 42, CLEAR);
        setPiece(testBoard, 51, FILL);
        testBoard.setTurn(CLEAR);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(60));

        for (int i = 42; i <= 60; i += 9) {
            assertEquals(CLEAR, getPieceState(testBoard, i));
        }
        assertEquals(3, getActivePiecesNum(testBoard));
        assertEquals(3, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
    }

    @Test
    public void testLowerRightCaptureMultiple() {
        for (int i = 9; i <= 54; i += 9) {
            setPiece(testBoard, i, CLEAR);
        }
        setPiece(testBoard, 0, FILL);
        testBoard.setTurn(FILL);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(63));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(8, getActivePiecesNum(testBoard));
        assertEquals(8, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testCaptureAllDirectionsMultiple() {
        setPiece(testBoard, 0, FILL);
        setPiece(testBoard, 3, FILL);
        setPiece(testBoard, 6, FILL);
        setPiece(testBoard, 24, FILL);
        setPiece(testBoard, 31, FILL);
        setPiece(testBoard, 48, FILL);
        setPiece(testBoard, 59, FILL);
        setPiece(testBoard, 63, FILL);
        for (int i = 9 ; i <= 54; i += 9) {
            if (i != 27) {
                setPiece(testBoard, i, CLEAR);
            }
        }
        for (int i = 11; i <= 51; i += 8) {
            if (i != 27) {
                setPiece(testBoard, i, CLEAR);
            }
        }
        for (int i = 13; i <= 41; i +=7) {
            if (i != 27) {
                setPiece(testBoard, i, CLEAR);
            }
        }
        for (int i = 25; i <= 30; i++) {
            if (i != 27) {
                setPiece(testBoard, i, CLEAR);
            }
        }
        testBoard.setTurn(FILL);
        countPieces(testBoard);

        assertTrue(testBoard.placePiece(27));

        for (int i = 0; i <= 63; i += 9) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        for (int i = 3; i <= 59; i += 8) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        for (int i = 6; i <= 48; i += 7) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        for (int i = 24; i <= 31; i++) {
            assertEquals(FILL, getPieceState(testBoard, i));
        }
        assertEquals(28, getActivePiecesNum(testBoard));
        assertEquals(28, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testSetValidMovesFailure() {
        for (int i = 0; i <= 63; i++) {
            setPiece(testBoard, i, FILL);
        }
        testBoard.setValidMoves();
        assertTrue(testBoard.getValidMoves().isEmpty());
    }

    @Test
    public void testPassTurn() {
        // Sets up board - fill has no valid moves
        for (int i = 0; i <= 23; i++) {
            if (i != 7) {
                setPiece(testBoard, i, FILL);
            }
        }
        for (int i = 31; i <= 63; i += 8) {
            setPiece(testBoard, i, CLEAR);
        }
        // Simulates turn passing
        testBoard.setTurn(FILL);
        testBoard.update();
        assertFalse(testBoard.isGameOver());
        assertEquals(0, testBoard.getGameOverCounter());
    }

    @Test
    public void testUpdateOnePassedTurn() {
        // Sets up board - fill has no valid moves
        for (int i = 0; i <= 23; i++) {
            if (i != 7) {
                setPiece(testBoard, i, FILL);
            }
        }
        for (int i = 31; i <= 63; i += 8) {
            setPiece(testBoard, i, CLEAR);
        }
        testBoard.setGameOverCounter(1);
        testBoard.setTurn(FILL);

        testBoard.update();

        assertTrue(testBoard.isGameOver());
    }

    @Test
    public void testUpdateHasValidMoves() {
        testBoard.setUpGame();
        testBoard.setGameOverCounter(1);

        testBoard.update();

        assertEquals(0, testBoard.getGameOverCounter());
    }

    @Test
    public void testEndGameTie() {
        for (int i = 0; i <= 31; i++) {
            setPiece(testBoard, i, FILL);
        }
        for (int i = 32; i <= 63; i++) {
            setPiece(testBoard, i, CLEAR);
        }
        // Simulates turn passing
        testBoard.setTurn(FILL);
        testBoard.update();

        assertTrue(testBoard.isGameOver());
        assertNull(testBoard.declareVictor());
    }

    @Test
    public void testEndGameClearVictory() {
        for (int i = 0; i <= 23; i++) {
            setPiece(testBoard, i, CLEAR);
        }
        for (int i = 24; i <= 56; i += 8) {
            setPiece(testBoard, i, FILL);
        }
        // Simulates turn passing
        testBoard.setTurn(FILL);
        testBoard.update();

        assertTrue(testBoard.isGameOver());
        countPieces(testBoard);
        assertEquals("clear", testBoard.declareVictor());
        assertEquals(24, testBoard.getClearPieceCount());
        assertEquals(5, testBoard.getFillPieceCount());
    }

    @Test
    public void testEndGameFillVictory() {
        for (int i = 0; i <= 23; i++) {
            setPiece(testBoard, i, FILL);
        }
        for (int i = 31; i <= 63; i += 8) {
            setPiece(testBoard, i, CLEAR);
        }
        // Simulates turn passing
        testBoard.setTurn(FILL);
        testBoard.update();

        assertTrue(testBoard.isGameOver());
        countPieces(testBoard);
        assertEquals("fill", testBoard.declareVictor());
        assertEquals(24, testBoard.getFillPieceCount());
        assertEquals(5, testBoard.getClearPieceCount());
    }

    @Test
    public void testToJsonEmptyBoard() {
        countPieces(testBoard);
        JSONObject testJson = testBoard.toJson();
        JSONArray testArray = testJson.getJSONArray("pieces");

        assertEquals(FILL, testJson.get("turn"));
        assertEquals(0, testJson.getInt("clearPieceCount"));
        assertEquals(0, testJson.getInt("fillPieceCount"));
        assertEquals(0, testJson.getInt("gameOverCount"));
        assertTrue(testArray.isEmpty());
    }

    @Test
    public void testToJsonGeneralBoard() {
        testBoard.setUpGame();
        testBoard.setTurn(CLEAR);
        countPieces(testBoard);
        JSONObject testJson = testBoard.toJson();
        JSONArray testArray = testJson.getJSONArray("pieces");
        JSONObject pieceToCheck;

        assertEquals(CLEAR, testJson.get("turn"));
        assertEquals(2, testJson.getInt("clearPieceCount"));
        assertEquals(2, testJson.getInt("fillPieceCount"));
        assertEquals(0, testJson.getInt("gameOverCount"));

        assertEquals(4, testArray.length());
        for (int i = 0; i < testArray.length(); i++) {
            pieceToCheck = testArray.getJSONObject(i);
            Object position = pieceToCheck.get("position");
            if (position.equals(27) || position.equals(36)) {
                assertEquals(CLEAR, pieceToCheck.get("state"));
            } else if (position.equals(28) || position.equals(35)) {
                assertEquals(FILL, pieceToCheck.get("state"));
            } else {
                fail("Expected pieces not in board.");
            }
        }
    }
}
