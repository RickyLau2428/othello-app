package persistence;

import model.BoardTest;
import model.GameBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static model.State.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// JUnit test class for JsonWriter
public class JsonWriterTest extends BoardTest {
    private GameBoard testBoard;

    @BeforeEach
    public void setup() {
        testBoard = new GameBoard();
        clearBoard(testBoard);
    }

    @Test
    public void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/test/IllegalFilename\0.json");
            writer.open();
            fail("Exception not thrown.");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    public void testWriterEmptyBoard() {
        countPieces(testBoard);

        try {
            JsonWriter writer = new JsonWriter("./data/test/testWriterEmptyBoard.json");
            writer.open();
            writer.write(testBoard);
            writer.close();

            JsonReader reader = new JsonReader("./data/test/testWriterEmptyBoard.json");
            testBoard = reader.read();
        } catch (IOException e) {
            fail("Exception not expected.");
        }

        assertEquals(0, getActivePiecesNum(testBoard));
        assertEquals(FILL, testBoard.getTurn());
        assertEquals(0, testBoard.getFillPieceCount());
        assertEquals(0, testBoard.getClearPieceCount());
    }

    @Test
    public void testWriterGeneralBoard() {
        for (int i = 0; i <= 33; i++) {
            setPiece(testBoard, i, FILL);
        }
        for (int i = 34; i <= 52; i++) {
            setPiece(testBoard, i, CLEAR);
        }
        countPieces(testBoard);
        testBoard.setTurn(CLEAR);

        try {
            JsonWriter writer = new JsonWriter("./data/test/testWriterGeneralBoard.json");
            writer.open();
            writer.write(testBoard);
            writer.close();

            JsonReader reader = new JsonReader("./data/test/testWriterGeneralBoard.json");
            testBoard = reader.read();
        } catch (IOException e) {
            fail("Exception not expected.");
        }

        assertEquals(53, getActivePiecesNum(testBoard));
        assertEquals(CLEAR, testBoard.getTurn());
        assertEquals(34, testBoard.getFillPieceCount());
        assertEquals(19, testBoard.getClearPieceCount());
    }

}
