package lib;

import io.reactivex.rxjava3.core.Observable;
import lib.dto.ProjectDTO;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;

/**
 * Interface of a project analyzer. It contains methods to get async
 * reports using {@link io.reactivex.rxjava3.core.Observable}
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
     * @return InterfaceReport observable
     */
    Observable<InterfaceReport> getInterfaceReport(String... srcInterfacePath);

    /**
     * Async method to retrieve the report about a specific class,
     * given the full path of the class source file
     *
     * @param srcClassPath path to class
     * @return ClassReport observable
     */
    Observable<ClassReport> getClassReport(String... srcClassPath);

    /**
     * Async method to retrieve the report about a package,
     * given the full path of the package folder
     *
     * @param srcPackagePath path to package
     * @return PackageReport observable
     */
    Observable<PackageReport> getPackageReport(String... srcPackagePath);

    /**
     * Async method to retrieve the report about a project
     * given the full path of the project folder
     *
     * @param srcProjectFolderPath path to project
     * @return ProjectReport observable
     */
    Observable<ProjectReport> getProjectReport(String srcProjectFolderPath);

    /**
     * Async function that analyze a project given the full path of the project folder.
     * Real time result were sent in topic specified.
     *
     * @param srcProjectFolderName path to project
     * @return an observable
     */
    Observable<ProjectDTO> analyzeProject(String srcProjectFolderName);
}
