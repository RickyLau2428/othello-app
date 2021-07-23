package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static model.State.*;
import static org.junit.jupiter.api.Assertions.*;

class GamePieceTest {
    GamePiece testPiece;

    @BeforeEach
    public void setup() {
        testPiece = new GamePiece(0, BLACK);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testPiece.getPosition());
        assertEquals(BLACK, testPiece.getState());
    }

    @Test
    public void testSetState() {
        testPiece.setState(WHITE);
        assertEquals(WHITE, testPiece.getState());

        testPiece.setState(EMPTY);
        assertEquals(EMPTY, testPiece.getState());
    }

    @Test
    public void testFlipBlack() {
        testPiece.flip();
        assertEquals(WHITE, testPiece.getState());
    }

    @Test
    public void testFlipWhite() {
        testPiece.setState(WHITE);
        testPiece.flip();
        assertEquals(BLACK, testPiece.getState());
    }
}