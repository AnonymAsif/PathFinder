import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 * Block class for a maze solver or pathfinder
 * Each block can be in a variety of states
 *
 * @author Asif Rahman
 * @version 08/05/2023
 */
public abstract class PathBlock extends Rectangle
{
    // Enum of Directions stored in PathBlock
    public enum Directions {
        NORTH, EAST, SOUTH, WEST
    }
    
    // Colours for drawing Paths
    private static Color backgroundColour = Color.GREEN;
    private static Color borderColour = Color.BLACK;
    private static Color highlightColour = Color.BLUE;
    
    // Image Icon to draw state
    private static ImageIcon icon;

    // Constructor
    public PathBlock(int x, int y, int width, int height) {
        // Calls Rectangle constructor
        super(x, y, width, height);
    }
    
    // Abstract method that returns the current Direction of this block
    public abstract PathBlock.Directions getCurrentDirection();
    
    // Method to draw this PathBlock
    public void draw(Graphics g) {
        // Sets the colour to the background colour
        // And draws the background
        g.setColor(backgroundColour);
        g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());

        // Sets the colour to the border colour
        // And draws the border
        g.setColor(borderColour);
        g.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
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
