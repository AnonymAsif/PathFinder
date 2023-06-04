import javax.swing.DefaultListCellRenderer;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Stack;
import java.awt.Graphics;
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
public class PathFinder extends MazePanel implements ActionListener
{
    // Number of ms between timer events
    private static int updateTime = 75;

    // Maps traversal states to the direction of movement
    private static EnumMap<Trail.TraversalState, Directions> stateDirections = null;
    
    // Stack for Depth First Search
    // Contains Coordinate2D for location in maze
    private final Stack<Coordinate2D> traversalStack;

    // The default direction of the ranger is south
    private final Directions DEFAULT_DIRECTION = Directions.SOUTH;

    // Direction that the Ranger is currently facing
    private Directions currentDirection;
    
    // Timer to animate the DFS with the ranger
    private final Timer timer;

    // ArrayList of event listeners to fire events on
    private final ArrayList<PathFinderListener> listeners;
    
    // Constructor
    public PathFinder(int mazeHeight, int mazeWidth, int panelHeight, int panelWidth, PathBlock[][] maze) {
        // Calls MazePanel constructor
        super(mazeHeight, mazeWidth, panelHeight, panelWidth, maze);

        // Initializes stack of indices
        traversalStack = new Stack<>();

        // Adds the rangers starting index to the Stack
        traversalStack.push(startIndex);

        // Facing default direction to start purely for design
        currentDirection = DEFAULT_DIRECTION;

        // Initializes the EnumMap if it is null
        if (stateDirections == null) {
            stateDirections = new EnumMap<>(Trail.TraversalState.class);

            // Adds 4 directions
            stateDirections.put(Trail.TraversalState.DISCOVERED_N, Directions.NORTH);
            stateDirections.put(Trail.TraversalState.DISCOVERED_E, Directions.EAST);
            stateDirections.put(Trail.TraversalState.DISCOVERED_S, Directions.SOUTH);
            stateDirections.put(Trail.TraversalState.DISCOVERED_W, Directions.WEST);
        }

        // Creates timer object for animation
        timer = new Timer(updateTime, this);

        // Initializes ArrayList of listeners
        listeners = new ArrayList<>();
    }
    
    // Action performed called by timer
    public void actionPerformed(ActionEvent e) {
        // If the stack is empty, the maze cannot be solved
        // End the PathFinder, giving false as no path was found
        if (traversalStack.isEmpty()) {
            endPathFinder(false);
            return;
        }

        // Location of ranger
        Coordinate2D rangerIndex = traversalStack.peek();
        
        // Variables with short names for convenience
        int col = rangerIndex.x();
        int row = rangerIndex.y();

        
        // If the ranger is not on a Trail, it is likely on a Tree
        // Throw an Exception because that should never happen
        if (!(maze[row][col] instanceof Trail currentBlock)) {
            throw new IllegalStateException("Ranger must be on a Trail");
        }

        // Handles the new state of the current block
        // All previous states should have been handled already
        switch (currentBlock.nextState()) {
            // Attempts to add the block in that direction to the stack
            // Uses the state direction that corresponds with the current traversal state
            case DISCOVERED_N, DISCOVERED_E, DISCOVERED_S, DISCOVERED_W ->
                    addIfUndiscovered(col, row, stateDirections.get(currentBlock.getTraversalState()));

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
                col = traversalStack.peek().x();
                row = traversalStack.peek().y();

                // Updates the current direction based on the traversal state of the previous Trail
                currentDirection = stateDirections.get(((Trail)maze[row][col]).getTraversalState());
            }

            // Successfully found the cabin, end the PathFinder
            case CABIN -> endPathFinder(true);

            // The next state of a block should never be UNDISCOVERED, since it's the first state
            case UNDISCOVERED -> throw new IllegalStateException("Next state of Block is UNDISCOVERED");
        }

