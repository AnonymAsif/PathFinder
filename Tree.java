import java.awt.Graphics;

/**
 * Tree is a PathBlock that cannot be walked through
 *
 * @author Asif Rahman
 * @version 09/05/2023
 */
public class Tree extends PathBlock
{
    // Constructor
    public Tree(int x, int y, int width, int height) {
        // Calls PathBlock constructor
        super(x, y, width, height);
    }
    
    // Method to draw this Tree
    public void draw(Graphics g) {
        // Draws PathBlock first
        super.draw(g);
    }
}
