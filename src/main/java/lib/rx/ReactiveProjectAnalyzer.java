package lib.rx;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import lib.Logger;
import lib.ProjectAnalyzer;
import lib.dto.DTOs;
import lib.dto.ProjectDTO;
import lib.reports.ClassReportImpl;
import lib.reports.InterfaceReportImpl;
import lib.reports.PackageReportImpl;
import lib.reports.ProjectReportImpl;
import lib.reports.interfaces.*;
import lib.visitors.ClassesVisitor;
import lib.visitors.InterfacesVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReactiveProjectAnalyzer implements ProjectAnalyzer {
    /**
     * Topic where messages are sent if no channel for {}
     * hasn't been specified yet
     */
    public static final String CHANNEL_DEFAULT = "default";

    private final Logger logger;

    /**
     * Constructor of class
     */
    public ReactiveProjectAnalyzer(Observer<String> observer) {
        this.logger = observer::onNext;
    }

    @Override
    public Observable<InterfaceReport> getInterfaceReport(String... srcInterfacePath) {
        return Observable.fromStream(Stream.of(srcInterfacePath).map(path -> {
            var report = new InterfaceReportImpl();
            try {
                new InterfacesVisitor(logger).visit(this.getCompilationUnit(path), report);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            logger.log(Logger.CodeElementFound.INTERFACE + " analyzed: " + report.getName() + " @ " + report.getSourceFullPath());
            return report;
        }));

    }

    @Override
    public Observable<ClassReport> getClassReport(String... srcClassPath) {
        return Observable.fromStream(Stream.of(srcClassPath).map(path -> {
            var report = new ClassReportImpl();
            try {
                new ClassesVisitor(logger).visit(this.getCompilationUnit(path), report);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            logger.log(Logger.CodeElementFound.CLASS + " analyzed: " + report.getName() + " @ " + report.getSourceFullPath());
            return report;
        }));
    }

    @Override
    public Observable<PackageReport> getPackageReport(String... srcPackagePath) {
        return Observable.fromStream(Stream.of(srcPackagePath).map((path) -> {
            var packageReport = new PackageReportImpl();
            packageReport.setFullPath(path);
            var folder = new File(path);
            Stream.of(Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".java"))))
                    .map(File::getPath)
                    .collect(Collectors.groupingBy(elem -> {
                        try {
                            return this.getCompilationUnit(elem).getType(0).asClassOrInterfaceDeclaration().isInterface();
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })).forEach((isInterface, list) -> {
                        if (isInterface) {
                            getInterfaceReport(list.toArray(new String[0])).subscribe(packageReport::addInterfaceReport);
                        } else {
                            getClassReport(list.toArray(new String[0])).subscribe(packageReport::addClassReport);
                        }
                    });
            setPackageNameAndFullName(packageReport);
            logger.log(Logger.CodeElementFound.PACKAGE + " analyzed: " + packageReport.getName());
            return packageReport;
        }));
    }


    @Override
    public Observable<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        return Observable.fromCallable(() -> {
            var projectReport = new ProjectReportImpl();
            final File folder = new File(srcProjectFolderPath);

            var list = Stream
                    .concat(Stream.of(folder.toString()), Stream.of(Objects.requireNonNull(folder.listFiles()))
                    .filter(File::isDirectory)
                    .map(File::getPath))
                    .toList();
            getPackageReport(list.toArray(new String[0])).subscribe(item -> {
                item.getClassesReports().stream().filter(m -> m.getName().equals("main")).findFirst().ifPresent(projectReport::setMainClass);
                projectReport.addPackageReport(item);
            });
            return projectReport;
        });

    }

    @Override
    public Observable<ProjectDTO> analyzeProject(String srcProjectFolderName, String topic) {
        return Observable.fromCallable(() -> {
            final ProjectDTO[] res = {DTOs.createProjectDTO(new ProjectReportImpl())};
            getProjectReport(srcProjectFolderName).subscribe(e -> res[0] = DTOs.createProjectDTO(e));
            return res[0];
        });

    }

    private void stopLibrary() {
    }

    /**
     * Get compilation unit of file passed. Throw an error if file not exists
     *
     * @param path path of file to parse
     * @return compilation unit of file parsed
     * @throws FileNotFoundException if path passed not correspond to any file
     */
    CompilationUnit getCompilationUnit(String path) throws FileNotFoundException {
        return StaticJavaParser.parse(new File(path));
    }

    private void setPackageNameAndFullName(PackageReportImpl packageReport) {
        var name = "Package name not found";
        var fullPath = "Full name not found";
        Report report = !packageReport.getClassesReports().isEmpty() ? packageReport.getClassesReports().get(0) :
                (!packageReport.getInterfaceReports().isEmpty() ? packageReport.getInterfaceReports().get(0) : null);
        if (report != null) {
            var s = report.getSourceFullPath().split("\\.");
            fullPath = s.length == 1 ? "" : report.getSourceFullPath().substring(0, report.getSourceFullPath().length() - report.getName().length() - 1);
            name = s.length == 1 ? "." : (s[s.length - 2]);
        }
        packageReport.setName(name);
        packageReport.setFullPath(fullPath);
    }

}
