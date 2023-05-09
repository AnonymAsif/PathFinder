import javax.swing.JPanel;
import java.util.Stack;

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
    private static final int MAZE_HEIGHT = 8;
    private static final int MAZE_WIDTH = 8;
    
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
            for (int j = 0; j < MAZE_WIDTH; j++)
                maze[i][j] = new Trail(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
                
        traversalStack = new Stack<>();
    }
}
