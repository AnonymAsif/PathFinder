import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;

/**
 * Driver class for a Pathfinding AI
 * JFrame window for PathFinder panel
 * Many methods take an ActionEvent, so they can be called by buttons
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

    // Menubar that contains maze and colour editor commands
    private final JMenuBar menubar;

    // Editor that can create and edit mazes
    private final MazeEditor editor;


    // Dimensions of the maze
    private static final int MAZE_HEIGHT = 12;
    private static final int MAZE_WIDTH = 12;

    // Dimensions of the main PathFinder panel
    private static final int PANEL_HEIGHT = 600;
    private static final int PANEL_WIDTH = 600;

    public Driver() {
        // Sets up frame for the game
        setTitle("Maze Pathfinder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Creates a file handler for the maze
        MazeFileHandler fileHandler = new MazeFileHandler(MAZE_HEIGHT, MAZE_WIDTH, PANEL_HEIGHT, PANEL_WIDTH);

        // Initializes Pathfinder panel using the maze and ranger created in file handler
        pathfinder = new PathFinder(MAZE_HEIGHT, MAZE_WIDTH, PANEL_HEIGHT, PANEL_WIDTH, fileHandler.getMaze());
        pathfinder.setStartIndex(fileHandler.getRangerIndex());

        // Creates a new maze editor
        editor = new MazeEditor(pathfinder, fileHandler);

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

        // Initializes menubar and sets it up
        menubar = new JMenuBar();
        setupMenuBar();

        // Adds JPanels and menubar to frame and packs it
        getContentPane().add(pathfinder);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(menubar, BorderLayout.NORTH);
        pack();
        setVisible(true);
    }

    // Starts Driver
    public static void main(String[] args) {
        new Driver();
    }

    // Sets up the menubar by adding JMenus to it
    // and by adding JMenuItems to each JMenu
    private void setupMenuBar() {
        // Creates an Edit menu
        JMenu edit = new JMenu("Edit");

        // Creates two menu items for maze and speed
        JMenuItem editMaze = new JMenuItem("Maze");
        JMenuItem editSpeed = new JMenuItem("Speed");

        // Adds the actions to the buttons
        editMaze.addActionListener(e -> editor.setVisible(true));
        editSpeed.addActionListener(this::setPathFinderSpeed);

        // Adds the menu items to the Edit menu
        edit.add(editMaze);
        edit.add(editSpeed);

        // Creates a File menu
        JMenu file = new JMenu("File");

        // Creates two menu items for saving and loading
        JMenuItem saveFile = new JMenuItem("Save");
        JMenuItem loadFile = new JMenuItem("Load");

        // Adds the actions to the buttons
        saveFile.addActionListener(this::writeNewMaze);
        loadFile.addActionListener(this::readNewMaze);

        // Adds the menu items to the File menu
        file.add(saveFile);
        file.add(loadFile);

        // Adds Edit and File menus to menubar
        menubar.add(edit);
        menubar.add(file);
    }

    // Attempts to read a new maze from maze.txt and send it to the editor
    private void readNewMaze(ActionEvent event) {
        // Message in confirm dialog
        String confirmMessage = "Are you sure you want to read a new maze from a file?\n" +
                "Your current maze will be overwritten and lost if unsaved.";

        // Asks user if they're sure
        int choice = JOptionPane.showConfirmDialog(this,
                confirmMessage, "Confirm Maze Loading", JOptionPane.YES_NO_CANCEL_OPTION);

        // If the user did not agree, stop and let them know
        if (choice != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Maze load operation cancelled.");
            return;
        }

        // Tries to read a maze since the user agreed
        // Editor will handle the error message if it doesn't work
        editor.readMazeFromFile();
    }

    // Attempts to write the current maze to maze.txt and save it
    private void writeNewMaze(ActionEvent event) {
        // Message in confirm dialog
        String confirmMessage = "Are you sure you want to save the current maze to a file?\n" +
                "The maze in the file will be overwritten and lost if unsaved.";

        // Asks user if they're sure
        int choice = JOptionPane.showConfirmDialog(this,
                confirmMessage, "Confirm Maze Saving", JOptionPane.YES_NO_CANCEL_OPTION);

        // If the user did not agree, stop and let them know
        if (choice != JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Maze save operation cancelled.");
            return;
        }

        // Tries to write the maze
        // Editor will handle error messages if necessary
        editor.saveMazeToFile();
    }

    // Takes an ActionEvent as this method should be called by a button
    // Gets input from user and attempts to parse it to an int
    // Does not throw any exceptions, only lets the user know if the operation failed
    private void setPathFinderSpeed(ActionEvent event) {
        // Gets a new timer delay from the user
        String inputString = JOptionPane.showInputDialog(this, "Enter the new timer delay: ");

        // If the user cancelled the operation
        if (inputString == null) {
            JOptionPane.showMessageDialog(this, "Delay editing cancelled.");
            return;
        }

        // Attempts to parse it to an integer and set a new timer delay
        // Lets the user know if successful
        try {
            int newTimerDelay = Integer.parseInt(inputString);

            // Makes sure that the new delay is a positive number
            if (newTimerDelay > 0) {
                pathfinder.setUpdateTime(newTimerDelay);
                JOptionPane.showMessageDialog(this, "Successfully updated timer delay.");
            }

            // Throws a NumberFormatException so the invalid timer message displays
            else throw new NumberFormatException();
        }

        // If the input was invalid, let the user know the operation failed
        catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(this, "Invalid timer delay. Please enter a positive integer.");
        }
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

    // The PathFinder reset itself, reset the button configuration to default
    // The timer is stopped by default, so call timerStopped
    // Gets maze from file handler and resets the one in pathfinder
    @Override
    public void reset(PathFinderEvent e) {
        // Gets the file handler from the editor
        MazeFileHandler fileHandler = editor.getFileHandler();

        // Sets the maze and ranger index in pathfinder to the original one in fileHandler
        pathfinder.setMaze(fileHandler.getMaze());
        pathfinder.setStartIndex(fileHandler.getRangerIndex());

        // Sets button configuration
        timerStopped(e);
    }

    // Frame skipped, leave button configuration unchanged
    @Override
    public void frameSkipped(PathFinderEvent e) {}
}
