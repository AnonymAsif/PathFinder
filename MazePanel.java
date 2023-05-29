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
    }

    @Override
    abstract public void paintComponent(Graphics g);
}
