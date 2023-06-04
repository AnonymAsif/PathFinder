import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Maze editor class for a maze solver
 * JDialog window created on top of main panel
 * Implements ListSelectionListener to listen to changes in JList
 *
 * @author Asif Rahman
 * @version 28/05/2023
 */
public class MazeEditor extends JDialog implements ListSelectionListener {

    // PathFinder object that will receive the updates to the maze
    private final PathFinder pathfinder;

    // The JPanel of the editor that displays the edited maze
    private final EditorPanel editorPanel;
    
    // ButtonPanel that contains buttons for editorPanel actions
    private final ButtonPanel buttonPanel;

    // JList of icons for the user to choose when editing
    private final JList<Icon> iconList;

    // File handler to save and load mazes to solve
    private final MazeFileHandler fileHandler;

    // Constructor
    public MazeEditor(PathFinder pathfinder, MazeFileHandler fileHandler) {
        // Saves PathFinder
        this.pathfinder = pathfinder;

        // Creates new file handler
        this.fileHandler = fileHandler;

        // Sets up frame
        setTitle("Maze Editor");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Sets editor as a modal window
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        // Initializes editor panel using dimensions from pathfinder and maze from file handler
        editorPanel = new EditorPanel(pathfinder.getMazeHeight(), pathfinder.getMazeWidth(),
                pathfinder.getPanelHeight(), pathfinder.getPanelWidth(), fileHandler.getMaze());

        // Sets cabin and ranger indices in editorPanel using the file handler
        editorPanel.setCabinIndex(fileHandler.getCabinIndex());
        editorPanel.setStartIndex(fileHandler.getRangerIndex());

        // Gets an array of icons from the EditorStates enum
        ImageIcon[] editorIcons = EditorPanel.EditorStates.getDrawableBlockIcons();

        // Creates JList of icons to put on the right
        // Limits selections to one at a time
        iconList = new JList<>(editorIcons);
        iconList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listens to the JList events
        iconList.addListSelectionListener(this);
        
        // Creates a new buttonUI for the ButtonPanel
        WorldsBestButtonUI ui = new WorldsBestButtonUI(Color.LIGHT_GRAY, Color.BLACK);
        
        // There will be two buttons, Clear and Apply
        // Each button has a name and an action corresponding to a public method in pathfinder
        String[] buttonNames = new String[] {"Clear", "Apply"};
        ButtonAction[] buttonActions = new ButtonAction[] {
          editorPanel::clear, this::applyMaze
        };
        
        // Creates ButtonPanel by passing in the names, actions and states of the buttons
        // Also passes in the width of the panel, background colour and the ButtonUI
        buttonPanel = new ButtonPanel(buttonNames, buttonActions, editorPanel.getWidth(), Color.ORANGE, ui);

        // Adds components and packs
        getContentPane().add(editorPanel);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(new JScrollPane(iconList), BorderLayout.EAST);
        pack();
    }

    // Applies new maze by setting it in pathfinder
    // Returns boolean of if the maze was applied
    public boolean applyMaze() {
        // Makes sure editorPanel has a valid maze
        // Lets user know and returns if it isn't
        if (!editorPanel.isMazeValid()) {
            JOptionPane.showMessageDialog(this, // Parent component is this
                    "Maze Editor does not have a valid maze.\n" +
                            "Please ensure that a ranger and cabin have been placed.", // Dialog message
                    "Invalid Message", // Title of Dialog box
                    JOptionPane.ERROR_MESSAGE); // Warning message
            return false;
        }

        // Double checks that the user wants to apply the maze
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you would like to apply the maze?");

        // If the user chooses yes, apply the maze
        // Otherwise do nothing
        if (choice == JOptionPane.YES_OPTION) {
            // Gives the maze created by editorPanel to the file handler
            fileHandler.setMaze(editorPanel.getMaze());

            // Sets the ranger index in file handler, since the maze changed
            fileHandler.setRangerIndex(editorPanel.getStartIndex());

            // Stops the timer in pathfinder if it is currently running
            pathfinder.stop();

            // Gets a copy of the maze from fileHandler and sets it in pathFinder
            pathfinder.setMaze(fileHandler.getMaze());

            // Gets the ranger index from fileHandler and sets it in pathfinder
            pathfinder.setStartIndex(fileHandler.getRangerIndex());

            // Lets user know pathfinder has been updated
            JOptionPane.showMessageDialog(this, "Maze updated in pathfinder.");

            // The maze was updated
            return true;
        }

        // Lets user know the operation has been cancelled
        JOptionPane.showMessageDialog(this, "Maze update cancelled.");
        return false;
    }

    // Method to read new maze in EditorPanel
    public void readMazeFromFile(File mazeFile) {
        // Tries to read the maze from file handler
        // If it works, apply it in the editorPanel and set visible to let the user know
        try {
            fileHandler.readMaze(mazeFile);

            // Since the reading worked
            // Set the maze, then the cabin and ranger indices in editorPanel
            editorPanel.setMaze(fileHandler.getMaze());
            editorPanel.setStartIndex(fileHandler.getRangerIndex());
            editorPanel.setCabinIndex(fileHandler.getCabinIndex());


            // Since a new maze was read, in the file handler, stop and reset the pathfinder
            pathfinder.stop();
            pathfinder.resetPathFinder();

            // Set visible to let the user know that something changed in the editor
            setVisible(true);
        }

        // If it didn't work, let the user know
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, // Centers on panel
                    "An error has occurred when reading the file: " + e.getMessage(), // Message in dialog
                    "Error: "  + e.getMessage(), // Puts cause of error in title
                    JOptionPane.ERROR_MESSAGE); // It is an error
        }
    }

    // Method to save the maze in file handler to file
    public void saveMazeToFile(File mazeFile) {

        // Attempts to save the maze to the file
        try {
            fileHandler.writeMaze(mazeFile);

            // Lets user know if it worked
            JOptionPane.showMessageDialog(this, "Maze save operation successful.");
        }

        // If it didn't work show an error message
        catch (IOException e) {
            JOptionPane.showMessageDialog(this, // Centers on panel
                    "An error has occurred when writing to the file: " + e.getMessage(), // Message in dialog
                    "Error: "  + e.getMessage(), // Puts cause of error in title
                    JOptionPane.ERROR_MESSAGE); // It is an error
        }
    }

    // Fired when the selection of the JList changes
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // The index in the JList of the new selected icon
        int index = iconList.getSelectedIndex();
        
        // Updates the icon in editor panel
        editorPanel.setCurrentIcon(index);
    }

    // Getter for the file handler
    public MazeFileHandler getFileHandler() {
        return fileHandler;
    }
}
