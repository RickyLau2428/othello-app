package ui.info;

import model.GameBoard;
import ui.BoardRender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import static ui.info.InfoPanel.*;

// Represents the "Get Help" button on the GUI - shows all valid moves on board when pressed
public class HelpButton extends JPanel implements ActionListener {
    private GameBoard game;
    private JButton showMoves;
    private BoardRender br;

    // EFFECTS: Initializes a button that displays valid positions when pressed
    public HelpButton(GameBoard game, BoardRender br) {
        this.game = game;
        this.br = br;
        setPreferredSize(new Dimension(SECTION_WIDTH, SECTION_HEIGHT));
        setBackground(Color.WHITE);
        setLayout(null);
        showMoves = new JButton("Get Help");
        showMoves.setToolTipText("Press this button to see valid moves.");
        showMoves.setActionCommand("help");

        initializeButton(showMoves);
        add(showMoves);
        setVisible(true);
    }

    // getters:
    public void setGame(GameBoard game) {
        this.game = game;
    }

    // MODIFIES: this
    // EFFECTS: Sets the appropriate values for the size of the button and its listener
    public void initializeButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.addActionListener(this);
        button.setBounds(0, 0, SECTION_WIDTH, SECTION_HEIGHT);
    }

    // MODIFIES: this
    // EFFECTS: Draws the help icon onto the appropriate squares on the board
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("help".equals(e.getActionCommand())) {
            Set<Integer> validPositions = game.getValidMoveKeys();
            for (int posn : validPositions) {
                br.getBoard().get(posn).setPieceImage(BoardRender.HELP_ICON);
            }
            br.repaint();
        }
    }
}
