import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

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
        setPreferredSize(new Dimension(200, 200));

        // Sets editor as a modal window
        setModalityType(ModalityType.APPLICATION_MODAL);

        // Creates list of icons
        ImageIcon[] editorIcons = new ImageIcon[] {
          new ImageIcon("tree.png"), // Tree image
          new ImageIcon("Ranger/failure.png"), // Ranger image
          new ImageIcon("States/cabin.png") // Cabin image
        };

        // Creates JList of icons to put on the right
        // Limits selections to one at a time
        iconList = new JList<>(editorIcons);
        iconList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Listens to the JList events
        iconList.addListSelectionListener(this);

        // Adds components and packs
        getContentPane().add(new JScrollPane(iconList), BorderLayout.EAST);
        pack();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int index = e.getFirstIndex();
    }
}
