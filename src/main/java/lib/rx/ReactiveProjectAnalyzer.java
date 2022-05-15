package lib.rx;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import lib.Logger;
import lib.ProjectAnalyzer;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Future;

public class ReactiveProjectAnalyzer implements ProjectAnalyzer {
    /**
     * Topic where messages are sent if no channel for {}
     * hasn't been specified yet
     */
    public static final String CHANNEL_DEFAULT = "default";

    private Logger logger;

    /**
     * Constructor of class
     */
    public ReactiveProjectAnalyzer() {
    }

    @Override
    public Future<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
//        return this.vertx.executeBlocking(ev -> {
//            InterfacesVisitor interfaceVisitor = new InterfacesVisitor(logger);
//            InterfaceReport interfaceReport = new InterfaceReportImpl();
//            try {
//                interfaceVisitor.visit(this.getCompilationUnit(srcInterfacePath), interfaceReport);
//                logger.log(interfaceReport);
//                ev.complete(interfaceReport);
//            } catch (FileNotFoundException e) {
//                ev.fail("EXCEPTION: getInterfaceReport has failed with message: " + e.getMessage());
//            }
//        });
        return null;
    }

    @Override
    public Future<ClassReport> getClassReport(String srcClassPath) {
//        return this.vertx.executeBlocking(ev -> {
//            ClassesVisitor classVisitor = new ClassesVisitor(logger);
//            ClassReport classReport = new ClassReportImpl();
//            try {
//                classVisitor.visit(this.getCompilationUnit(srcClassPath), classReport);
//                logger.log(classReport);
//                ev.complete(classReport);
//            } catch (FileNotFoundException e) {
//                ev.fail("EXCEPTION: getClassReport has failed with message: " + e.getMessage());
//            }
//        });
        return null;
    }

    @Override
    public Future<PackageReport> getPackageReport(String srcPackagePath) {
//        Promise<PackageReport> promise = new PromiseImpl<>();
//        if (!new File(srcPackagePath).isDirectory()) {
//            promise.fail("Package path is not a directory");
//        } else {
//            PackageVerticle vert = new PackageVerticle(this, promise, srcPackagePath, this.logger);
//            this.vertx.deployVerticle(vert).onComplete(id -> this.verticleIDs.add(id.result()));
//            promise.future().onFailure(res -> {
//                if (!res.getMessage().equals(Logger.STOP_ANALYZING_PROJECT)) {
//                    logger.logError(res.getMessage());
//                }
//            });
//        }
//        return promise.future();
        return null;
    }


    @Override
    public Future<ProjectReport> getProjectReport(String srcProjectFolderPath) {
//        Promise<ProjectReport> promise = new PromiseImpl<>();
//        if (!new File(srcProjectFolderPath).isDirectory()) {
//            promise.fail("Package path is not a directory");
//        } else {
//            ProjectVerticle vert = new ProjectVerticle(this, promise, srcProjectFolderPath, this.logger);
//            this.vertx.deployVerticle(vert).onComplete(id -> this.verticleIDs.add(id.result()));
//            promise.future().onFailure(res -> {
//                if (!res.getMessage().equals(Logger.STOP_ANALYZING_PROJECT)) {
//                    logger.logError(res.getMessage());
//                }
//            });
//        }
//        return promise.future();
        return null;
    }

    @Override
    public void analyzeProject(String srcProjectFolderName, String topic) {
//        this.vertx.eventBus().consumer(topic, m -> {
//            if (m.body().toString().equals(Logger.STOP_ANALYZING_PROJECT)) this.stopLibrary();
//        });
//        this.logger = message -> vertx.eventBus().publish(topic, message);
//        this.getProjectReport(srcProjectFolderName).onFailure(res -> {
//            if (res.getMessage().equals(Logger.STOP_ANALYZING_PROJECT)) {
//                logger.logInterrupt(res.getMessage());
//            } else {
//                logger.logError(res.getMessage());
//            }
//        });
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
