package view;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controller.Controller;

public class CommandRowBuilderPanel extends JPanel {

    private final static String ENTER_TITLE = "Enter command title...";
    private final static String ENTER_COMMAND = "Enter command...";

    private JTextField titleField;
    private JTextField commandField;
    private JButton submitButton;

    // Reference to SwingView to notify when a command is added
    private SwingView swingView;
    private Controller controller;

    // Constructor takes in SwingView and Controller references
    public CommandRowBuilderPanel(SwingView swingView, Controller controller) {
        this.swingView = swingView; // Save reference to SwingView
        this.controller = controller; // Save reference to Controller

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Input fields for title and command
        titleField = new JTextField(ENTER_TITLE);
        commandField = new JTextField(ENTER_COMMAND);
        titleField.setFont(GUIStyle.FONT);
        commandField.setFont(GUIStyle.FONT);

        // Submit button for adding the command
        submitButton = new JButton("Add Command");
        GUIStyle.applyStyle(submitButton);
        submitButton.addActionListener(e -> handleSubmit());

        // Add components to the panel
        add(titleField);
        add(commandField);
        add(submitButton);

    }

    // Method called when the submit button is clicked
    private void handleSubmit() {
        String title = getTitle();
        String command = getCommand();

        // Check if the title is already present
        if (controller.getAllCommands().containsKey(title)) {
            swingView.displayMessage("Command **" + title + "** already exist.");
        } else {
            // Add new command
            controller.addCommand(title, command); // Add the command to the controller
            swingView.addCommandRowPanel(title, command); // Add a new command row panel to the view
        }

        // Clear the text fields after submitting
        clearFields();
    }

    // Method to get the title from the input field
    public String getTitle() {
        return titleField.getText();
    }

    // Method to get the command from the input field
    public String getCommand() {
        return commandField.getText();
    }

    // Clears the text fields after submitting the command
    private void clearFields() {
        titleField.setText(ENTER_TITLE);
        commandField.setText(ENTER_COMMAND);
    }
}
