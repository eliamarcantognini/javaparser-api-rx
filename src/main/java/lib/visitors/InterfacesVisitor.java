package lib.visitors;

import lib.Logger;
import lib.reports.interfaces.InterfaceReport;

/**
 * Visitor for interfaces that extends from {@link FileVisitor}
 *
 * @see com.github.javaparser.ast.visitor
 */
public class InterfacesVisitor extends FileVisitor<InterfaceReport> {

    /**
     * Class constructor
     *
     * @param logger logger where send message when incurred new find
     */
    public InterfacesVisitor(Logger logger) {
        super(false, logger);
    }
}
