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

    // Saves the number of buttons in panel
    private int buttonCount;

    public ButtonPanel(String[] buttonNames, ButtonAction[] actions, ButtonStates[][] states, int width, Color backgroundColour, ButtonUI ui) {
        // Sets panel size and background colour
        // Total height is equal to the sum of both margins and center height
        setPreferredSize(new Dimension(width, CENTER_HEIGHT + 2 * MARGIN));
        setBackground(backgroundColour);
        setLayout(new BorderLayout());

        // Sets buttonCount to the number of button names
        buttonCount = buttonNames.length;
        
        // If the number of button names, actions and states do not match
        // A valid number of buttons cannot be created
        if (actions.length != buttonCount || states.length != buttonCount) {
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

        // Creates new JPanel to add the buttons to
        JPanel buttonJPanel = new JPanel();
        buttonJPanel.setBackground(backgroundColour);

        // Adds every button to buttonJPanel
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

    // Sets a new button configuration, without affected button presses
    // This method only temporarily changes the configuration until a button is pressed
    public void setButtonConfig(ButtonStates[] newStates) {
        // Invalid number of new states
        if (newStates.length != buttonCount) {
            throw new IllegalArgumentException("Length of new states does not match button count.");
        }

        // For each button in the panel
        // Set it enabled only if the corresponding new state is ENABLED
        for (int i = 0; i < newStates.length; i++) {
            buttons[i].setEnabled(newStates[i] == ButtonStates.ENABLED);
        }
    }
}