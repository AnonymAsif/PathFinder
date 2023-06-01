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

    // Constructor
    public MazeEditor(PathFinder pathfinder) {
        // Saves PathFinder
        this.pathfinder = pathfinder;

        // Sets up frame
        setTitle("Maze Editor");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Sets editor as a modal window
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        // Initializes editor panel using dimensions from pathfinder
        editorPanel = new EditorPanel(pathfinder.getMazeHeight(), pathfinder.getMazeWidth(),
                pathfinder.getPanelHeight(), pathfinder.getPanelWidth());

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
    private void applyMaze() {
        // Makes sure editorPanel has a valid maze
        // Lets user know and returns if it isn't
        if (!editorPanel.isMazeValid()) {
            JOptionPane.showMessageDialog(this, // Parent component is this
                    "Maze Editor does not have a valid maze.\n" +
                            "Please ensure that a ranger and cabin have been placed.", // Dialog message
                    "Invalid Message", // Title of Dialog box
                    JOptionPane.ERROR_MESSAGE); // Warning message
            return;
        }

        // Double checks that the user wants to apply the maze
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you would like to apply the maze?");

        // If the user chooses yes, apply the maze
        // Otherwise do nothing
        if (choice == JOptionPane.YES_OPTION) {
            // Gets the maze from editorPanel and sets it in pathfinder
            pathfinder.setMaze(editorPanel.getMaze());

            // Gets the start index from editorPanel and sets it in pathfinder
            pathfinder.setStartIndex(editorPanel.getStartIndex());

            // Lets user know pathfinder has been updated
            JOptionPane.showMessageDialog(this, "Maze updated in pathfinder.");
        }

        // Lets user know the operation has been cancelled
        else JOptionPane.showMessageDialog(this, "Maze update cancelled.");
    }

    // Fired when the selection of the JList changes
    @Override
    public void valueChanged(ListSelectionEvent e) {
        // The index in the JList of the new selected icon
        int index = iconList.getSelectedIndex();
        
        // Updates the icon in editor panel
        editorPanel.setCurrentIcon(index);
    }
}
