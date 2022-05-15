package view;

import controller.AnalysisController;
import view.GUI.PathChooserGUI;
import view.GUI.TreeGUI;
import view.utils.Commands;
import view.utils.Strings;

/**
 * Listener handle commands arriving from view. Events are forwarded to an {@link AnalysisController}
 *
 * @see Commands
 * @see AnalysisController
 */
public class ViewListener {

    private final AnalysisController analysisController;
    private View analyzerView;

    /**
     * Constructor of listener that forwards event to {@link AnalysisController}
     *
     * @param analysisController controller of the analysis
     */
    public ViewListener(final AnalysisController analysisController) {
        this.analysisController = analysisController;
    }

    /**
     * Perform event sent.
     *
     * @param code event to perform
     * @see Commands
     */
    public void eventPerformed(Commands code) {
        switch (code) {
            case SELECT_PROJECT -> this.initAnalysis();
            case START_ANALYSIS -> this.start();
            case STOP_ANALYSIS -> this.stop();
            case SAVE_REPORT_INSIDE_FILE -> this.saveProjectReport();
        }
    }

    /**
     * Set the view that manage analysis
     *
     * @param view view that manage analysis
     * @see TreeGUI
     */
    public void setViewToRunForAnalysis(final View view) {
        this.analysisController.setView(view);
        this.analyzerView = view;
    }

    private void initAnalysis() {
        var p = PathChooserGUI.getFolderPath();
        if (p.isBlank()) {
            analyzerView.showError(Strings.VALID_PATH, Strings.PATH_ERROR);
        } else {
            this.analysisController.setPathProjectToAnalyze(p);
            this.analyzerView.launch();
        }
    }

    private void start() {
        this.analysisController.startAnalysisProject();
    }

    private void stop() {
        this.analysisController.stopAnalysisProject();
    }

    private void saveProjectReport() {
        this.analysisController.saveProjectReportToFile();
    }

}
