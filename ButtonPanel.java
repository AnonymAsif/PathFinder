import javax.swing.plaf.ButtonUI;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;

/** 
 * Button Panel class for GUI windows
 * Contains buttons that call the provided methods
 * Uses the ButtonUI passed in constructor
 *
 * Centers button by using a grid layout and
 * creating new JPanels for margins
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
    private final int buttonCount;

    public ButtonPanel(String[] buttonNames, ButtonAction[] actions, int width, Color backgroundColour, ButtonUI ui) {
        // Sets panel size and background colour
        // Total height is equal to the sum of both margins and center height
        setPreferredSize(new Dimension(width, CENTER_HEIGHT + 2 * MARGIN));
        setBackground(backgroundColour);
        setLayout(new BorderLayout());

        // Sets buttonCount to the number of button names
        buttonCount = buttonNames.length;
        
        // If the number of button names and actions do not match
        // A valid number of buttons cannot be created
        if (actions.length != buttonCount) {
            throw new IllegalArgumentException("Length of button names and actions do not match.");
        }
        
        // Initializes buttons array
        buttons = new JButton[buttonCount];
        
        // Create every button in the buttons array,
        // Then set its UI and ActionListener
        for (int i = 0; i < buttons.length; i++) {
            // Creates JButton with the name of the button
            buttons[i] = new JButton(buttonNames[i]);

            // Sets the ButtonUI of the JButton
            buttons[i].setUI(ui);

            // Adds the button action to the button
            // buttonAction must be final to be used in a lambda expression
            ButtonAction buttonAction = actions[i];
            buttons[i].addActionListener(e -> buttonAction.doAction());
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
        for (int i = 0; i < newStates.length; i++) {
            // Don't do anything if the state is UNCHANGED
            if (newStates[i] == ButtonStates.UNCHANGED) continue;

            // Set it enabled only if the corresponding new state is ENABLED
            buttons[i].setEnabled(newStates[i] == ButtonStates.ENABLED);
        }
    }

    // Overload of setButtonConfig that sets all button states to one state
    public void setButtonConfig(ButtonStates newState) {
        // Do nothing if the new state is UNCHANGED
        if (newState == ButtonStates.UNCHANGED) return;

        // For each button in the panel
        // Set it enabled only if the new state is ENABLED
        for (JButton button : buttons) {
            button.setEnabled(newState == ButtonStates.ENABLED);
        }
    }
}