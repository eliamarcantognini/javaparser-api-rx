package lib.reports.interfaces;

import java.util.List;

/**
 * Interface for project report. It does not extend Report because in this case there is not a name and path to set.
 */
public interface ProjectReport {

    /**
     * Get the main class of project
     *
     * @return the main class of project
     */
    ClassReport getMainClass();

    /**
     * Set the main class of project
     *
     * @param classReport the main class of project to set
     */
    void setMainClass(ClassReport classReport);

    /**
     * Get all packages reports of project packages
     *
     * @return all packages reports of project packages
     */
    List<PackageReport> getAllPackageReports();

    /**
     * Add package report to project report
     *
     * @param packageReport package report to add to project report
     */
    void addPackageReport(PackageReport packageReport);

    /**
     * Get a class report of class passed as parameter
     *
     * @param fullClassName the full class name of which get report
     * @return class report of class requested
     */
    ClassReport getClassReport(String fullClassName);
}
