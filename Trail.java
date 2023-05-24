import java.awt.*;

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
        UNDISCOVERED("States/undiscovered.png", new Color(0, 0, 0, 0)),
        DISCOVERED_N("States/discoveredNorth.png", Color.BLUE),
        DISCOVERED_E("States/discoveredEast.png", Color.CYAN),
        DISCOVERED_S("States/discoveredSouth.png", Color.ORANGE),
        DISCOVERED_W("States/discoveredWest.png", Color.YELLOW),
        EXPLORED("States/explored.png", Color.RED),
        CABIN("States/cabin.png", Color.BLACK);

        // File path of the image
        private final String filepath;

        // Default colour to use if the icon was not loaded
        private final Color defaultColour;

        // Constructor that saves instance fields
        TraversalState(String filepath, Color defaultColour) {
            this.filepath = filepath;
            this.defaultColour = defaultColour;
        }

        // Getter methods
        public String getFilePath() {
            return filepath;
        }

        public Color getDefaultColour() {
            return defaultColour;
        }
    }

    // The traversal state of this block
    private TraversalState traversalState;

    // Constructor
    public Trail(int x, int y, int width, int height) {
        // Calls PathBlock constructor
        super(x, y, width, height);
        
        // Every path is undiscovered by default
        traversalState = TraversalState.UNDISCOVERED;

        // Updates the current Icon to the image given by traversalState
        updateIcon(traversalState.getFilePath(), traversalState.getDefaultColour());
    }
    
    // Getter for traversalState
    public TraversalState getTraversalState() {
        return traversalState;
    }

    // Setter for traversalState
    public void setTraversalState(TraversalState newState) {
        this.traversalState = newState;
    }

    // Updates traversalState to the next state
    // Then returns the new traversalState
    public TraversalState nextState() {
        // This method should never be called on a fully explored Trail
        if (traversalState == TraversalState.EXPLORED) {
            throw new IllegalStateException("Cannot get the next state of an explored Trail.");
        }

        // Ordinal of the new state will be one higher than the old one
        int newIndex = traversalState.ordinal() + 1;

        // Updates traversalState to the state at the new ordinal
        traversalState = TraversalState.values()[newIndex];

        // Updates Icon and default colour to the icon of the new state
        updateIcon(traversalState.getFilePath(), traversalState.getDefaultColour());

        // Returns traversalState after updating for cleaner code
        return traversalState;
    }
}
