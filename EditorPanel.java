import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

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

    // Enum of the different drawable blocks in the maze
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
        public String getFilePath() {
            return filepath;
        }
        
        // Method that returns an array of ImageIcons
        // Created from all the file paths
        public static ImageIcon[] getDrawableBlockIcons() {
            return Arrays.stream(values()) // Gets every DrawableBlock
                    .map(item -> new ImageIcon(item.getFilePath())) // Creates an ImageIcon with its file path
                    .toArray(ImageIcon[]::new); // Creates an ImageIcon[] from the Stream<ImageIcon> 
        }
    }
    
    // The current block to draw when the user clicks on a block
    private DrawableBlocks currentIcon = DrawableBlocks.TREE;

    public EditorPanel(int mazeHeight, int mazeWidth, int panelHeight, int panelWidth) {
        // Calls MazePanel constructor
        super(mazeHeight, mazeWidth, panelHeight, panelWidth);
    
        // Maze was initialized by super constructor
        // Initializes all PathBlocks in maze to Trails by default
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                // Trail is a subclass of Rectangle, so pass in coordinates and dimensions
                maze[i][j] = new Trail(j * blockWidth, i * blockHeight, blockWidth, blockHeight);
            }
        }
        
        repaint();
    }
    
    // Sets a new currentIcon value as the selected icon changed
    // Based on the index of the selected icon
    public void setCurrentIcon(int index) {
        // Updates currentIcon to the value at the given index in DrawableBlocks
        currentIcon = DrawableBlocks.values()[index];
    }

    @Override
    public void paintComponent(Graphics graphics) {
        // Calls MazePanel paintComponent method
        super.paintComponent(graphics);
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // switch (currentIcon) {
            // case TREE -> ;
            // case RANGER -> return;
            // case CABIN -> return;
        // }
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {}
}
