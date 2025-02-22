import java.awt.Color;

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
    private static final String IMAGE_PATH = "tree.png";

    // Default colour if icon doesn't work
    private static final Color treeColour = Color.GRAY;

    // Constructor
    public Tree(int x, int y, int width, int height) {
        // Calls PathBlock constructor
        super(x, y, width, height);

        // Updates the icon of this Tree to the tree image and colour
        updateIcon(IMAGE_PATH, treeColour);
    }

    // Static method returning the imagePath
    public static String getImagePath() {
        return IMAGE_PATH;
    }
}
