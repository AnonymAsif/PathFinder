import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.EnumMap;
import java.util.Stack;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
public class PathFinder extends JPanel implements ActionListener
{
    // Record class used for 2D coordinates and moves
    public record Coordinate2D(int x, int y) {}

    // Enum of directions for pathfinding movement
    public enum Directions {
        NORTH(-1, 0), // moves one up
        EAST(0, 1), // moves one right
        SOUTH(1, 0), // moves one down
        WEST(0, -1); // moves one left  
        
        // 2d coordinates storing the x and y vectors
        private final Coordinate2D move;
        
        // Saves x and y to moves
        Directions(int x, int y) {
            move = new Coordinate2D(x, y);
        }

        // Getter method for move
        private Coordinate2D getMove() {
            return move;
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
    
    // Number of ms between timer events
    private static final int UPDATE_TIME = 500;

    // Maps traversal states to the direction of movement
    private static EnumMap<Trail.TraversalState, Directions> stateDirections = null;
    
    // 2D array of PathBlock
    private final PathBlock[][] maze;
    
    // Stack for Depth First Search
    // Contains Coordinate2D for location in maze
    private final Stack<Coordinate2D> traversalStack;

    // Ranger that will explore the maze
    private final Ranger ranger;

    // Starting location of the Ranger
    private final Coordinate2D startIndex;

    // Direction that the Ranger is currently facing
    private Directions currentDirection;
    
    // Timer to animate the DFS with the ranger
    private final Timer timer;
    
    // Constructor
    public PathFinder() {
        // Creates new maze
        maze = new PathBlock[MAZE_HEIGHT][MAZE_WIDTH];

        // Initializes stack of indices
        traversalStack = new Stack<>();
        
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

        // Creates a new Ranger at 0, 0
        ranger = new Ranger();
        startIndex = new Coordinate2D(0, 0);
        traversalStack.push(startIndex);

        // Facing south to start purely for design
        currentDirection = Directions.SOUTH;

        // Initializes the EnumMap if it is null
        if (stateDirections == null) {
            stateDirections = new EnumMap<>(Trail.TraversalState.class);

            // Adds 4 directions
            stateDirections.put(Trail.TraversalState.DISCOVERED_N, Directions.NORTH);
            stateDirections.put(Trail.TraversalState.DISCOVERED_E, Directions.EAST);
            stateDirections.put(Trail.TraversalState.DISCOVERED_S, Directions.SOUTH);
            stateDirections.put(Trail.TraversalState.DISCOVERED_W, Directions.WEST);
        }

        // sets preferred size
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        
        // Creates timer object for animation
        timer = new Timer(UPDATE_TIME, this);
        timer.start();
        

    }
    
    // Action performed called by timer
    public void actionPerformed(ActionEvent e) {
        // If the stack is empty, the maze cannot be solved
        // Stop the timer and do something with the Ranger
        if (traversalStack.isEmpty()) {
            timer.stop();
            return;
            // DO SOMETHING HERE
        }

        // Location of ranger
        Coordinate2D rangerIndex = traversalStack.peek();
        
        // Variables with short names for convenience
        int row = rangerIndex.x();
        int col = rangerIndex.y();

        
        // If the ranger is not on a Trail, it is likely on a Tree
        // Throw an Exception because that should never happen
        if (!(maze[col][row] instanceof Trail currentBlock)) {
            throw new IllegalStateException("Ranger must be on a Trail");
        }

        // Handles the new state of the current block
        // All previous states should have been handled already
        switch (currentBlock.nextState()) {
            // Attempts to add the block in that direction to the stack
            // Uses the state direction that corresponds with the current traversal state
            case DISCOVERED_N, DISCOVERED_E, DISCOVERED_S, DISCOVERED_W ->
                    addIfUndiscovered(row, col, stateDirections.get(currentBlock.getTraversalState()));

            // This path is fully explored, pop it from the stack
            // Updates the direction of the ranger
            case EXPLORED -> {
                traversalStack.pop();

                // Face south if the stack is empty
                if (traversalStack.isEmpty()) {
                    currentDirection = Directions.SOUTH;
                    return;
                }

                // Gets coordinates of previous Trail
                row = traversalStack.peek().x();
                col = traversalStack.peek().y();

                // Updates the current direction based on the traversal state of the previous Trail
                currentDirection = stateDirections.get(((Trail)maze[col][row]).getTraversalState());
            }

            // Successfully found the cabin, stop the timer
            case CABIN -> timer.stop();

            // The next state of a block should never be UNDISCOVERED, since it's the first state
            case UNDISCOVERED -> throw new IllegalStateException("Next state of Block is UNDISCOVERED");
        }

        // Repaints the panel
        repaint();
    }

    // Tries to move the ranger in the given direction
    // Takes the current location of the ranger and direction af movement
    public void addIfUndiscovered(int xLoc, int yLoc, Directions movementDirection) {
        // Calculates the new movement direction
        int newX = xLoc + movementDirection.getMove().x();
        int newY = yLoc + movementDirection.getMove().y();

        // Makes sure that the new coordinate is valid
        if (newX < 0 || newX >= MAZE_WIDTH) return;
        if (newY < 0 || newY >= MAZE_HEIGHT) return;

        // If the block is not a Trail (it is a Tree),
        // Then the ranger cannot traverse it, return
        if (!(maze[newY][newX] instanceof Trail newTrail)) return;

        // If the new Trail has already been discovered then
        // It is on the stack and should not be pushed again
        if (newTrail.isDiscovered()) return;

        // Add the index to the stack since it is valid
        traversalStack.push(new Coordinate2D(newX, newY));

        // Since the ranger is moving to this square,
        // it should face the movement direction
        currentDirection = movementDirection;
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

        // Coordinates to draw the Ranger
        int x;
        int y;

        // Get the coordinates from the stack if possible
        if (!traversalStack.isEmpty()) {
            // Gets the coordinates in grid of ranger
            Coordinate2D rangerIndex = traversalStack.peek();

            // Calculates the x and y coordinates in pixels of ranger
            x = rangerIndex.x() * BLOCK_WIDTH;
            y = rangerIndex.y() * BLOCK_HEIGHT;
        }

        // Otherwise draw the ranger at the start
        else {
            x = startIndex.x() * BLOCK_WIDTH;
            y = startIndex.y() * BLOCK_HEIGHT;
        }

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
