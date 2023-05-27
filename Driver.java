import javax.swing.JFrame;
import java.awt.*;

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

    // Button panel with all relevant buttons (start, stop, skip)
    private final ButtonPanel buttonPanel;

    public Driver() {
        // Sets up frame for the game
        setTitle("Maze Pathfinder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Initializes Pathfinder panel
        pathfinder = new PathFinder();

        // Creates a new buttonUI for the ButtonPanel
        WorldsBestButtonUI ui = new WorldsBestButtonUI(Color.LIGHT_GRAY, Color.BLACK);

        // There will be three buttons, start, stop and skip
        // Each button has a name and an action corresponding to a public method in pathfinder
        String[] buttonNames = new String[] {"Start", "Stop", "Skip"};
        ButtonAction[] buttonActions = new ButtonAction[] {pathfinder::start, pathfinder::stop, pathfinder::skip};

        // Each button also has a state configuration that defines
        // which buttons will be enabled/disabled after it is pressed
        // The order is relative to the order of the buttons in buttonNames and buttonActions
        ButtonStates[][] buttonStates = new ButtonStates[][] {
                {ButtonStates.DISABLED, ButtonStates.ENABLED, ButtonStates.DISABLED}, // Start configuration
                {ButtonStates.ENABLED, ButtonStates.DISABLED, ButtonStates.ENABLED}, // Stop configuration
                {ButtonStates.ENABLED, ButtonStates.DISABLED, ButtonStates.ENABLED}  // Skip configuration
        };

        // Creates ButtonPanel by passing in the names, actions and states of the buttons
        // Also passes in the width of the panel, background colour and the ButtonUI
        buttonPanel = new ButtonPanel(buttonNames, buttonActions, buttonStates, pathfinder.getWidth(), Color.ORANGE, ui);

        // Sets a default button configuration
        // Timer is stopped by default
        buttonPanel.setButtonConfig(buttonStates[1]);

        // Adds JPanels to frame and packs it
        getContentPane().add(pathfinder);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    // Starts Driver
    public static void main(String[] args) {
        new Driver();
    }
}
