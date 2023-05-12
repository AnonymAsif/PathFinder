
/**
 * Trail is a PathBlock
 * Represents a walkable path
 * Can be traversed for searching
 * Saves the states of neighbours during traversal
 *
 * @author Asif Rahman
 * @version 09/05/2023
 */
public class Trail extends PathBlock
{
    // Current state of the trail during traversal
    // Explored means all possible paths 
    // From here have also been explored
    public enum TraversalState {
        UNDISCOVERED,
        DISCOVERED,
        EXPLORED
    }

    // The traversal state of this block
    private TraversalState traversalState;

    // Constructor
    public Trail(int x, int y, int width, int height) {
        // Calls PathBlock constructor
        super(x, y, width, height);
        
        // Every path is undiscovered by default
        traversalState = TraversalState.UNDISCOVERED;
    }
    
    // Getter for traversalState
    public TraversalState getTraversalState() {
        return traversalState;
    }

    // Setter for traversalState
    public void setTraversalState(TraversalState newState) {
        this.traversalState = newState;
    }
}
