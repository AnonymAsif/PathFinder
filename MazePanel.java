import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 * Maze JPanel for a maze solver
 * Displays the maze
 * Used in both solver and editor panels
 *
 * @author Asif Rahman
 * @version 28/05/2023
 */
public abstract class MazePanel extends JPanel {
    // Record class used for 2D coordinates and moves
    public record Coordinate2D(int x, int y) {}

    // Enum of directions for pathfinding movement
    public enum Directions {
        NORTH(0, -1), // moves one up
        EAST(1, 0), // moves one right
        SOUTH(0, 1), // moves one down
        WEST(-1, 0); // moves one left

        // 2d coordinates storing the x and y vectors
        private final Coordinate2D move;

        // Saves x and y to moves
        Directions(int x, int y) {
            move = new Coordinate2D(x, y);
        }

        // Getter method for move
        public Coordinate2D getMove() {
            return move;
        }
    }

    // Dimensions of the maze
    protected final int mazeHeight;
    protected final int mazeWidth;

    // Dimensions of the panel
    protected final int panelHeight;
    protected final int panelWidth;

    // Dimensions of each block
    protected final int blockHeight;
    protected final int blockWidth;

    // Ranger that will explore the maze
    protected final Ranger ranger;

    // Starting location of the Ranger
    protected Coordinate2D startIndex;

    // 2D array of PathBlock
    protected PathBlock[][] maze;


    public MazePanel(int mazeHeight, int mazeWidth, int panelHeight, int panelWidth, PathBlock[][] maze) {
        // Sets maze and panel dimensions
        this.mazeHeight = mazeHeight;
        this.mazeWidth = mazeWidth;
        this.panelHeight = panelHeight;
        this.panelWidth = panelWidth;

        // Calculates dimensions of each block
        blockHeight = panelHeight / mazeHeight;
        blockWidth = panelWidth / mazeWidth;

        // Creates a new ranger at (0, 0)
        ranger = new Ranger();
        startIndex = new Coordinate2D(0, 0);

        // Saves maze
        this.maze = maze;
        
        // sets preferred size
        setPreferredSize(new Dimension(panelWidth, panelHeight));
    }

    // Calls the draw methods on every PathBlock
    @Override
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;

        // Increases the thickness of the lines
        // By making a new stroke
        g.setStroke(new BasicStroke(3));

        // Draws every block
        for (PathBlock[] blocks : maze) {
            for (PathBlock block : blocks) {
                // Passes in the graphics context and this for the icon
                block.draw(this, g);
            }
        }

        // Draws the border around every block
        for (PathBlock[] blocks : maze) {
            for (PathBlock block : blocks) {
                // Passes in the graphics context only
                block.drawBorder(g);
            }
        }
    }

    /* Getters for dimensions and coordinates */
    public int getMazeHeight() {
        return mazeHeight;
    }

    public int getMazeWidth() {
        return mazeWidth;
    }

    public int getPanelHeight() {
        return panelHeight;
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    /* Setter and Getter methods for maze */
    public PathBlock[][] getMaze() {
        return maze;
    }

    public void setMaze(PathBlock[][] maze) {
        // Setter throws exception given null maze
        if (maze == null)
            throw new IllegalArgumentException("Maze cannot be null");

        // Sets maze
        this.maze = maze;

        // Repaints since maze has been updated
        repaint();
    }

    /* Setter and Getter methods for maze */
    public Coordinate2D getStartIndex() {
        return startIndex;
    }

    // Setter throws exception given null or invalid index
    public void setStartIndex(Coordinate2D startIndex) {
        // Null index
        if (startIndex == null)
            throw new IllegalArgumentException("Start index cannot be null");

        // Given index is out of bounds
        if (startIndex.x() < 0 || startIndex.x() >= mazeWidth || // Invalid x
                startIndex.y() < 0 || startIndex.y() >= mazeHeight) // Invalid y
            throw new IllegalArgumentException("Start index out of bounds");

        // Sets start index
        this.startIndex = startIndex;

        // Repaints since maze has been updated
        repaint();
    }
}
