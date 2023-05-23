import javax.swing.JFrame;

/**
 * Driver class for a Pathfinding AI
 * JFrame window for PathFinder panel
 *
 * @author Asif Rahman
 * @version 10/05/2023
 */
public class Driver extends JFrame
{
    // Pathfinder object to display (JPanel)
    private final PathFinder pathfinder;

    public Driver() {
        // Sets up frame for the game
        setTitle("Maze Pathfinder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initializes Pathfinder panel
        pathfinder = new PathFinder();
        

        // Adds JPanels to frame and packs it
        getContentPane().add(pathfinder);
        pack();
        setVisible(true);
    }

    // Starts Driver
    public static void main(String[] args) {
        new Driver();
    }
}
