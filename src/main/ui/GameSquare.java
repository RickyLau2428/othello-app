package ui;

import java.awt.*;

// Represents a single square image on the game board in the GUI
public class GameSquare {
    private Image pieceImage;
    private int positionX;
    private int positionY;

    // EFFECTS: Initializes a square on the board at the given x- and y-positions with the given image from file
    public GameSquare(Image pieceImage, int positionX, int positionY) {
        this.pieceImage = pieceImage;
        this.positionX = positionX;
        this.positionY = positionY;
    }

    // getters:
    public Image getPieceImage() {
        return this.pieceImage;
    }

    public int getPositionX() {
        return this.positionX;
    }

    public int getPositionY() {
        return this.positionY;
    }

    // setters:
    public void setPieceImage(Image pieceImage) {
        this.pieceImage = pieceImage;
    }
}
