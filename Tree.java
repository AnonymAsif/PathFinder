import javax.swing.*;
import java.awt.*;

/**
 * Tree is a PathBlock that cannot be walked through
 * It is drawn using a tree ImageIcon and has a gray default colour
 *
 * @author Asif Rahman
 * @version 09/05/2023
 */
public class Tree extends PathBlock
{
    // File path to find the tree image
    public static final String imagePath = "tree.png";

    // Default colour if icon doesn't work
    private static final Color treeColour = Color.GRAY;

    // Constructor
    public Tree(int x, int y, int width, int height) {
        // Calls PathBlock constructor
        super(x, y, width, height);

        // Updates the icon of this Tree to the tree image and colour
        updateIcon(imagePath, treeColour);
    }

    // Returns ImageIcon
    public ImageIcon getIcon() {
        return icon;
    }

    // Returns default Color used when icon is not valid
    public Color getDefaultColour() {
        return treeColour;
    }
}
