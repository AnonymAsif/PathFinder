import javax.swing.*;
import java.awt.*;

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
    protected final Coordinate2D startIndex;

    // 2D array of PathBlock
    protected final PathBlock[][] maze;


    public MazePanel(int mazeHeight, int mazeWidth, int panelHeight, int panelWidth) {
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

        // Creates new maze
        maze = new PathBlock[mazeHeight][mazeWidth];
        
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
}
