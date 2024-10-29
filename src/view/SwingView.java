package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.CommandModel;

public class SwingView implements Viewable {

    public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

    private Controller controller;
    private JFrame frame;
    private JLabel title;
    private JPanel commandsPanel;
    private JPanel utilityPanel;

    private JButton newCmdBtn;

    public void createAndShowGUI() {
        frame = new JFrame("CmdExeGUI");
        frame.setSize(500, 800);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.saveCommands();
            }
        });

        Container contentPane = frame.getContentPane();

        // Logic with contentPane..
        addTitle(contentPane, BorderLayout.NORTH);
        addCommandsPanel(contentPane, BorderLayout.CENTER);
        addUtilityPanel(contentPane, BorderLayout.SOUTH);

        frame.setVisible(true);

        // Load and display saved commands after the GUI is created
        displayCommandList();
    }

    private void addTitle(Container contentPane, Object constraints) {
        title = new JLabel("Commands :");
        title.setFont(GUIStyle.getFont(32));
        title.setBorder(new EmptyBorder(0, 20, 0, 20));
        addComponent(title, contentPane, constraints);
    }

    private void addCommandsPanel(Container contentPane, Object constraints) {
        commandsPanel = new JPanel();
        commandsPanel.setLayout(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(commandsPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        addComponent(scrollPane, contentPane, constraints);
    }

    private void addUtilityPanel(Container contentPane, Object constraints) {
        utilityPanel = new JPanel();
        utilityPanel.setBorder(new EmptyBorder(0, 20, 0, 20));
        addComponent(utilityPanel, contentPane, constraints);

        addNewCommandButton(utilityPanel, null);
    }

    private void addNewCommandButton(Container utilityPanel, Object constraints) {
        newCmdBtn = new JButton("+");
        newCmdBtn.setBackground(Color.WHITE);
        newCmdBtn.setFont(GUIStyle.getFont(32));
        newCmdBtn.setFocusPainted(false);
        newCmdBtn.setMargin(new Insets(0, 100, 0, 100));
        newCmdBtn.setToolTipText("Create a new command row.");

        newCmdBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleNewCommandButtonClick();
            }
        });

        addComponent(newCmdBtn, utilityPanel, constraints);
    }

    private void handleNewCommandButtonClick() {
        // Create a new instance of CommandRowBuilderPanel
        CommandRowBuilderPanel builderPanel = new CommandRowBuilderPanel(this, controller);

        // Display the CommandRowBuilderPanel in a dialog
        JOptionPane.showMessageDialog(frame, builderPanel, "Add New Command", JOptionPane.PLAIN_MESSAGE);
    }

    public void addCommandRowPanel(String title, String command) {
        CommandRowPanel commandRowPanel = new CommandRowPanel(title, controller);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        commandsPanel.add(commandRowPanel, gbc);
        commandsPanel.revalidate();
        commandsPanel.repaint();
    }

    public void removeCommandRowPanel(CommandRowPanel cmdRowPanel) {
        commandsPanel.remove(cmdRowPanel);
        commandsPanel.revalidate();
        commandsPanel.repaint();
    }

    @SuppressWarnings("unused")
    private void centeredComponent(JComponent component, Container contentPane) {
        component.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    private void addComponent(JComponent component, Container contentPane, Object constraints) {

        if (constraints == null)
            contentPane.add(component);
        else
            contentPane.add(component, constraints);
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void displayCommandList() {
        // Retrieve the list of commands from the controller
        Map<String, CommandModel> commands = controller.getAllCommands();

        // Loop through each command and create a CommandRowPanel
        for (CommandModel command : commands.values()) {
            // Create and add the command row panel
            CommandRowPanel commandRowPanel = new CommandRowPanel(command.getTitle(), controller);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.weightx = 1.0;
            gbc.weighty = 0;
            gbc.gridx = 0;
            gbc.gridy = GridBagConstraints.RELATIVE;
            commandsPanel.add(commandRowPanel, gbc);
        }

        // Refresh the commandsPanel to show the updated list
        commandsPanel.revalidate();
        commandsPanel.repaint();
    }

    @Override
    public void displayMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            // Affiche une boîte de dialogue avec le message
            JOptionPane.showMessageDialog(frame, message, "Information", JOptionPane.INFORMATION_MESSAGE);
        });
    }

}
