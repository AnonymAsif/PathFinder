import java.awt.*;
import javax.swing.ImageIcon;

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
        DISCOVERED_E("States/discoveredEast.png", Color.GREEN),
        DISCOVERED_S("States/discoveredSouth.png", Color.ORANGE),
        DISCOVERED_W("States/discoveredWest.png", Color.YELLOW),
        EXPLORED("States/explored.png", Color.RED),
        CABIN("States/cabin.png", Color.BLACK);

        // Saves if the icon was properly loaded with a valid file path
        private final boolean validImage;

        // Icon pertaining to the State
        private ImageIcon icon;

        // Default colour to use if the icon was not loaded
        private final Color defaultColour;

        TraversalState(String filepath, Color defaultColour) {
            // Saves default colour in case of invalid icon
            this.defaultColour = defaultColour;

            // Gets the URL of the file path to be validated
            java.net.URL imageURL = getClass().getResource(filepath);

            // If the URL is valid, create an ImageIcon
            if (imageURL != null) {
                validImage = true;
                icon = new ImageIcon(filepath);
            }
            // If the URL is invalid
            else validImage = false;
        }

        // Getter methods
        public boolean hasImage() {
            return validImage;
        }

        public ImageIcon getIcon() {
            return icon;
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
    }
    
    // Method to draw this Trail
    public void draw(Graphics g) {
        // Draws PathBlock first
        super.draw(g);
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
