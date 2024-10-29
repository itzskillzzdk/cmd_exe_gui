package model;

import java.io.Serializable;

public class CommandModel implements Serializable {

    private static final long serialVersionUID = 1L; // Unique ID for serialization

    private String title;
    private String command;
    transient private Process process;

    public CommandModel(String title, String command) {
        this.title = title;
        this.command = command;
    }

    public String getTitle() {
        return title;
    }

    public String getCommand() {
        return command;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Process getProcess() {
        return process;
    }

    public void killProcess() {
        if (process != null) {
            process.destroy();
        }
    }
}
