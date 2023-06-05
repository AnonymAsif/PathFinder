import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.util.EnumMap;

/**
 * Ranger for Pathfinding maze solver
 * Ranger has four images for each direction
 * as well two end images, for success and failure
 * This class is used mainly for drawing purposes
 *
 * @author Asif Rahman
 * @version 24/05/2023
 */
public class Ranger
{
    // Enum of ranger states for drawing
    // If the ranger is not traversing, it was a success or failure
    private enum RangerStates {TRAVERSING, SUCCESS, FAILURE}

    // Color is used to draw ranger when images are not loaded correctly
    private static final Color defaultColour = Color.ORANGE;

    // EnumMap storing the for each direction
    private final EnumMap<MazePanel.Directions, ImageIcon> images;

    // File paths for success and failure images
    private static final String SUCCESS_ICON_PATH = "Ranger/success.png";
    private static final String FAILURE_ICON_PATH = "Ranger/failure.png";

    // ImageIcon drawn when Ranger is successful
    private final ImageIcon successIcon;

    // ImageIcon drawn when Ranger fails
    private final ImageIcon failureIcon;

    // Boolean to check if all images have loaded correctly
    private boolean imagesLoaded;

    // Current state in the rangers journey
    private RangerStates currentState;

    public Ranger() {
        // Initializes EnumMap
        images = new EnumMap<>(MazePanel.Directions.class);

        // imagesLoaded stays true until any image fails to load
        imagesLoaded = true;

        // The ranger is always TRAVERSING by default
        currentState = RangerStates.TRAVERSING;

        // For every single direction, get the corresponding image file path
        for (PathFinder.Directions direction : PathFinder.Directions.values()) {
            String imagePath = "Ranger/" + direction.toString().toLowerCase() + ".png";

            // Attempts to get an ImageIcon from file
            ImageIcon image = getImage(imagePath);

            // The image did not load correctly
            // break since no images will be used unless they all load
            if (image == null) break;

            // Adds the image to the map
            images.put(direction, image);
        }

        // Attempts to load in success and failure icons
        successIcon = getImage(SUCCESS_ICON_PATH);
        failureIcon = getImage(FAILURE_ICON_PATH);
    }

    // Attempts to load an image from a file path
    private ImageIcon getImage(String imagePath) {
        // Gets the URL of the image file
        java.net.URL imageURL = getClass().getResource(imagePath);

        // If the URL is valid, create an ImageIcon
        if (imageURL != null) {
            return new ImageIcon(imageURL);
        }

        // Otherwise the image fails to load, return null
        imagesLoaded = false;
        return null;
    }

    // Draws the Ranger using the icon if possible
    // Otherwise it fills the given area with the default colour
    public void draw(JPanel panel, Graphics g, MazePanel.Directions direction, int x, int y, int width, int height) {
        // If the icon is loaded, draw the icon
        if (imagesLoaded) {
            // If the ranger is still traversing, get the image of the ranger facing a direction
            // Otherwise get the success or failure icon of the ranger depending on the state
            ImageIcon rangerImage = switch (currentState) {
                case TRAVERSING -> images.get(direction);
                case SUCCESS -> successIcon;
                case FAILURE -> failureIcon;
            };

            rangerImage.paintIcon(panel, g, x, y);
        }

        // Otherwise fill in the square with default colour
        else {
            g.setColor(defaultColour);
            g.fillRect(x, y, width, height);
        }
    }

    // Ends the journey of this ranger
    // and sets its success state
    public void setSuccess(boolean successful) {
        // If successful, the ranger is in a SUCCESS state
        if (successful)
            currentState = RangerStates.SUCCESS;

        // Otherwise it is set to a FAILURE state
        else currentState = RangerStates.FAILURE;
    }

    // Resets the success state of the Ranger by setting it to TRAVERSING
    public void resetSuccess() {
        currentState = RangerStates.TRAVERSING;
    }

    // static method returning the "Default" image
    // The default image of the ranger is the success image
    public static String getDefaultImagePath() {
        return SUCCESS_ICON_PATH;
    }
}
