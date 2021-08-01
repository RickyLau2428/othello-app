package model;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.State.*;
import static org.junit.jupiter.api.Assertions.*;

// JUnit test class for GamePiece
class GamePieceTest {
    GamePiece testPiece;

    @BeforeEach
    public void setup() {
        testPiece = new GamePiece(0, FILL);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testPiece.getPosition());
        assertEquals(FILL, testPiece.getState());
    }

    @Test
    public void testSetState() {
        testPiece.setState(CLEAR);
        assertEquals(CLEAR, testPiece.getState());

        testPiece.setState(FILL);
        assertEquals(FILL, testPiece.getState());
    }

    @Test
    public void testFlipFill() {
        testPiece.flip();
        assertEquals(CLEAR, testPiece.getState());
    }

    @Test
    public void testFlipClear() {
        testPiece.setState(CLEAR);
        testPiece.flip();
        assertEquals(FILL, testPiece.getState());
    }

    @Test
    public void testToJson() {
        JSONObject testJson = testPiece.toJson();

        assertEquals(0, testJson.getInt("position"));
        assertEquals(FILL, testJson.get("state"));
    }
}