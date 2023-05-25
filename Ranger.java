import java.awt.*;
import javax.swing.*;
import java.util.EnumMap;

/**
 * Ranger for Pathfinding maze solver
 * Ranger has four images for each direction
 * Keeps track of current location
 *
 * @author Asif Rahman
 * @version 24/05/2023
 */
public class Ranger
{
    // Array of size two storing the location
    private int[] currentIndex;
    
    // EnumMap storing the for each direction
    private EnumMap<PathFinder.Directions, ImageIcon> images;
    
    // Boolean to check if all images have loaded correctly
    private boolean imagesLoaded;
    
    // Color is used to draw ranger when images are not loaded correctly
    private Color defaultColour;

    // Constructor defaults to (0, 0)
    public Ranger() {
        this(0, 0);
    }
    
    // Constructor that takes a starting index
    public Ranger(int x, int y) {
        
        // Saves the starting index as the current index
        currentIndex = new int[]{x, y};
        
        // Initializes EnumMap
        images = new EnumMap<>(PathFinder.Directions.class);

        // For every single direction
        for (PathFinder.Directions direction : PathFinder.Directions.values()) {
            String imagePath = "Ranger/" + direction.toString().toLowerCase() + ".png";
            
            // Gets the URL of the image file
            java.net.URL imageURL = getClass().getResource(imagePath);
            
            // If the URL is valid, create an ImageIcon
            if (imageURL != null) {
                images.put(direction, new ImageIcon(imageURL));
                imagesLoaded = true;
            }
            
            // If the URL is invalid, the images did not load correctly
            // break since no images will be used unless they all load
            else {
                imagesLoaded = false;
                break;
            }
        }
    }

    // Draws the Ranger using the icon if possible
    // Otherwise it fills the given area with the default colour
    public void draw(JPanel panel, Graphics g, PathFinder.Directions direction, int x, int y, int width, int height) {
        // If the icon is loaded, draw the icon
        if (imagesLoaded) {
            images.get(direction).paintIcon(panel, g, x, y);
        }

        // Otherwise fill in the square with default colour
        else {
            g.setColor(defaultColour);
            g.fillRect(x, y, width, height);
        }
    }
    
    // Returns the image associated with the direction
    public ImageIcon getImage(PathFinder.Directions direction) {
        return images.get(direction);
    }
    
    // Returns the default colour of the Ranger
    public Color getDefaultColour() {
        return defaultColour;
    }

    // Returns the current index of the Ranger
    public int[] getCurrentIndex() {
        return currentIndex;
    }
}
