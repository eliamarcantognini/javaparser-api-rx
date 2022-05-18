package lib.rx;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import lib.Logger;
import lib.ProjectAnalyzer;
import lib.dto.DTOParser;
import lib.dto.DTOs;
import lib.reports.ClassReportImpl;
import lib.reports.InterfaceReportImpl;
import lib.reports.PackageReportImpl;
import lib.reports.ProjectReportImpl;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;
import lib.visitors.ClassesVisitor;
import lib.visitors.InterfacesVisitor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
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
    public Observable<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
        return Observable.fromCallable(() -> {
            var report = new InterfaceReportImpl();
            new InterfacesVisitor(logger).visit(this.getCompilationUnit(srcInterfacePath), report);
            logger.log(Logger.CodeElementFound.INTERFACE.getCode() + DTOParser.parseString(DTOs.createInterfaceDTO(report)));
            return report;
        });
    }

    @Override
    public Observable<ClassReport> getClassReport(String srcClassPath) {
        return Observable.fromCallable(() -> {
            var report = new ClassReportImpl();
            new ClassesVisitor(logger).visit(this.getCompilationUnit(srcClassPath), report);
            logger.log(Logger.CodeElementFound.CLASS.getCode() + DTOParser.parseString(DTOs.createClassDTO(report)));
            return report;
        });
    }

    @Override
    public Observable<PackageReport> getPackageReport(String srcPackagePath) {
        return Observable.fromCallable(() -> {
            var packageReport = new PackageReportImpl();
            var set = new AtomicBoolean(false);
            var folder = new File(srcPackagePath);
            var list = Stream
                    .of(Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".java"))))
                    .map(File::getPath)
                    .toList();
            list.forEach(path -> {
                CompilationUnit cu;
                try {
                    cu = this.getCompilationUnit(path);
                    if (cu.getType(0).asClassOrInterfaceDeclaration().isInterface()) {
                        getInterfaceReport(path).subscribe(report -> {
                            setPackageNameAndPath(packageReport, set, report.getName(), report.getSourceFullPath());
                            packageReport.addInterfaceReport(report);
                        });
                    } else {
                        getClassReport(path).subscribe(report -> {
                            setPackageNameAndPath(packageReport, set, report.getName(), report.getSourceFullPath());
                            packageReport.addClassReport(report);
                        });
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            logger.log(Logger.CodeElementFound.PACKAGE.getCode() + DTOParser.parseString(DTOs.createPackageDTO(packageReport)));
            return packageReport;
        });

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
            list.forEach(path -> getPackageReport(path).subscribe(packageReport -> {
                packageReport.getClassesReports()
                        .forEach(c -> c.getMethodsInfo()
                                .forEach(m -> {
                                    if (m.getName().equals("main")) projectReport.setMainClass(c);
                                }));
                projectReport.addPackageReport(packageReport);
            }));
            logger.log(Logger.CodeElementFound.PROJECT.getCode() + DTOParser.parseString(DTOs.createProjectDTO(projectReport)));
            return projectReport;
        });

    }

    @Override
    public void analyzeProject(String srcProjectFolderName) {
        // Implementation full reactive, but is all done by the logger observer.
        getProjectReport(srcProjectFolderName).subscribe(o -> logger.log("Finished."));
        // First implementation.
//        return Observable.fromCallable(() -> {
//            final ProjectDTO[] res = {DTOs.createProjectDTO(new ProjectReportImpl())};
//            getProjectReport(srcProjectFolderName).subscribe(e -> res[0] = DTOs.createProjectDTO(e));
//            return res[0];
//        });
        // Implementation full reactive, every event is sent in the same observable as string.
//        return Observable.create(emitter -> {
//            var projectReport = new ProjectReportImpl();
//            var folder = new File(srcProjectFolderName);
//            var packages = Stream
//                    .concat(Stream.of(folder.toString()), Stream.of(Objects.requireNonNull(folder.listFiles()))
//                            .filter(File::isDirectory)
//                            .map(File::getPath))
//                    .toList();
//            for (String pkg : packages) {
//                var packageReport = new PackageReportImpl();
//                var pkgNameSet = false;
//                var files = Stream
//                        .of(Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".java"))))
//                        .map(File::getPath)
//                        .toList();
//                for (String file : files) {
//                    CompilationUnit cu;
//                    try {
//                        cu = this.getCompilationUnit(file);
//                        if (cu.getType(0).asClassOrInterfaceDeclaration().isInterface()) {
//                            var report = new InterfaceReportImpl();
//                            new InterfacesVisitor(logger).visit(this.getCompilationUnit(file), report);
//                            setPackageNameAndPath(packageReport, pkgNameSet, report.getName(), report.getSourceFullPath());
//                            packageReport.addInterfaceReport(report);
//                            emitter.onNext(Logger.CodeElementFound.INTERFACE.getCode() + DTOParser.parseString(DTOs.createInterfaceDTO(report)));
//                        } else {
//                            var report = new ClassReportImpl();
//                            new ClassesVisitor(logger).visit(this.getCompilationUnit(file), report);
//                            setPackageNameAndPath(packageReport, pkgNameSet, report.getName(), report.getSourceFullPath());
//                            packageReport.addClassReport(report);
//                            emitter.onNext(Logger.CodeElementFound.CLASS.getCode() + DTOParser.parseString(DTOs.createClassDTO(report)));
//                        }
//                    } catch (FileNotFoundException e) {}
//                }
//                projectReport.addPackageReport(packageReport);
//                emitter.onNext(Logger.CodeElementFound.PACKAGE.getCode() + DTOParser.parseString(DTOs.createPackageDTO(packageReport)));
//            }
//            emitter.onNext(Logger.CodeElementFound.PROJECT.getCode() + DTOParser.parseString(DTOs.createProjectDTO(projectReport)));
//            emitter.onComplete();
//        });
    }

//    private void setPackageNameAndPath(PackageReport packageReport, Boolean set, String name, String sourceFullPath) {
//        if (!set) {
//            var s = sourceFullPath.split("\\.");
//            packageReport.setName(s.length == 1 ? "." : (s[s.length - 2]));
//            packageReport.setFullPath(s.length == 1 ? "" : sourceFullPath.substring(0, sourceFullPath.length() - name.length() - 1));
//            set = true;
//        }
//    }

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

    private void setPackageNameAndPath(PackageReport packageReport, AtomicBoolean set, String name, String sourceFullPath) {
        if (!set.get()) {
            var s = sourceFullPath.split("\\.");
            packageReport.setName(s.length == 1 ? "." : (s[s.length - 2]));
            packageReport.setFullPath(s.length == 1 ? "" : sourceFullPath.substring(0, sourceFullPath.length() - name.length() - 1));
            set.set(true);
        }
    }
}
