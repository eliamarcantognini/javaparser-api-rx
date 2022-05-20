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
     * Async method to retrieve the report about a variable number of  interface,
     * given the full path of the interface source file
     *
     * @param srcInterfacePath variable argument for file paths
     * @return InterfaceReport observable
     */
    Observable<InterfaceReport> getInterfaceReport(String... srcInterfacePath);

    /**
     * Async method to retrieve the report about a variable number of  class,
     * given the full path of the class source file
     *
     * @param srcClassPath variable argument for file paths
     * @return ClassReport observable
     */
    Observable<ClassReport> getClassReport(String... srcClassPath);

    /**
     * Async method to retrieve the report about a variable number of package,
     * given the full path of the package folder
     *
     * @param srcPackagePath variable argument for folder paths
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
     *
     * @param srcProjectFolderName path to project
     * @return an observable
     */
    Observable<ProjectDTO> analyzeProject(String srcProjectFolderName);
}
