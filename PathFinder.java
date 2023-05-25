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
    // Enum of directions for pathfinding movement
    public enum Directions {
        NORTH(-1, 0), // moves one up
        EAST(0, 1), // moves one right
        SOUTH(1, 0), // moves one down
        WEST(0, -1); // moves one left  
        
        // int[2] storing the x and y vectors
        int[] moves;
        
        // Saves x and y to moves
        Directions(int x, int y) {
            moves = new int[]{x, y};
        }
    }
    
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
    // Contains int[2] of Directions
    private final Stack<int[]> traversalStack;

    // Ranger that will explore the maze
    private final Ranger ranger;

    // Direction that the Ranger is currently facing
    private Directions currentDirection;
    
    // Constructor
    public PathFinder() {
        // Creates new maze
        maze = new PathBlock[MAZE_HEIGHT][MAZE_WIDTH];
        
        int[][] trees = {
            {0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,1,0,0,0,1,0,1},
            {0,0,1,1,0,1,0,1,0,0,0,1},
            {0,1,1,0,0,1,0,1,0,1,1,1},
            {0,1,0,0,0,1,0,0,0,0,0,1},
            {0,1,1,0,1,1,1,1,1,0,0,1},
            {0,0,1,0,1,0,1,0,1,0,1,1},
            {1,1,1,0,1,0,0,0,1,0,1,1},
            {0,0,0,0,1,0,1,0,0,0,1,1},
            {1,0,1,0,1,1,1,1,0,1,1,1},
            {1,1,1,0,0,0,0,1,0,0,0,0}
        };

        // Initializes each block
        // Every Block is a Trail by default until set otherwise
        // Multiplies by dimensions for no spacing between blocks
        for (int i = 0; i < MAZE_HEIGHT; i++)
            for (int j = 0; j < MAZE_WIDTH; j++) {
                if (trees[i][j] == 1)
                    maze[i][j] = new Tree(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
                else maze[i][j] = new Trail(j * BLOCK_WIDTH, i * BLOCK_HEIGHT, BLOCK_WIDTH, BLOCK_HEIGHT);
            }

        // Initializes stack of indices
        traversalStack = new Stack<>();

        // Creates a new Ranger at 0, 0
        ranger = new Ranger(0, 0);

        // Facing south to start purely for design
        currentDirection = Directions.SOUTH;
        
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
                // Passes in the graphics context and this for the icon
                block.draw(this, g);
            }
        }

        // Gets the coordinates in grid of ranger
        int[] rangerIndex = ranger.getCurrentIndex();

        // Calculates the x and y coordinates in pixels of ranger
        int x = rangerIndex[0] * BLOCK_WIDTH;
        int y = rangerIndex[1] * BLOCK_HEIGHT;

        // Draws ranger
        ranger.draw(this, g, currentDirection, x, y, BLOCK_WIDTH, BLOCK_HEIGHT);

        // Draws the border around every block
        for (PathBlock[] blocks : maze) {
            for (PathBlock block : blocks) {
                // Passes in the graphics context only
                block.drawBorder(g);
            }
        }

    }
}
