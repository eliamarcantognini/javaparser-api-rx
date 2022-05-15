package controller;

import io.vertx.core.Vertx;
import lib.Logger;
import lib.ProjectAnalyzer;
import lib.async.AsyncProjectAnalyzer;
import lib.dto.DTOParser;
import lib.dto.PackageDTO;
import lib.dto.ProjectDTO;
import view.View;
import view.utils.Strings;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Controller to manage application about project analysis
 *
 * @see AsyncProjectAnalyzer
 * @see lib.dto
 */
public class AnalysisController {

    /**
     * File where analysis will be saved
     */
    private final static String OUTPUT_PATH = "./output.json";
    /**
     * Channel of vertx eventBus where analysis messages are exchanged
     */
    private final static String VERTX_CHANNEL_TOPIC = "new_find";

    private final ProjectAnalyzer projectAnalyzer;
    private final Vertx vertx;
    private ProjectDTO projectDTO;
    private View view;
    private String pathProjectToAnalyze;

    /**
     * Constructor of class
     */
    public AnalysisController() {
        this.vertx = Vertx.vertx();
        this.projectAnalyzer = new AsyncProjectAnalyzer(this.vertx);
    }

    /**
     * Set the view where display analysis results
     *
     * @param view view where display analysis results
     */
    public void setView(View view) {
        this.view = view;
    }

    /**
     * Set path to project to analyze
     *
     * @param pathProjectToAnalyze path to project to analyze
     */
    public void setPathProjectToAnalyze(final String pathProjectToAnalyze) {
        this.pathProjectToAnalyze = pathProjectToAnalyze;
    }

    /**
     * Start project analysis for project passed in {@link #setPathProjectToAnalyze(String)}
     */
    public void startAnalysisProject() {
        this.setViewBehaviourAtStarts();
        this.initializeEventBus();
        this.projectAnalyzer.analyzeProject(this.pathProjectToAnalyze, AnalysisController.VERTX_CHANNEL_TOPIC);
    }

    /**
     * Stop project analysis
     */
    public void stopAnalysisProject() {
        this.view.setStopEnabled(false);
        this.vertx.eventBus().publish(AnalysisController.VERTX_CHANNEL_TOPIC, Logger.STOP_ANALYZING_PROJECT);
    }

    /**
     * Save project report got from analysis in file named "output.json
     */
    public void saveProjectReportToFile() {
        try {
            var writer = new FileWriter(AnalysisController.OUTPUT_PATH);
            writer.write(DTOParser.parseStringToPrettyJSON(projectDTO));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            this.view.showError(Strings.SOMETHING_WENT_WRONG, Strings.SAVE_ERROR);
        }

    }

    private void initializeEventBus() {
        this.vertx.eventBus().consumer(AnalysisController.VERTX_CHANNEL_TOPIC, message -> this.manageMessage(message.body().toString()));
    }

    private void setViewBehaviourAtStarts() {
        this.view.setStartEnabled(false);
        this.view.setSaveEnabled(false);
        this.view.setStopEnabled(true);
    }

    private void manageMessage(final String message) {

        if (message.startsWith(Logger.CodeElementFound.PROJECT.getCode())) {
            this.projectDTO = DTOParser.parseProjectDTO(message.substring(Logger.CodeElementFound.PROJECT.getCode().length()));
            this.view.setSaveEnabled(true);
            this.view.setStopEnabled(false);
            this.view.renderTree(projectDTO);
        } else if (message.startsWith(Logger.CodeElementFound.PACKAGE.getCode())) {
            PackageDTO packageFound = DTOParser.parsePackageDTO(message.substring(Logger.CodeElementFound.PACKAGE.getCode().length()));
            this.view.printText("Found package " + packageFound.name() + " at path " + packageFound.path());
        } else if (message.startsWith(Logger.CodeElementFound.CLASS.getCode())) {
            var classFound = DTOParser.parseClassInterfaceDTO(message.substring(Logger.CodeElementFound.CLASS.getCode().length()));
            this.view.printText("Found class " + classFound.name() + " at path " + classFound.path());
        } else if (message.startsWith(Logger.CodeElementFound.INTERFACE.getCode())) {
            var interfaceFound = DTOParser.parseClassInterfaceDTO(message.substring(Logger.CodeElementFound.INTERFACE.getCode().length()));
            this.view.printText("Found interface " + interfaceFound.name() + " at path " + interfaceFound.path());
        } else if (message.startsWith(Logger.CodeElementFound.METHOD.getCode())) {
            var methodFound = DTOParser.parseMethodDTO(message.substring(Logger.CodeElementFound.METHOD.getCode().length()));
            this.view.printText("Found method " + methodFound.name());
        } else if (message.startsWith(Logger.CodeElementFound.FIELD.getCode())) {
            var fieldFound = DTOParser.parseFieldDTO(message.substring(Logger.CodeElementFound.FIELD.getCode().length()));
            this.view.printText("Found field " + fieldFound.name());
        } else if (message.startsWith(Logger.CodeElementFound.ERROR.getCode())) {
            this.view.printText("Error: " + message.substring(Logger.CodeElementFound.ERROR.getCode().length()));
            this.view.showError(message.substring(Logger.CodeElementFound.ERROR.getCode().length()), "ERROR");
            this.view.setStartEnabled(true);
        }
    }
}
