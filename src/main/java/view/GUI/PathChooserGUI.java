package view.GUI;

import view.utils.Strings;

import javax.swing.*;
import java.io.File;

/**
 * Static class for {@link JFileChooser} path chooser
 *
 * @see JFileChooser
 */
public class PathChooserGUI {

    /**
     * Create a {@link JFileChooser} and return the absolute path to file selected
     *
     * @return absolute path to file selected with chooser
     */
    public static String getFolderPath() {
        JFileChooser chooser = new JFileChooser();
        File f = new java.io.File(".");
        chooser.setCurrentDirectory(f);
        chooser.setDialogTitle(Strings.CHOOSER);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        var choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) return chooser.getSelectedFile().getAbsolutePath();
        return "";
    }
}
