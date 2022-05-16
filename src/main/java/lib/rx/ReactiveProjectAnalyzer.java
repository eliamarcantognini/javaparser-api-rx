package lib.rx;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.reactivex.rxjava3.core.Observable;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public ReactiveProjectAnalyzer() {
        this.logger = message -> System.out.println("Logger: " + message);

    }

    @Override
    public Observable<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
//        return Observable.create(emitter -> {
//            var visitor = new InterfacesVisitor(logger);
//            var report = new InterfaceReportImpl();
//            visitor.visit(this.getCompilationUnit(srcInterfacePath), report);
//            emitter.onNext(report);
//            emitter.onComplete();
//        });
        return Observable.fromCallable(() -> {
           var report = new InterfaceReportImpl();
           new InterfacesVisitor(logger).visit(this.getCompilationUnit(srcInterfacePath), report);
           return report;
        });
    }

    @Override
    public Observable<ClassReport> getClassReport(String srcClassPath) {
//        return Observable.create(emitter -> {
//            var visitor = new ClassesVisitor(logger);
//            var report = new ClassReportImpl();
//            visitor.visit(this.getCompilationUnit(srcClassPath), report);
//            emitter.onNext(report);
//            emitter.onComplete();
//        });
        return Observable.fromCallable(() -> {
            var report = new ClassReportImpl();
            new ClassesVisitor(logger).visit(this.getCompilationUnit(srcClassPath), report);
            return report;
        });
    }

    @Override
    public Observable<PackageReport> getPackageReport(String srcPackagePath) {
        final List<Observable<InterfaceReport>> interfaces = new ArrayList<>();
        final List<Observable<ClassReport>> classes = new ArrayList<>();

        return Observable.fromCallable(() -> {
            var packageReport = new PackageReportImpl();
            var folder = new File(srcPackagePath);
            var list = Stream.of(Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".java")))).map(File::getPath).toList();
            list.forEach(path -> {
                CompilationUnit cu;
                try {
                    cu = this.getCompilationUnit(path);
                    packageReport.setName("");
                    packageReport.setFullPath("");
                    if (cu.getType(0).asClassOrInterfaceDeclaration().isInterface()) {
                        getInterfaceReport(path).subscribe(packageReport::addInterfaceReport);
                    } else {
                        getClassReport(path).subscribe(packageReport::addClassReport);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            });
            return packageReport;
        });

    }


    @Override
    public Observable<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        return Observable.fromCallable(() -> {
            var projectReport = new ProjectReportImpl();
            final File folder = new File(srcProjectFolderPath);

            var list = Stream.concat(Stream.of(folder.toString()), Stream.of(Objects.requireNonNull(folder.listFiles())).filter(File::isDirectory).map(File::getPath)).toList();
            list.forEach(path -> {
                getPackageReport(path).subscribe(projectReport::addPackageReport);
            });
            projectReport.setMainClass(new ClassReportImpl());
            return projectReport;
        });

    }

    @Override
    public Observable<String> analyzeProject(String srcProjectFolderName, String topic) {
        return Observable.fromCallable(() -> {
            final String[] res = {""};
            getProjectReport(srcProjectFolderName).subscribe(e -> {
                res[0] = DTOParser.parseString(DTOs.createProjectDTO(e));
            });
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
}
