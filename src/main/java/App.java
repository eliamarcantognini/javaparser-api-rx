import controller.AnalysisController;
import view.GUI.StartGUI;
import view.GUI.TreeGUI;
import view.ViewListener;

/**
 * Main class of the project.
 */
public class App {
    /**
     * Entry point of the project.
     */
    public static void main(String[] args) {
        AnalysisController analysisController = new AnalysisController();
        ViewListener viewListener = new ViewListener(analysisController);
        viewListener.setViewToRunForAnalysis(new TreeGUI(viewListener));
        new StartGUI(viewListener);
    }
}
