package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

// Represents a menu in the GUI that has saving and loading behaviour
public class Menu implements ActionListener {
    private OthelloApp parent;
    private JMenuBar menuBar;
    private JMenu menu;
    private JMenuItem saveBoard;
    private JMenuItem loadBoard;
    private JFileChooser fileChooser;

    // EFFECTS: Creates a menu that has options for saving and loading the current game
    public Menu(OthelloApp parent) {
        this.parent = parent;
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        saveBoard = new JMenuItem("Save Game...", KeyEvent.VK_S);
        loadBoard = new JMenuItem("Load Game...", KeyEvent.VK_L);
        initializeMenuItem(saveBoard, "save");
        initializeMenuItem(loadBoard, "load");
        fileChooser = new JFileChooser();
        menu.add(saveBoard);
        menu.add(loadBoard);
        menuBar.add(menu);
    }

    // getters:
    public JMenuBar getMenuBar() {
        return menuBar;
    }

    // MODIFIES: item
    // EFFECTS: Helper method for initializing menu items - adds the menu as an action listener for the given item
    //          and sets its action command to command.
    public void initializeMenuItem(JMenuItem item, String command) {
        item.setActionCommand(command);
        item.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: Opens a file dialog for loading files
    public void openFileDialog() {
        fileChooser.setCurrentDirectory(new File("./data/saves"));
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();
            parent.loadSave(selected);
        }
    }

    // MODIFIES: this
    // EFFECTS: Opens a file dialog for saving files
    public void saveFileDialog() {
        fileChooser.setCurrentDirectory(new File("./data/saves"));
        fileChooser.setDialogTitle("Save the file as a .json");
        int selection = fileChooser.showSaveDialog(parent);
        if (selection == JFileChooser.APPROVE_OPTION) {
            File toSave = fileChooser.getSelectedFile();
            parent.saveFile(toSave);
        }
    }

    // MODIFIES: this
    // EFFECTS: Calls the appropriate file dialog method depending on the menu item clicked
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("load".equals(e.getActionCommand())) {
            openFileDialog();
        } else if ("save".equals(e.getActionCommand())) {
            saveFileDialog();
        }
    }
}
