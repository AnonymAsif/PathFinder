
/**
 * Cabin class for Maze pathfinder
 * Cabin is the end goal of the maze
 * Extends trail since the Cabin is a walkable block
 *
 * @author Asif Rahman
 * @version 15/05/2023
 */
public class Cabin extends Trail
{
    // Constructor
    public Cabin(int x, int y, int width, int height) {
        // Calls Trail constructor
        super(x, y, width, height);
    }
}
