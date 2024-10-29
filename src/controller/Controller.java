package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import model.CommandModel;
import view.Viewable;

public class Controller {

    private Map<String, CommandModel> commands = new HashMap<>();
    private Viewable view;
    private static final String FILE_PATH = "commands.ser"; // File path to save serialized commands

    public Controller(Viewable view) {
        this.view = view;
        view.setController(this);
        loadCommands();
    }

    public void run() {
        view.createAndShowGUI();
    }

    public Map<String, CommandModel> getAllCommands() {
        return commands;
    }

    public String getCommand(String title) {
        return commands.get(title).getCommand();
    }

    public void addCommand(String title, String command) {
        commands.put(title, new CommandModel(title, command));
        saveCommands();
    }

    // Save commands to a file using serialization
    public void saveCommands() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(commands); // Write the commands map to the file
        } catch (IOException e) {
            view.displayMessage("Error saving commands: " + e.getMessage());
        }
    }

    // Load commands from a file using deserialization
    @SuppressWarnings("unchecked")
    public void loadCommands() {
        File file = new File(FILE_PATH);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                commands = (Map<String, CommandModel>) ois.readObject(); // Read the commands map from the file
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                view.displayMessage("Error loading commands: " + e.getMessage());
            }
        }
    }

    public void removeCommand(String title) {
        if (commands.containsKey(title)) {
            commands.remove(title);
            // view.displayMessage("Command removed: " + title);
        } else {
            view.displayMessage("Command not found: " + title);
        }
    }

    public void executeCommand(String title) {
        try {
            CommandModel cmd = commands.get(title);
            if (cmd != null) {
                Process process = new ProcessBuilder("cmd.exe", "/c", cmd.getCommand()).start();
                cmd.setProcess(process);

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } else {
                view.displayMessage("Command not found: " + title);
            }
        } catch (Exception e) {
            view.displayMessage("Error executing command: " + e.getMessage());
        }
    }

    public void killCommand(String title) {
        CommandModel cmd = commands.get(title);
        if (cmd != null) {
            cmd.killProcess();
        } else {
            view.displayMessage("Command not found: " + title);
        }
    }
}
