import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Maze editor panel for a maze solver
 * JPanel created inside MazeEditor
 * Displays an editable maze
 * Uses mouse events to allow user to edit
 *
 * @author Asif Rahman
 * @version 28/05/2023
 */
public class EditorPanel extends MazePanel implements MouseListener, MouseMotionListener {

    public enum DrawableBlocks {
        // Gets the file paths of the images using static methods
        TREE(Tree.getImagePath()),
        RANGER(Ranger.getDefaultImagePath()),
        CABIN(Trail.TraversalState.CABIN.getFilePath());

        // File path of the corresponding image
        private final String filepath;

        // Sets filepath
        DrawableBlocks(String filepath) {
            this.filepath = filepath;
        }

        // getter for the filepath
        public String getFilepath() {
            return filepath;
        }

    }

    public EditorPanel(int mazeHeight, int mazeWidth, int panelHeight, int panelWidth) {
        // Calls MazePanel constructor
        super(mazeHeight, mazeWidth, panelHeight, panelWidth);


    }

    @Override
    public void paintComponent(Graphics g) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
