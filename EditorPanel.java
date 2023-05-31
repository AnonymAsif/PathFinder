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
    public enum EditorStates {
        // Gets the file paths of the images using static methods
        TREE(Tree.getImagePath()),
        RANGER(Ranger.getDefaultImagePath()),
        CABIN(Trail.TraversalState.CABIN.getFilePath()),

        // ERASER is unique to EditorPanel, get the file path directly
        ERASER("eraser.png");

        // File path of the corresponding image
        private final String filepath;

        // Sets filepath
        EditorStates(String filepath) {
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
    private EditorStates currentIcon = EditorStates.TREE;

    // The location of the cabin, null if it is not placed
    // There should not be more than one cabin
    private Coordinate2D cabinIndex = null;

    public EditorPanel(int mazeHeight, int mazeWidth, int panelHeight, int panelWidth) {
        // Calls MazePanel constructor
        super(mazeHeight, mazeWidth, panelHeight, panelWidth);
    
        // Clear method will setup all PathBlock objects to default
        clear();
        
        // Adds this class as a mouse and mouse motion listener
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    // Sets a new currentIcon value as the selected icon changed
    // Based on the index of the selected icon
    public void setCurrentIcon(int index) {
        // Updates currentIcon to the value at the given index in DrawableBlocks
        currentIcon = EditorStates.values()[index];
    }
    
    // Erases the given square in maze
    // If a ranger or cabin was there, set its index to null
    private void erase(int x, int y) {
        // If the block is a ranger, set start index to null
         if (startIndex != null && x == startIndex.x() && y == startIndex.y())
             startIndex = null;

         // Do the same for cabin, set its index to null if being erased
         else if (cabinIndex != null && x == cabinIndex.x() && y == cabinIndex.y()) {
             cabinIndex = null;
        }
            
         // Create a new Trail object in case it is a cabin or Tree
         maze[y][x] = new Trail(x * blockWidth, y * blockHeight, blockWidth, blockHeight);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        // Calls MazePanel paintComponent method
        super.paintComponent(graphics);

        // If the ranger is currently placed, draw the ranger
        // Draw the ranger facing SOUTH
        if (startIndex != null) {
            ranger.draw(this, graphics, Directions.SOUTH,
                    startIndex.x() * blockWidth, startIndex.y() * blockHeight, blockWidth, blockHeight);
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        // Calculates index in maze based on coordinates (pixels) of click
        // Truncates extra pixels and divides by block size to get index
        int x = (e.getX() - e.getX() % blockWidth) / blockWidth;
        int y = (e.getY() - e.getY() % blockHeight) / blockHeight;
        
        // If the index is invalid, return
        if (x < 0 || x > mazeWidth) return;
        if (y < 0 || y > mazeHeight) return;
        
        // Erases whatever was there previously
        erase(x, y);

        // Places the correct icon on the square based on currentIcon
         switch (currentIcon) {
             case TREE -> {
                 // Makes the block a Tree
                 maze[y][x] = new Tree(x * blockWidth, y * blockHeight, blockWidth, blockHeight);
             }
             case RANGER -> {
                 // Updates start index of ranger
                 startIndex = new Coordinate2D(x, y);
             }
             case CABIN -> {
                 // If there is another cabin, cabinIndex is not null
                 // remove that cabin and change the cabinIndex to this index
                 if (cabinIndex != null) {
                     int xInd = cabinIndex.x();
                     int yInd = cabinIndex.y();
                     ((Trail)maze[yInd][xInd]).setTraversalState(Trail.TraversalState.UNDISCOVERED);
                 }

                 // Sets the state of the Trail to CABIN and save cabin index
                 cabinIndex = new Coordinate2D(x, y);
                 ((Trail)maze[y][x]).setTraversalState(Trail.TraversalState.CABIN);
             }
             
             // Does nothing as current square was already erased
             case ERASER -> {}
         }

         // Repaint the panel
        repaint();
    }
    
    // Clears everything off of the maze
    public void clear() {
        // Sets start index and cabin index to null
        startIndex = null;
        cabinIndex = null;
        
        for (int i = 0; i < mazeHeight; i++) {
            for (int j = 0; j < mazeWidth; j++) {
                // Trail is a subclass of Rectangle, so pass in coordinates and dimensions
                maze[i][j] = new Trail(j * blockWidth, i * blockHeight, blockWidth, blockHeight);
            }
        }
    }
        
    
    // Calls the mousePressed method if the mouse is held above a square
    @Override
    public void mouseDragged(MouseEvent e) {
        mousePressed(e);
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
