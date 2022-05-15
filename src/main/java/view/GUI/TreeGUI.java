package view.GUI;

import lib.dto.ClassInterfaceDTO;
import lib.dto.DTOParser;
import lib.dto.DTOs;
import lib.dto.PackageDTO;
import lib.dto.ProjectDTO;
import view.View;
import view.ViewListener;
import view.utils.Commands;
import view.utils.Strings;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Analysis GUI
 */
public class TreeGUI implements View {

    private static final int HEIGHT_OFFSET = 30;
    private static final int WIDTH_DIVISOR = 2;
    public static boolean error = false;

    private VisualiserFrame frame;
    private JButton btnStart;
    private JButton btnStop;
    private JButton btnSave;

    private final ViewListener listener;
    private final LoggerGUI loggerGUI;


    /**
     * Constructor of the GUI
     *
     * @param listener the listener for the view
     */
    public TreeGUI(ViewListener listener) {
        this.listener = listener;
        loggerGUI = new LoggerGUI();
    }

    /**
     * Launch GUI
     *
     * @see VisualiserFrame
     */
    public void launch() {
        var h = Toolkit.getDefaultToolkit().getScreenSize().height - HEIGHT_OFFSET;
        var w = Toolkit.getDefaultToolkit().getScreenSize().width / WIDTH_DIVISOR;
        frame = new VisualiserFrame(w, h);
        loggerGUI.launch();
    }

    @Override
    public void setStopEnabled(final Boolean enabled) {
        this.btnStop.setEnabled(enabled);
    }

    @Override
    public void setStartEnabled(final Boolean enabled) {
        this.btnStart.setEnabled(enabled);
    }

    @Override
    public void setSaveEnabled(Boolean enabled) {
        this.btnSave.setEnabled(enabled);
    }

    /**
     * Set render tree in view to display dto passed as parameter
     *
     * @param dto the report to show with a render tree
     * @param <T> DTO report type
     * @see DTOs
     * @see DTOParser
     */
    public <T> void renderTree(T dto) {
        var node = new DefaultMutableTreeNode();
        if (dto instanceof ProjectDTO) node = Trees.createProjectTreeNode((ProjectDTO) dto);
        else if (dto instanceof PackageDTO) node = Trees.createPackageTreeNode((PackageDTO) dto);
        else if (dto instanceof ClassInterfaceDTO) node = Trees.createClassOrInterfaceTreeNode((ClassInterfaceDTO) dto);
        JTree tree = new JTree(new DefaultTreeModel(node));

        JScrollPane scrollPane = new JScrollPane(tree, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.validate();
    }

    /**
     * Print in view text passed as parameter
     *
     * @param textToPrint text to print in the view
     */
    public void printText(String textToPrint) {
        SwingUtilities.invokeLater(() -> loggerGUI.printText(textToPrint));
    }

    /**
     * Display a dialog to show the message error passed as parameter
     *
     * @param message message to display in dialog
     * @param title   title of the dialog
     * @see InfoDialog#showDialog(String, String, int)
     */
    public void showError(final String message, final String title) {
        if (!error) {
            SwingUtilities.invokeLater(() -> {
                var j = new JOptionPane();
                var o = j.createDialog(frame, title);
                j.setMessage(message);
                o.addWindowListener(new MyWindowListener());
                o.setVisible(true);
            });
        }
    }

    public static void setError(boolean enabled) {
        error = enabled;
    }

    private class VisualiserFrame extends JFrame {

        public VisualiserFrame(int w, int h) {
            getContentPane().setLayout(new BorderLayout());
            setTitle(Strings.TITLE);
            setSize(w, h);
            setResizable(true);

            var btnPane = new JPanel();
            btnStart = new JButton(Strings.START);
            btnStop = new JButton(Strings.STOP);
            btnSave = new JButton(Strings.SAVE);
            btnStart.addActionListener(e -> listener.eventPerformed(Commands.START_ANALYSIS));
            btnStop.addActionListener(e -> listener.eventPerformed(Commands.STOP_ANALYSIS));
            btnStop.setEnabled(false);
            btnSave.addActionListener(e -> listener.eventPerformed(Commands.SAVE_REPORT_INSIDE_FILE));
            btnSave.setEnabled(false);
            btnPane.add(btnStart);
            btnPane.add(btnStop);
            btnPane.add(btnSave);

            getContentPane().add(btnPane, BorderLayout.NORTH);

            this.setVisible(true);
        }

    }

    private static class MyWindowListener implements WindowListener {
        @Override
        public void windowOpened(WindowEvent e) {
            setError(true);
        }
        @Override
        public void windowClosed(WindowEvent e) {
            setError(false);
        }
        public void windowClosing(WindowEvent e) {}
        public void windowIconified(WindowEvent e) {}
        public void windowDeiconified(WindowEvent e) {}
        public void windowActivated(WindowEvent e) {}
        public void windowDeactivated(WindowEvent e) {}
    }

}
