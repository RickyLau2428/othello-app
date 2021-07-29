package model;

import exceptions.IllegalCursorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

// JUnit test class for Cursor
public class CursorTest {
    Cursor testCursor;

    @BeforeEach
    public void setup() {
        testCursor = new Cursor(0);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testCursor.getOriginal());
        assertEquals(0, testCursor.getCurrent());
    }

    @Test
    public void testSetPositionBorderNothingThrown() {
        try {
            testCursor.setPosition(63);
        } catch (IllegalCursorException e) {
            fail("Exception was not expected.");
        }
        assertEquals(63, testCursor.getOriginal());
        assertEquals(63, testCursor.getCurrent());
    }

    @Test
    public void testSetPositionTypicalNothingThrown() {
        try {
            testCursor.setPosition(25);
        } catch (IllegalCursorException e) {
            fail("Exception was not expected.");
        }
        assertEquals(25, testCursor.getOriginal());
        assertEquals(25, testCursor.getCurrent());
    }

    @Test
    public void testSetPositionGreaterException() {
        try {
            testCursor.setPosition(100);
            fail("Exception was not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testSetPositionLesserException() {
        try {
            testCursor.setPosition(-3);
            fail("Exception was not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testReset() {
        testCursor.setCurrent(25);
        assertEquals(25, testCursor.getCurrent());

        testCursor.reset();

        assertEquals(0, testCursor.getOriginal());
        assertEquals(0, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorRightNothingThrown() {
        try {
            testCursor.moveCursorRight();
        } catch (IllegalCursorException e) {
            fail("Exception not expected.");
        }
        assertEquals(0, testCursor.getOriginal());
        assertEquals(1, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorRightBorderException() {
        try {
            testCursor.setPosition(63);
            testCursor.moveCursorRight();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorRightWrapAroundException() {
        try {
            testCursor.setPosition(7);
            testCursor.moveCursorRight();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorLeftNothingThrown() {
        try {
            testCursor.setPosition(25);
            testCursor.moveCursorLeft();
        } catch (IllegalCursorException e) {
            fail("Exception not expected.");
        }
        assertEquals(25, testCursor.getOriginal());
        assertEquals(24, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorLeftBorderException() {
        try {
            testCursor.moveCursorLeft();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorLeftWrapAroundException() {
        try {
            testCursor.setPosition(8);
            testCursor.moveCursorLeft();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorUpNothingThrown() {
        try {
            testCursor.setPosition(25);
            testCursor.moveCursorUp();
        } catch (IllegalCursorException e) {
            fail("Exception not expected.");
        }
        assertEquals(25, testCursor.getOriginal());
        assertEquals(17, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorUpBorderException() {
        try {
            testCursor.moveCursorUp();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorDownNothingThrown() {
        try {
            testCursor.moveCursorDown();
        } catch (IllegalCursorException e) {
            fail("Exception not expected.");
        }
        assertEquals(0, testCursor.getOriginal());
        assertEquals(8, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorBorderException() {
        try {
            testCursor.setPosition(61);
            testCursor.moveCursorDown();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorUpperRightNothingThrown() {
        try {
            testCursor.setPosition(19);
            testCursor.moveCursorUpperRight();
        } catch (IllegalCursorException e) {
            fail("Exception not expected.");
        }
        assertEquals(19, testCursor.getOriginal());
        assertEquals(12, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorUpperRightBorderException() {
        try {
            testCursor.setPosition(7);
            testCursor.moveCursorUpperRight();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorLowerRightNothingThrown() {
        try {
            testCursor.setPosition(19);
            testCursor.moveCursorLowerRight();
        } catch (IllegalCursorException e) {
            fail("Exception not expected.");
        }
        assertEquals(19, testCursor.getOriginal());
        assertEquals(28, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorLowerRightBorderException() {
        try {
            testCursor.setPosition(63);
            testCursor.moveCursorLowerRight();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorUpperLeftNothingThrown() {
        try {
            testCursor.setPosition(19);
            testCursor.moveCursorUpperLeft();
        } catch (IllegalCursorException e) {
            fail("Exception not expected.");
        }
        assertEquals(19, testCursor.getOriginal());
        assertEquals(10, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorUpperLeftBorderException() {
        try {
            testCursor.moveCursorUpperLeft();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }

    @Test
    public void testMoveCursorLowerLeftNothingThrown() {
        try {
            testCursor.setPosition(19);
            testCursor.moveCursorLowerLeft();
        } catch (IllegalCursorException e) {
            fail("Exception not expected.");
        }
        assertEquals(19, testCursor.getOriginal());
        assertEquals(26, testCursor.getCurrent());
    }

    @Test
    public void testMoveCursorLowerLeftBorderException() {
        try {
            testCursor.setPosition(56);
            testCursor.moveCursorLowerLeft();
            fail("Exception not thrown.");
        } catch (IllegalCursorException e) {

        }
    }
}
