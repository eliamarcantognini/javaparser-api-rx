package lib;

import io.reactivex.rxjava3.core.Observable;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;

/**
 * Interface of a project analyzer. It contains methods to get async
 * reports using {@link java.util.concurrent.Future}
 *
 * @see io.reactivex.rxjava3.core.Observable
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
    Observable<InterfaceReport> getInterfaceReport(String srcInterfacePath);

    /**
     * Async method to retrieve the report about a specific class,
     * given the full path of the class source file
     *
     * @param srcClassPath path to class
     * @return ClassReport future
     */
    Observable<ClassReport> getClassReport(String srcClassPath);

    /**
     * Async method to retrieve the report about a package,
     * given the full path of the package folder
     *
     * @param srcPackagePath path to package
     * @return PackageReport future
     */
    Observable<PackageReport> getPackageReport(String srcPackagePath);

    /**
     * Async method to retrieve the report about a project
     * given the full path of the project folder
     *
     * @param srcProjectFolderPath path to project
     * @return ProjectReport future
     */
    Observable<ProjectReport> getProjectReport(String srcProjectFolderPath);

    /**
     * Async function that analyze a project given the full path of the project folder.
     * Real time result were sent in topic specified.
     *  @param srcProjectFolderName path to project
     * @param topic                topic where analysis results messages where sent
     * @return
     */
    Observable<ProjectReport> analyzeProject(String srcProjectFolderName, String topic);
}
