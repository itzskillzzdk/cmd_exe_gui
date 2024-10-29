package view;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import controller.Controller;

public class CommandRowPanel extends JPanel {

    private Controller controller;
    private String title;

    private JLabel titleLabel;

    public CommandRowPanel(String title, Controller controller) {

        this.controller = controller;
        this.title = title;

        // Set a GridLayout with 1 row and 2 columns
        setLayout(new GridLayout(1, 2));

        // Create the label that will take up maximum space
        titleLabel = new JLabel(title);
        titleLabel.setFont(GUIStyle.FONT);

        // Create the panel to hold the buttons, aligned to the right
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Create the buttons
        JButton executeButton = new JButton("Execute");
        JButton killButton = new JButton("Kill");
        JButton editButton = new JButton("Edit");
        JButton delButton = new JButton("Delete");

        // Add action listeners for the buttons as needed
        executeButton.addActionListener(e -> executeCommand());
        killButton.addActionListener(e -> killCommand());
        editButton.addActionListener(e -> editCommand());
        delButton.addActionListener(e -> delCommand());

        JButton[] buttons = { executeButton, killButton, editButton, delButton };
        for (JButton btn : buttons) {
            GUIStyle.applyStyle(btn);
        }

        // Add buttons to the button panel
        buttonPanel.add(executeButton);
        buttonPanel.add(killButton);
        buttonPanel.add(editButton);
        buttonPanel.add(delButton);

        // Add the titleLabel and buttonPanel to the main panel
        add(titleLabel); // This will take up all available space on the left
        add(buttonPanel); // This will contain the buttons aligned to the right
    }

    private void showEditDialog() {
        // Create a new JDialog for editing
        JDialog editDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Command", true);
        editDialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5); // Adds padding between components

        // Fields to edit title and command
        JTextField titleField = new JTextField(title);
        JTextField commandField = new JTextField(controller.getCommand(title));

        titleField.setFont(GUIStyle.FONT);
        commandField.setFont(GUIStyle.FONT);

        JButton saveButton = new JButton("Save");
        JButton backButton = new JButton("Back");

        JButton[] buttons = { saveButton, backButton };
        for (JButton btn : buttons) {
            GUIStyle.applyStyle(btn);
        }

        // Add the title field to the first row
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across all columns
        gbc.weightx = 1.0; // Take full width
        editDialog.add(titleField, gbc);

        // Add the command field to the second row
        gbc.gridx = 0;
        gbc.gridy = 1;
        editDialog.add(commandField, gbc);

        // Add buttons to the third row
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5; // Change the space taken by the buttons
        editDialog.add(backButton, gbc);

        gbc.gridx = 1;
        editDialog.add(saveButton, gbc);

        // Add action listener for save button to update the command
        saveButton.addActionListener(e -> {
            String newTitle = titleField.getText();
            String newCommand = commandField.getText();

            // Update the command in the controller
            if (!newTitle.equals(title)) {
                controller.removeCommand(title); // Remove old entry if the title has changed
            }
            controller.addCommand(newTitle, newCommand);
            // Update the value of the attribute title
            title = newTitle;
            // Update the titleLabel
            titleLabel.setText(newTitle);

            editDialog.dispose();
        });

        // Action listener for back button (closes dialog)
        backButton.addActionListener(e -> editDialog.dispose());

        editDialog.setSize(400, 200);
        editDialog.setLocationRelativeTo(this);
        editDialog.setVisible(true);
    }

    private void executeCommand() {
        System.out.println("Executing command: " + title);
        controller.executeCommand(title);
    }

    private void killCommand() {
        System.out.println("Killing command: " + title);
        controller.killCommand(title);
    }

    private void editCommand() {
        // System.out.println("Editing command: " + title);
        showEditDialog();
    }

    private void delCommand() {
        controller.removeCommand(title);
        delCommandRowPanel();
    }

    private void delCommandRowPanel() {
        Container parentPanel = this.getParent();
        if (parentPanel != null) {
            parentPanel.remove(this);
            parentPanel.revalidate();
            parentPanel.repaint();
        }
    }
}
