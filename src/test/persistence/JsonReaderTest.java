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
    public void testSetSource() {
        JsonReader reader = new JsonReader("./data/test/testFile.json");
        assertEquals("./data/test/testFile.json", reader.getSource());

        reader.setSource("./data/test/newFile.json");
        assertEquals("./data/test/newFile.json", reader.getSource());
    }

    @Test
    public void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/test/nonexistentfile.json");
        try {
            testBoard = reader.read();
            fail("Exception not thrown.");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testReaderEmptyBoard() {
        JsonReader reader = new JsonReader("./data/test/testReaderEmptyBoard.json");
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
        JsonReader reader = new JsonReader("./data/test/testReaderGeneralBoard.json");
        try {
            testBoard = reader.read();
        } catch (IOException e) {
            fail("Exception not expected.");
        }

        assertEquals(9, getActivePiecesNum(testBoard));
        assertEquals(CLEAR, testBoard.getTurn());
        assertEquals(4, testBoard.getClearPieceCount());
        assertEquals(5, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getGameOverCounter());
    }
}
