import javax.swing.plaf.ButtonUI;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;

/** 
 * Button Panel class for Pathfinding maze solver
 * Contains buttons that call the provided methods
 * Uses the ButtonUI passed in constructor
 *
 * Centers button by using a grid layout and
 * creating new Jpanels for margins
 *
 * @author Asif Rahman
 * @version 26/05/2023
 */

public class ButtonPanel extends JPanel {

    // JButtons in panel
    private final JButton[] buttons;

    // The height of the center panel with buttons
    private final int CENTER_HEIGHT = 35;

    // Size of the north and south panels for spacing
    private final int MARGIN = 10;

    public ButtonPanel(String[] buttonNames, ButtonAction[] actions, ButtonStates[][] states, int width, Color backgroundColour, ButtonUI ui) {
        // Sets panel size and background colour
        // Total height is equal to the sum of both margins and center height
        setPreferredSize(new Dimension(width, CENTER_HEIGHT + 2 * MARGIN));
        setBackground(backgroundColour);
        setLayout(new BorderLayout());
        
        // If the number of button names, actions and states do not match
        // A valid number of buttons cannot be created
        if (buttonNames.length != actions.length || states.length != actions.length) {
            throw new IllegalArgumentException("Length of button names, actions and states do not match.");
        }
        
        // Initializes buttons array
        buttons = new JButton[buttonNames.length];
        
        // Create every button in the buttons array,
        // Then set its UI and ActionListener
        for (int i = 0; i < buttons.length; i++) {
            // Creates JButton with the name of the button
            buttons[i] = new JButton(buttonNames[i]);
            
            // Sets the ButtonUI of the JButton
            buttons[i].setUI(ui);
            
            // Adds the button action to the button
            // buttonAction and i must be final to be used in a lambda expression
            int finalI = i;
            ButtonAction buttonAction = actions[i];
            buttons[i].addActionListener(e -> {
                // Does the button action
                buttonAction.doAction();

                // states[finalI] gives the button configuration applied by this button
                // For every button in the button configuration, set it enabled if the given state is ENABLED
                for (int j = 0; j < states[finalI].length; j++) {
                    buttons[j].setEnabled(states[finalI][j] == ButtonStates.ENABLED);
                }
            });
        }

        // // Buttons for Start, Stop and Skip
        // startButton = new JButton("Start");
        // stopButton = new JButton("Stop");
        // skipButton = new JButton("Skip");

        // // Sets ui to ButtonUI passed in
        // startButton.setUI(ui);
        // stopButton.setUI(ui);
        // skipButton.setUI(ui);

        // // Disables stop by default since the timer hasn't started
        // stopButton.setEnabled(false);

        // /* Add action listeners to buttons
         // * Start and stop start/stop the grid timer
         // * And disable themselves
         // * until the timer is start/stopped again */
        // startButton.addActionListener(e -> {
            // grid.start();

            // // Enable other buttons
            // startButton.setEnabled(false);
            // stopButton.setEnabled(true);
            // skipButton.setEnabled(false);
        // });

        // stopButton.addActionListener(e -> {
            // grid.stop();

            // // Enable other buttons
            // startButton.setEnabled(true);
            // stopButton.setEnabled(false);
            // skipButton.setEnabled(true);
        // });

        // // Skip button skips one frame - doesn't disable anything
        // skipButton.addActionListener(e -> grid.skip());

        // Creates new JPanel to add the buttons to
        JPanel buttonJPanel = new JPanel();
        buttonJPanel.setBackground(backgroundColour);

        // Adds every buttons to buttonJPanel
        for (JButton button : buttons) {
            buttonJPanel.add(button);
        }

        // Adds this panel to center
        add(buttonJPanel, BorderLayout.CENTER);

        // Adds layouts to north and south to center the buttons
        // They both have the same background colour
        JPanel northPanel = new JPanel();
        northPanel.setPreferredSize(new Dimension(0, MARGIN));
        northPanel.setBackground(backgroundColour);

        JPanel southPanel = new JPanel();
        southPanel.setPreferredSize(new Dimension(0, MARGIN));
        southPanel.setBackground(backgroundColour);

        // Adds north and south panels for spacing
        add(northPanel, BorderLayout.NORTH);
        add(southPanel, BorderLayout.SOUTH);
    }
}