import javax.swing.JFrame;
import java.awt.*;

/**
 * Driver class for a Pathfinding AI
 * JFrame window for PathFinder panel
 *
 * @author Asif Rahman
 * @version 10/05/2023
 */
public class Driver extends JFrame implements PathFinderListener
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

        // Adds this as a PathFinderListener to pathfinder
        pathfinder.addPathFinderListener(this);

        // Creates a new buttonUI for the ButtonPanel
        WorldsBestButtonUI ui = new WorldsBestButtonUI(Color.LIGHT_GRAY, Color.BLACK);

        // There will be three buttons, start, stop, skip and reset
        // Each button has a name and an action corresponding to a public method in pathfinder
        String[] buttonNames = new String[] {"Start", "Stop", "Skip", "Reset"};
        ButtonAction[] buttonActions = new ButtonAction[] {
                pathfinder::start, pathfinder::stop, pathfinder::skip, pathfinder::resetPathFinder
        };

        // Creates ButtonPanel by passing in the names, actions and states of the buttons
        // Also passes in the width of the panel, background colour and the ButtonUI
        buttonPanel = new ButtonPanel(buttonNames, buttonActions, pathfinder.getWidth(), Color.ORANGE, ui);

        // Sets the default button configuration since timer is stopped by default
        // The event is null as it was called from this class
        timerStopped(null);

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

    // PathFinder ended, disable all buttons and let user know
    @Override
    public void pathFound(PathFinderEvent e) {
        // Sets all buttons to DISABLED state except reset
        // Enables reset now that the animation has finished
        buttonPanel.setButtonConfig(new ButtonStates[] {
                ButtonStates.DISABLED, ButtonStates.DISABLED, ButtonStates.DISABLED, ButtonStates.ENABLED
        });
    }

    // PathFinder ended, disable all buttons and let user know
    @Override
    public void noPathFound(PathFinderEvent e) {
        // Sets all buttons to DISABLED state except reset
        // Enables reset now that the animation has finished
        buttonPanel.setButtonConfig(new ButtonStates[] {
                ButtonStates.DISABLED, ButtonStates.DISABLED, ButtonStates.DISABLED, ButtonStates.ENABLED
        });
    }

    // Animation started, disable start and skip buttons
    // The 4th button, reset, is disabled until the animation ends
    @Override
    public void timerStarted(PathFinderEvent e) {
        buttonPanel.setButtonConfig(new ButtonStates[] {
                ButtonStates.DISABLED, ButtonStates.ENABLED, ButtonStates.DISABLED, ButtonStates.DISABLED
        });
    }

    // Animation stopped, disable stop button
    // The 4th button, reset, is disabled until the animation ends
    @Override
    public void timerStopped(PathFinderEvent e) {
        buttonPanel.setButtonConfig(new ButtonStates[] {
                ButtonStates.ENABLED, ButtonStates.DISABLED, ButtonStates.ENABLED, ButtonStates.DISABLED
        });
    }

    // Frame skipped, leave button configuration unchanged
    @Override
    public void frameSkipped(PathFinderEvent e) {}


    // The PathFinder reset itself, reset the button configuration to default
    // The timer is stopped by default, so call timerStopped instead
    @Override
    public void reset(PathFinderEvent e) {
        timerStopped(e);
    }

}
