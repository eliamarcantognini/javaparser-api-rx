package lib;

import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;

import java.util.concurrent.Future;

/**
 * Interface of a project analyzer. It contains methods to get async
 * reports using {@link java.util.concurrent.Future}
 *
 * @see java.util.concurrent.Future
 * @see InterfaceReport
 * @see ClassReport
 * @see PackageReport
 * @see ProjectReport
 */
public interface ProjectAnalyzer {

    /**
     * Async method to retrieve the report about a specific interface,
     * given the full path of the interface source file
     *
     * @param srcInterfacePath path to interface
     * @return InterfaceReport future
     */
    Future<InterfaceReport> getInterfaceReport(String srcInterfacePath);

    /**
     * Async method to retrieve the report about a specific class,
     * given the full path of the class source file
     *
     * @param srcClassPath path to class
     * @return ClassReport future
     */
    Future<ClassReport> getClassReport(String srcClassPath);

    /**
     * Async method to retrieve the report about a package,
     * given the full path of the package folder
     *
     * @param srcPackagePath path to package
     * @return PackageReport future
     */
    Future<PackageReport> getPackageReport(String srcPackagePath);

    /**
     * Async method to retrieve the report about a project
     * given the full path of the project folder
     *
     * @param srcProjectFolderPath path to project
     * @return ProjectReport future
     */
    Future<ProjectReport> getProjectReport(String srcProjectFolderPath);

    /**
     * Async function that analyze a project given the full path of the project folder.
     * Real time result were sent in topic specified.
     *
     * @param srcProjectFolderName path to project
     * @param topic                topic where analysis results messages where sent
     */
    void analyzeProject(String srcProjectFolderName, String topic);
}
