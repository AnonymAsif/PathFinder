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

    // The location of the cabin, null if it is not placed
    // There should not be more than one cabin
    private Coordinate2D cabinIndex = null;

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
        
        // Adds this class as a mouse and mouse motion listener
        addMouseListener(this);
        addMouseMotionListener(this);
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

        System.out.printf("x: %d, y: %d\n", x, y);
        System.out.println(currentIcon.toString());

        // Places the correct icon on the square based on currentIcon
         switch (currentIcon) {
             case TREE -> {
                 // If the square is already a tree do nothing
                 if (maze[y][x] instanceof Tree) return;

                 // If the square is where the ranger is (start index)
                 // Set the start index to null and replace it
                 if (startIndex != null && startIndex.x() == x && startIndex.y() == y)
                     startIndex = null;

                 // If the block is a Trail and has a Cabin, set cabinIndex to null as it was replaced
                 else if (maze[y][x] instanceof Trail trail && trail.getTraversalState() == Trail.TraversalState.CABIN) {
                     cabinIndex = null;
                 }

                 // Makes the block a Tree
                 maze[y][x] = new Tree(x * blockWidth, y * blockHeight, blockWidth, blockHeight);
             }
             case RANGER -> {
                 // If the square is where the ranger already is (start index), return
                 if (startIndex != null && startIndex.x() == x && startIndex.y() == y)
                     return;

                 // The ranger cannot start on a tree
                 // If the block is Tree, replace it with a new Trail
                 if (maze[y][x] instanceof Tree)
                     maze[y][x] = new Trail(x * blockWidth, y * blockHeight, blockWidth, blockHeight);

                 // If the block is a Trail and has a Cabin, replace it with UNDISCOVERED
                 else if (maze[y][x] instanceof Trail trail && trail.getTraversalState() == Trail.TraversalState.CABIN) {
                     trail.setTraversalState(Trail.TraversalState.UNDISCOVERED);

                     // Sets cabinIndex to null since it was replaced
                     cabinIndex = null;
                 }

                 // Updates start index of ranger
                 startIndex = new Coordinate2D(x, y);
             }
             case CABIN -> {
                 // If the block is a trail, and it is a cabin, return
                 if (maze[y][x] instanceof Trail trail) {
                     if (trail.getTraversalState() == Trail.TraversalState.CABIN) return;
                 }

                 // The cabin cannot be on a tree
                 // If the block is Tree, replace it with a new Trail
                 else maze[y][x] = new Trail(x * blockWidth, y * blockHeight, blockWidth, blockHeight);

                 // If the square is where the ranger is (start index)
                 // Set the start index to null and replace it with a cabin
                 if (startIndex != null && startIndex.x() == x && startIndex.y() == y)
                     startIndex = null;

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
         }

         // Repaint the panel
        repaint();
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
