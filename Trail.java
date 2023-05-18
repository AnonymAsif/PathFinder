import java.awt.Graphics;
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
        UNDISCOVERED(null),
        DISCOVERED(null),
        EXPLORED(null),
        CABIN(null);
        
        private ImageIcon icon;
        
        TraversalState(ImageIcon icon) {
           this.icon = icon; 
        }
        
        public ImageIcon getIcon() {
            return icon;
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
