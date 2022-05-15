package lib.reports.info.interfaces;

import lib.reports.interfaces.Report;

import java.util.Optional;

/**
 * Interface to wrap information about classes and interfaces methods
 *
 * @see lib.reports.interfaces.ClassReport
 * @see lib.reports.interfaces.InterfaceReport
 */
public interface MethodInfo {

    /**
     * Get the name of the method
     *
     * @return the name
     */
    String getName();

    /**
     * Get the line in the source code in which the methods begin, counting from 1
     *
     * @return the number of the line
     */
    int getSrcBeginLine();

    /**
     * Get the line in the source code in which the methods end, counting from 1
     *
     * @return the number of the line
     */
    int getEndBeginLine();

    /**
     * Get all the modifiers of the method, if present
     *
     * @return an {@link Optional<String>} if the modifiers are present, Empty otherwise
     */
    Optional<String> getModifiers();

    /**
     * Get the full parent report, {@link lib.reports.interfaces.ClassReport} or {@link lib.reports.interfaces.InterfaceReport}
     *
     * @return the parent report
     */
    Report getParentClassOrInterfaceReport();
}
