package view.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Class to manage view dialog
 *
 * @see JOptionPane
 */
public class InfoDialog {

    /**
     * Create a dialog {@link JOptionPane} to inform about what is passed as parameters
     *
     * @param text        test inside panel
     * @param title       title of panel displayed
     * @param messageType type of message displayed
     * @see JOptionPane#showMessageDialog(Component, Object, String, int)
     */
    public static void showDialog(String text, String title, int messageType) {
        JOptionPane.showMessageDialog(null, text, title, messageType);
    }

}
