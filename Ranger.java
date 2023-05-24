import java.awt.Color;
import javax.swing.ImageIcon;
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
    
    // EnumMap storing the for each directions
    private EnumMap<PathFinder.Directions, ImageIcon> images;
    
    // Boolean to check if all images have loaded correctly
    private boolean imagesLoaded;
    
    // Color is used to draw ranger when images are not loaded correctly
    private Color defaultColour;
    
    // Constructor that takes a starting index
    public Ranger(int x, int y) {
        
        // Saves the starting index as the current index
        currentIndex = new int[]{x, y};
        
        // Initializes EnumMap
        images = new EnumMap<>(PathFinder.Directions.class);
        
        for (PathFinder.Directions direction : images.keySet()) {
            String imagepath = "Ranger/" + direction.toString().toLowerCase();
            
            // Gets the URL of the image file
            java.net.URL imageURL = getClass().getResource(imagepath);
            
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
    
    // Returns the image associated with the direction
    public ImageIcon getImage(PathFinder.Directions direction) {
        return images.get(direction);
    }
    
    
}
