import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Color;

/**
 * ButtonUI that extends the BasicButtonUI
 * Draws a rectangle on hover
 * Creates an inward shadow effect on press without moving the button
 *
 * @author Asif Rahman
 * @version 17/04/2023
 */

public class WorldsBestButtonUI extends BasicButtonUI
{
    // Margins for the buttons border
    private final int VERTICAL_MARGIN = 5;
    private final int HORIZONTAL_MARGIN = 15;

    // Colours for the theme
    private final Color borderColour;
    private final Color backgroundColour;

    public WorldsBestButtonUI(Color backgroundColour, Color borderColour) {
        // Sets colours
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    protected void installDefaults(AbstractButton button) {
        // Sets an empty border on the button with margin dimension
        // To give it some space
        button.setBorder(BorderFactory.createEmptyBorder(VERTICAL_MARGIN, HORIZONTAL_MARGIN, VERTICAL_MARGIN, HORIZONTAL_MARGIN));

        // Sets default background colour
        button.setBackground(backgroundColour);
    }

    protected void paintButtonPressed(Graphics g, AbstractButton button) {
        // Array that holds HSB values of the background colour
        float[] hsb = new float[3];
        Color.RGBtoHSB(backgroundColour.getRed(), backgroundColour.getGreen(), backgroundColour.getBlue(), hsb);

        // Temporary brightness that increases to give a shadow effect
        // The shadow increments by increment each iteration
        float tempBrightness = 0;
        float increment = 0.1F;

        // Loop that draws rectangles slowly going inwards with for a shadow effect
        // Loop stops once the rectangle gets to the center of either dimension of the button
        for (int i = 0; i < Math.min(button.getWidth(), button.getHeight()) / 2 - 1; i++) {

            // Sets the colour to the original hsb colour
            // but with a brightness of tempBrightness
            g.setColor(Color.getHSBColor(hsb[0], hsb[1], tempBrightness));

            // Draws the rectangle inward by subtracting i
            // At coordinates of i to move inward in the other direction
            g.drawRect(i, i, button.getWidth()-i-1, button.getHeight()-i-1);

            // Increments tempBrightness
            tempBrightness += increment;

            // If the colour brighter than the actual background colour
            // The shadow should stop
            if (tempBrightness >= hsb[2]) break;
        }
    }

    public void paint(Graphics g, JComponent component) {
        super.paint(g, component);

        // Should never happen
        if (!(component instanceof AbstractButton button)) return;

        // If the button is being hovered over (rollover)
        // And is not currently being pressed, draw a border
        if (button.getModel().isRollover() && !button.getModel().isPressed()) {
            // Draws a rectangle at the border to show the button is hovered
            g.setColor(borderColour);
            g.drawRect(0, 0, button.getWidth()-1, button.getHeight()-1);
        }
    }
}