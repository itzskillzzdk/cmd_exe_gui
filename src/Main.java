import controller.Controller;
import view.SwingView;

public class Main {
    public static void main(String[] args) {

        SwingView view = new SwingView();
        Controller controller = new Controller(view);
        controller.run();
    }
}
