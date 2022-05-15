import controller.AnalysisController;
import view.GUI.StartGUI;
import view.GUI.TreeGUI;
import view.ViewListener;

public class App {
    public static void main(String[] args) {
        AnalysisController analysisController = new AnalysisController();
        ViewListener viewListener = new ViewListener(analysisController);
        viewListener.setViewToRunForAnalysis(new TreeGUI(viewListener));
        new StartGUI(viewListener);
    }
}
