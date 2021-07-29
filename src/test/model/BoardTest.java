package model;

import static model.State.*;

// Superclass for methods useful in testing game boards
public class BoardTest {
    // MODIFIES: gb
    // EFFECTS: Places pieces on the test board regardless of game rules
    public void setPiece(GameBoard gb, int position, State state) {
        gb.getBoard().get(position).setState(state);
    }

    // MODIFIES: gb
    // EFFECTS: Returns the state of the game piece on the board
    public State getPieceState(GameBoard gb, int position) {
        return gb.getBoard().get(position).getState();
    }

    // MODIFIES: gb
    // EFFECTS: Returns the number of active pieces on the board (state != EMPTY)
    public int getActivePiecesNum(GameBoard gb) {
        int counter = 0;
        for (GamePiece g : gb.getBoard()) {
            if (!(g.getState().equals(EMPTY))) {
                counter++;
            }
        }
        return counter;
    }

    // MODIFIES: gb
    // EFFECTS: Removes all active pieces from play
    public void clearBoard(GameBoard gb) {
        for (GamePiece g : gb.getBoard()) {
            if (!(g.getState().equals(EMPTY))) {
                g.setState(EMPTY);
            }
        }
    }

    // MODIFIES: gb
    // EFFECTS: Counts all pieces on the board and stores them in their respective local counters. Does nothing
    //          if piece state is EMPTY
    public void countPieces(GameBoard gb) {
        int clearPieceCounter = 0;
        int fillPieceCounter = 0;
        for (int i = 0; i <= 63; i++) {
            if (getPieceState(gb, i).equals(CLEAR)) {
                clearPieceCounter++;
            } else if (getPieceState(gb, i).equals(FILL)) {
                fillPieceCounter++;
            }
        }
        gb.setPieceCount(CLEAR, clearPieceCounter);
        gb.setPieceCount(FILL, fillPieceCounter);
    }
}
