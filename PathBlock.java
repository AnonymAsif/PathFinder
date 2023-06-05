import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

/**
 * Block class for a maze solver or pathfinder
 * Each block has ana image icon and default colour to represent it
 * Abstract class that mainly provides drawing tools
 *
 * @author Asif Rahman
 * @version 08/05/2023
 */
public abstract class PathBlock extends Rectangle
{
    // Colours for drawing Paths
    private static final Color backgroundColour = new Color(0x6D7062);
    private static final Color borderColour = Color.BLACK;
    
    // Image Icon to draw state
    protected ImageIcon icon;

    // Saves if the icon was properly loaded with a valid file path
    protected boolean validImage;

    // Colour that is to be used when icon is invalid
    protected Color defaultColour;

    // Constructor
    public PathBlock(int x, int y, int width, int height) {
        // Calls Rectangle constructor
        super(x, y, width, height);

        // Default colour is black until updated with a new icon
        defaultColour = Color.BLACK;
    }
    
    // Method to draw this PathBlock
    public void draw(JPanel panel, Graphics g) {

        // If the image is valid, then draw it on the square
        if (validImage) {
            // Sets the colour to the background colour
            // And draws the background
            g.setColor(backgroundColour);
            g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());

            // Draws icon
            icon.paintIcon(panel, g, (int)getX(), (int)getY());
        }
        // If it isn't then fill the background with the default colour
        else {
            g.setColor(defaultColour);
            g.fillRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        }
    }

    // Draws the border around this PathBlock
    public void drawBorder (Graphics g) {
        // Sets the colour to the border colour
        // And draws the border
        g.setColor(borderColour);
        g.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    }

    // Attempts to update the ImageIcon, if it does not work,
    // then validImage is set to false
    protected void updateIcon(String filepath, Color newDefaultColour) {
        // Updates the default colour to the colour matching this icon
        defaultColour = newDefaultColour;

        // Gets the URL of the file path to be validated
        java.net.URL imageURL = getClass().getResource(filepath);

        // If the URL is valid, create an ImageIcon
        if (imageURL != null) {
            validImage = true;
            icon = new ImageIcon(imageURL);
        }
        // If the URL is invalid
        else validImage = false;
    }
}
