    import javax.swing.JPanel;
import java.util.Stack;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Dimension;

/**
 * PathFinder which solves a maze of PathBlocks
 * Uses Depth First Search 
 * 
 * Animates a ranger finding a cabin using a swing timer
 * Keeps track of position and uses a stack for DFS
 *
 * @author Asif Rahman
 * @version 09/05/2023
 */
public class PathFinder extends JPanel
{
    // Dimensions of the maze
    private static final int MAZE_HEIGHT = 12;
    private static final int MAZE_WIDTH = 12;
    
    // Dimensions of the panel
    private static final int PANEL_HEIGHT = 600;
    private static final int PANEL_WIDTH = 600;
    
    // Dimensions of each block
    private static final int BLOCK_HEIGHT = PANEL_HEIGHT / MAZE_HEIGHT;
    private static final int BLOCK_WIDTH = PANEL_WIDTH / MAZE_WIDTH;
    
    // 2D array of PathBlock
    private final PathBlock[][] maze;
    
    // Stack for Depth First Search
    private final Stack<int[]> traversalStack;
    
    // Constructor
    public PathFinder() {
        // Creates new maze
        maze = new PathBlock[MAZE_HEIGHT][MAZE_WIDTH];

        // Initializes each block
        // Every Block is a Trail by default until set otherwise
        // Multiplies by dimensions for no spacing between blocks
        for (int i = 0; i < MAZE_HEIGHT; i++)
            for (int j = 0; j < MAZE_WIDTH; j++) {
                if (i == 9)
                    maze[i][j] = new Tree(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
                else maze[i][j] = new Trail(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
            }

        for (int i = 6; i >= 1; i--) {
            for (int j = 1; j < i; j++) {
                for (PathBlock p : maze[j]) {
                    if (p instanceof Trail t) {
                        t.nextState();
                    }
                }

            }
        }



        
        // Initializes stack of indices
        traversalStack = new Stack<>();
        
        // sets preferred size
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }
    
    // Override of paintComponent to draw
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;

        // Increases the thickness of the lines
        // By making a new stroke
        g.setStroke(new BasicStroke(3));

        // Draws every block
        for (PathBlock[] blocks : maze) {
            for (PathBlock block : blocks) {
                // Draws the block and passes in the graphics context
                block.draw(this, g);
            }
        }

    }
}
