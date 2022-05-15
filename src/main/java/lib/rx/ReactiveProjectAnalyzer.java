package lib.rx;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;
import lib.Logger;
import lib.ProjectAnalyzer;
import lib.reports.ClassReportImpl;
import lib.reports.InterfaceReportImpl;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;
import lib.visitors.ClassesVisitor;
import lib.visitors.InterfacesVisitor;

import java.io.File;
import java.io.FileNotFoundException;

public class ReactiveProjectAnalyzer implements ProjectAnalyzer {
    /**
     * Topic where messages are sent if no channel for {}
     * hasn't been specified yet
     */
    public static final String CHANNEL_DEFAULT = "default";

    private PublishSubject<ProjectReport> publishSubject;

    private final Logger logger;

    /**
     * Constructor of class
     */
    public ReactiveProjectAnalyzer() {
        this.logger = System.out::println;
    }

    @Override
    public Observable<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
        return Observable.create(emitter -> {
            var visitor = new InterfacesVisitor(logger);
            var report = new InterfaceReportImpl();
            visitor.visit(this.getCompilationUnit(srcInterfacePath), report);
//            logger.log(report);
            emitter.onNext(report);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<ClassReport> getClassReport(String srcClassPath) {
        return Observable.create(emitter -> {
            var visitor = new ClassesVisitor(logger);
            var report = new ClassReportImpl();
            visitor.visit(this.getCompilationUnit(srcClassPath), report);
//            logger.log(report);
            emitter.onNext(report);
            emitter.onComplete();
        });
    }

    @Override
    public Observable<PackageReport> getPackageReport(String srcPackagePath) {
//        final List<Observable<InterfaceReport>> interfaces = new ArrayList<>();
//        final List<Observable<ClassReport>> classes = new ArrayList<>();
//
//        return Observable.create(emitter -> {
//            var packageReport = new PackageReportImpl();
//            var folder = new File(srcPackagePath);
//            var list = Stream.of(Objects.requireNonNull(
//                            folder.listFiles((dir, name) -> name.endsWith(".java"))))
//                    .map(File::getPath)
//                    .toList();
//            list.forEach(path -> {
//                CompilationUnit cu;
//                try {
//                    cu = this.getCompilationUnit(path);
//                    packageReport.setName("");
//                    packageReport.setFullPath("");
//                    if (cu.getType(0).asClassOrInterfaceDeclaration().isInterface()) {
//                        interfaces.add(this.getInterfaceReport(path));
//                    } else {
//                        classes.add(this.getClassReport(path));
//                    }
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//            });
//            var interfaceObs = Observable.merge(interfaces);
//            interfaceObs.subscribe(packageReport::addInterfaceReport);
//            var classObs = Observable.merge(classes);
//            classObs.subscribe(packageReport::addClassReport);
//            var o = Observable.merge(interfaceObs, classObs).subscribe(onNext -> {
//                System.out.println("sono qui");
//                emitter.onNext(packageReport);
//                emitter.onComplete();
//            });
//
//        });
        return null;
    }


    @Override
    public Observable<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        Observable<ProjectReport> obs;

        return null;
    }

    @Override
    public void analyzeProject(String srcProjectFolderName, String topic) {
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
