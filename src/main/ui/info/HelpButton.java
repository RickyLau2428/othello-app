package ui.info;

import ui.BoardRender;
import ui.OthelloGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class HelpButton extends JPanel implements ActionListener {
    private OthelloGame game;
    private JButton button;
    private BoardRender br;

    // EFFECTS: Initializes a button that displays valid positions when pressed
    public HelpButton(OthelloGame game, BoardRender br) {
        this.game = game;
        this.br = br;
        setPreferredSize(new Dimension(300, 100));
        setBackground(Color.WHITE);
        setLayout(null);
        initializeButton();
        add(button);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Sets the appropriate values for the size of the button and its listener
    public void initializeButton() {
        button = new JButton("Get Help");
        button.setToolTipText("Press this button to see valid moves.");
        button.setActionCommand("help");
        button.addActionListener(this);
        button.setBounds(0, 0, 300, 100);
    }

    // MODIFIES: this
    // EFFECTS: Draws the help icon onto the appropriate squares on the board
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("help".equals(e.getActionCommand())) {
            Set<Integer> validPositions = game.displayValidMoves();
            for (int posn : validPositions) {
                br.getBoard().get(posn).setPieceImage(BoardRender.HELP_ICON);
            }
            br.repaint();
        }
    }
}
