package view;

import lib.dto.ClassInterfaceDTO;
import lib.dto.PackageDTO;
import lib.dto.ProjectDTO;

/**
 * The View interface define the behaviour needs to use the library within a custom GUI or CLI.
 */
public interface View {

    /**
     * Method called by the controller to say if it's possible to stop the analysis.
     *
     * @param enabled - is possible to stop the analysis
     */
    void setStopEnabled(final Boolean enabled);

    /**
     * Method called by the controller to say if it's possible to start the analysis.
     *
     * @param enabled - is possible to start the analysis
     */
    void setStartEnabled(final Boolean enabled);

    /**
     * Method called by the controller to say if it's possible to save the analysis.
     *
     * @param enabled - is possible to save the analysis
     */
    void setSaveEnabled(final Boolean enabled);

    /**
     * Launch the View
     */
    void launch();

    /**
     * Display an error message
     *
     * @param message message to display
     * @param title   title of the view
     */
    void showError(final String message, final String title);

    /**
     * Print the text
     *
     * @param text the text to print
     */
    void printText(String text);

    /**
     * Render the DTO in a Tree structure
     *
     * @param dto the report
     * @param <T> a {@link ClassInterfaceDTO} or {@link PackageDTO} or {@link ProjectDTO}
     */
    <T> void renderTree(T dto);

}
