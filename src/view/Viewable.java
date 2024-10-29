package view;

import controller.Controller;

public interface Viewable {

    void setController(Controller controller);

    void createAndShowGUI();

    void displayCommandList();

    void displayMessage(String message);
}
