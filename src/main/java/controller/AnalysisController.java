package controller;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lib.Logger;
import lib.ProjectAnalyzer;
import lib.dto.DTOParser;
import lib.dto.PackageDTO;
import lib.dto.ProjectDTO;
import lib.rx.ReactiveProjectAnalyzer;
import view.View;
import view.utils.Strings;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Controller to manage application about project analysis
 *
 * @see ProjectAnalyzer
 * @see lib.dto
 */
public class AnalysisController {

    /**
     * File where analysis will be saved
     */
    private final static String OUTPUT_PATH = "./output.json";

    private final ProjectAnalyzer projectAnalyzer;
    private ProjectDTO projectDTO;
    private View view;
    private String pathProjectToAnalyze;
    private Disposable projectDisposable;

    /**
     * Constructor of class
     */
    public AnalysisController() {
        /*
         * Implement the onNext method to manage the library's logger stream of messages.
         * In this case, the controller takes every message sent by the logger and manage it in the manageMessage method.
         */
        Observer<String> observer = new Observer<>() {
            public void onSubscribe(@NonNull Disposable d) {}
            @Override
            public void onNext(@NonNull String s) { manageMessage(s); }
            public void onError(@NonNull Throwable e) {}
            public void onComplete() {}
        };
        this.projectAnalyzer = new ReactiveProjectAnalyzer(observer);
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
        this.setViewBehaviourAtStarts(true);
        projectDisposable = this.projectAnalyzer.analyzeProject(this.pathProjectToAnalyze).subscribe(result -> {
            this.projectDTO = result;
            this.view.setStopEnabled(false);
            this.view.setSaveEnabled(true);
            this.view.renderTree(projectDTO);
            this.view.printText("PROJECT ANALYZE DONE.");
        });
    }

    /**
     * Stop project analysis
     */
    public void stopAnalysisProject() {
        setViewBehaviourAtStarts(false);
        projectDisposable.dispose();
    }

    /**
     * Save project report got from analysis in file named "output.json"
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

    private void setViewBehaviourAtStarts(boolean start) {
        this.view.setStartEnabled(!start);
        this.view.setSaveEnabled(!start);
        this.view.setStopEnabled(start);
    }

    /**
     * Method that takes a string and based on the code found at its start it does an action.
     *
     * @param message the String to manage
     */
    private void manageMessage(final String message) {
        if (message.startsWith(Logger.CodeElementFound.PROJECT.getCode())) {
            this.projectDTO = DTOParser.parseProjectDTO(message.substring(Logger.CodeElementFound.PROJECT.getCode().length()));
            var s = projectDTO.mainClass().name().isBlank() ? "" : ("entry point at " + projectDTO.mainClass().name() + " class and ");
            this.view.printText("Finished parsing. Project has " + s + projectDTO.packages().size() + " packages.");
        } else if (message.startsWith(Logger.CodeElementFound.PACKAGE.getCode())) {
            PackageDTO packageFound = DTOParser.parsePackageDTO(message.substring(Logger.CodeElementFound.PACKAGE.getCode().length()));
            this.view.printText("Found package " + packageFound.path());
        } else if (message.startsWith(Logger.CodeElementFound.CLASS.getCode())) {
            var classFound = DTOParser.parseClassInterfaceDTO(message.substring(Logger.CodeElementFound.CLASS.getCode().length()));
            this.view.printText("Found class " + classFound.name() + " with fully classified name " + classFound.path());
        } else if (message.startsWith(Logger.CodeElementFound.INTERFACE.getCode())) {
            var interfaceFound = DTOParser.parseClassInterfaceDTO(message.substring(Logger.CodeElementFound.INTERFACE.getCode().length()));
            this.view.printText("Found interface " + interfaceFound.name() + " with fully classified name " + interfaceFound.path());
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
