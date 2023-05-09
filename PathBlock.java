import java.awt.Rectangle;
import java.awt.Color;

/**
 * Block class for a maze solver or pathfinder
 * Each block can be in a variety of states
 *
 * @author Asif Rahman
 * @version 08/05/2023
 */
public class PathBlock extends Rectangle
{
    // Colours for drawing Paths
    private static Color backgroundColour = Color.GREEN;
    private static Color borderColour = Color.BLACK;
    private static Color highlightColour = Color.BLUE;
    
    // Constructor
    public PathBlock(int x, int y, int width, int height) {
        // Calls Rectangle constructor
        super(x, y, width, height);
    }
    
    // Static getter method returning the colours
    public static Color getBackgroundColour() {
        return backgroundColour;
    }

    public static Color getBorderColour() {
        return borderColour;
    }

    public static Color getHighlightColour() {
        return highlightColour;
    }
    
    // Static setter methods for changing the colours
    public static void setBackgroundColour(Color bgColour) {
        backgroundColour = bgColour;
    }

    public static void setBorderColour(Color bdColour) {
        borderColour = bdColour;
    }

    public static void setHighlightColour(Color hlColour) {
        highlightColour = hlColour;
    }
}
