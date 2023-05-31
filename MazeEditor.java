import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.BorderLayout;
import java.awt.Color;

/**
 * Maze editor class for a maze solver
 * JFrame window created on top of main panel
 *
 * @author Asif Rahman
 * @version 28/05/2023
 */
public class MazeEditor extends JDialog implements ListSelectionListener {
    // Dimensions of the maze
    private final int mazeHeight;
    private final int mazeWidth;

    // Dimensions of the panel
    private final int panelHeight;
    private final int panelWidth;
    
    // The JPanel of the editor that displays the edited maze
    private final EditorPanel editorPanel;
    
    // ButtonPanel that contains buttons for editorPanel actions
    private final ButtonPanel buttonPanel;

    // JList of icons for the user to choose when editing
    private final JList<Icon> iconList;

    // Constructor
    public MazeEditor(int mazeHeight, int mazeWidth, int panelHeight, int panelWidth) {
        // Saves maze and panel dimensions
        this.mazeHeight = mazeHeight;
        this.mazeWidth = mazeWidth;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;

        // Sets up frame
        setTitle("Maze Editor");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        // Sets editor as a modal window
        setModalityType(ModalityType.APPLICATION_MODAL);
        
        // Initializes editor panel
        editorPanel = new EditorPanel(mazeHeight, mazeWidth, panelHeight, panelWidth);

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
            
        };
        
        // Creates ButtonPanel by passing in the names, actions and states of the buttons
        // Also passes in the width of the panel, background colour and the ButtonUI
        buttonPanel = new ButtonPanel(buttonNames, buttonActions, editorPanel.getWidth(), Color.ORANGE, ui);

        // Adds components and packs
        getContentPane().add(editorPanel);
        getContentPane().add(new JScrollPane(iconList), BorderLayout.EAST);
        pack();
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
