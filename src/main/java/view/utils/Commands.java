package view.utils;

import view.GUI.TreeGUI;
import view.ViewListener;

/**
 * Commands use to manage analyzing of a project.
 *
 * @see ViewListener
 * @see TreeGUI
 */
public enum Commands {

    /**
     * Command to select analyzeProject API
     */
    SELECT_PROJECT,

    /**
     * Command to start analyze project.
     */
    START_ANALYSIS,

    /**
     * Command to stop analyze project.
     */
    STOP_ANALYSIS,

    /**
     * Command to save output JSON
     */
    SAVE_REPORT_INSIDE_FILE,
}
