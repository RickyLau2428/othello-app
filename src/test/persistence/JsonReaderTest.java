package persistence;

import model.BoardTest;
import model.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static model.State.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// JUnit test class for JsonReader
public class JsonReaderTest extends BoardTest {
    private GameBoard testBoard;

    @BeforeEach
    public void setup() {
        testBoard = new GameBoard();
    }
    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/nonexistentfile.json");
        try {
            testBoard = reader.read();
            fail("Exception not thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReaderEmptyBoard() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyBoard.json");
        try {
            testBoard = reader.read();
        } catch (IOException e) {
            fail("Exception not expected.");
        }

        assertEquals(0, getActivePiecesNum(testBoard));
        assertEquals(FILL, testBoard.getTurn());
        assertEquals(0, testBoard.getClearPieceCount());
        assertEquals(0, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getGameOverCounter());
    }

    @Test
    public void testReaderGeneralBoard() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralBoard.json");
        try {
            testBoard = reader.read();
        } catch (IOException e) {
            fail("Exception not expected.");
        }

        assertEquals(27, getActivePiecesNum(testBoard));
        assertEquals(CLEAR, testBoard.getTurn());
        assertEquals(16, testBoard.getClearPieceCount());
        assertEquals(11, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getGameOverCounter());
    }
}