        // Repaints the panel
        repaint();
    }

    // Tries to move the ranger in the given direction
    // Takes the current location of the ranger and direction af movement
    private void addIfUndiscovered(int xLoc, int yLoc, Directions movementDirection) {
        // Since the ranger is trying to move to this square,
        // it should face the movement direction
        currentDirection = movementDirection;

        // Calculates the new movement direction
        int newX = xLoc + movementDirection.getMove().x();
        int newY = yLoc + movementDirection.getMove().y();

        // Makes sure that the new coordinate is valid
        if (newX < 0 || newX >= mazeWidth) return;
        if (newY < 0 || newY >= mazeHeight) return;

        // If the block is not a Trail (it is a Tree),
        // Then the ranger cannot traverse it, return
        if (!(maze[newY][newX] instanceof Trail newTrail)) return;

        // If the new Trail has already been discovered then
        // It is on the stack and should not be pushed again
        if (newTrail.isDiscovered()) return;

        // Add the index to the stack since it is valid
        traversalStack.push(new Coordinate2D(newX, newY));
    }
    
    // Override of paintComponent to draw
    // Draws the ranger as the PathBlocks are drawn in superclass
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        // Coordinates to draw the Ranger
        int x;
        int y;

        // Get the coordinates from the stack if possible
        if (!traversalStack.isEmpty()) {
            // Gets the coordinates in grid of ranger
            Coordinate2D rangerIndex = traversalStack.peek();

            // Calculates the x and y coordinates in pixels of ranger
            x = rangerIndex.x() * blockWidth;
            y = rangerIndex.y() * blockHeight;
        }

        // Otherwise draw the ranger at the start
        else {
            x = startIndex.x() * blockWidth;
            y = startIndex.y() * blockHeight;
        }

        // Draws ranger
        ranger.draw(this, graphics, currentDirection, x, y, blockWidth, blockHeight);
    }

    // Overrides the setter for startIndex, since the stack needs to be updated as well
    @Override
    public void setStartIndex(Coordinate2D startIndex) {
        // Validates and sets the new start index
        super.setStartIndex(startIndex);

        // Updates the stack to contain only the new index
        traversalStack.clear();
        traversalStack.push(startIndex);

        // Makes sure the ranger faces default direction when reset
        currentDirection = DEFAULT_DIRECTION;

        // Repaints to show updates
        repaint();
    }

    // Setter for the updateTime variable
    // Updates the timer delay with new speed
    public void setUpdateTime(int updateTime) {
        // saves new update time and sets timer delay
        PathFinder.updateTime = updateTime;
        timer.setDelay(updateTime);
    }

    // Resets the PathFinder by resetting the maze, ranger and current direction and
    // Fires a reset event
    public void resetPathFinder() {
        // Resets the state of the Ranger
        ranger.resetSuccess();

        // Fires a reset event,
        // Event listeners should set the new maze
        fireReset();

        // Redraws the reset PathFinder panel
        repaint();
    }

    // Starts timer and fires timerStarted
    public void start() {
        timer.start();
        fireTimerStarted();
    }

    // Stops timer and fires timerStopped
    public void stop() {
        timer.stop();
        fireTimerStopped();
    }

    // Skips one animation frame and fires frameSkipped
    // Event can be null as it is unused
    public void skip() {
        actionPerformed(null);
        fireFrameSkipped();
    }

    // Adds a PathFinderListener to fire events to
    public void addPathFinderListener(PathFinderListener listener) {
        // The listener cannot be null
        if (listener == null) {
            throw new IllegalArgumentException("Event listener cannot be null.");
        }

        // Adds the listener
        listeners.add(listener);
    }

    // Attempts to remove a PathFinderListener to fire events to
    // Does nothing if the listener is not present
    public void removePathFinderListener(PathFinderListener listener) {
        listeners.remove(listener);
    }

    // Ends the PathFinder animation,
    // The maze was either fully explored or solved
    private void endPathFinder(boolean pathFound) {
        // Stops the timer
        timer.stop();

        // Clears the stack
        traversalStack.clear();

        // Set the success state of the ranger based on if it found a path
        ranger.setSuccess(pathFound);

        // Repaints to show the rangers new image
        repaint();

        // If a path was found, fire the pathFound event
        // Otherwise fire the noPathFound event
        if (pathFound) {
            firePathFound();
        }
        else fireNoPathFound();
    }

    // Fires a pathFound event as the maze was solved
    private void firePathFound() {
        // If the stack is empty the ranger is at the start
        Coordinate2D rangerLocation = traversalStack.isEmpty() ? startIndex : traversalStack.peek();

        // Fires the event for every added listener
        for (PathFinderListener listener : listeners) {
            listener.pathFound(new PathFinderEvent(this, rangerLocation, currentDirection));
        }
    }

    // Fires a noPathFound event as the maze was fully explored
    private void fireNoPathFound() {
        // If the stack is empty the ranger is at the start
        Coordinate2D rangerLocation = traversalStack.isEmpty() ? startIndex : traversalStack.peek();

        // Fires the event for every added listener
        for (PathFinderListener listener : listeners) {
            listener.noPathFound(new PathFinderEvent(this, rangerLocation, currentDirection));
        }
    }

    // Fires a timerStarted event
    private void fireTimerStarted() {
        // If the stack is empty the ranger is at the start
        Coordinate2D rangerLocation = traversalStack.isEmpty() ? startIndex : traversalStack.peek();

        // Fires the event for every added listener
        for (PathFinderListener listener : listeners) {
            listener.timerStarted(new PathFinderEvent(this, rangerLocation, currentDirection));
        }
    }

    // Fires a timerStopped event
    private void fireTimerStopped() {
        // If the stack is empty the ranger is at the start
        Coordinate2D rangerLocation = traversalStack.isEmpty() ? startIndex : traversalStack.peek();

        // Fires the event for every added listener
        for (PathFinderListener listener : listeners) {
            listener.timerStopped(new PathFinderEvent(this, rangerLocation, currentDirection));
        }
    }

    // Fires a frameSkipped event
    private void fireFrameSkipped() {
        // If the stack is empty the ranger is at the start
        Coordinate2D rangerLocation = traversalStack.isEmpty() ? startIndex : traversalStack.peek();

        // Fires the event for every added listener
        for (PathFinderListener listener : listeners) {
            listener.frameSkipped(new PathFinderEvent(this, rangerLocation, currentDirection));
        }
    }

    // Fires a reset event
    private void fireReset() {
        // If the stack is empty the ranger is at the start
        Coordinate2D rangerLocation = traversalStack.isEmpty() ? startIndex : traversalStack.peek();

        // Fires the event for every added listener
        for (PathFinderListener listener : listeners) {
            listener.reset(new PathFinderEvent(this, rangerLocation, currentDirection));
        }
    }
}
