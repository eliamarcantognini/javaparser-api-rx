package lib.reports.interfaces;

import java.util.List;

/**
 * Interface for package report that extends from {@link Report}
 */
public interface PackageReport extends Report {

    /**
     * Get reports of classes inside package report
     *
     * @return a list of {@link ClassReport} inside package
     */
    List<ClassReport> getClassesReports();

    /**
     * Add class report to class reports of package report
     *
     * @param classReport class report to add to package report
     */
    void addClassReport(ClassReport classReport);

    /**
     * Get reports of interfaces inside package report
     *
     * @return a list of {@link InterfaceReport} inside package
     */
    List<InterfaceReport> getInterfaceReports();

    /**
     * Add interface report to interfaces report of package report
     *
     * @param interfaceReport interface report to add to package report
     */
    void addInterfaceReport(InterfaceReport interfaceReport);

}
