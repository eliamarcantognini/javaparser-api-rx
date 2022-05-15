package lib.async;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.future.PromiseImpl;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Asynchronous project analyzer to get {@link lib.reports} about java sources.
 *
 * @see com.github.javaparser
 * @see io.vertx
 * @see lib.reports
 * @see lib.Logger
 * @see lib.ProjectAnalyzer
 * @see lib.visitors
 */
public class AsyncProjectAnalyzer implements ProjectAnalyzer {

    /**
     * Topic where messages are sent if no channel for {@link Vertx#eventBus()}
     * hasn't been specified yet
     */
    public static final String CHANNEL_DEFAULT = "default";

    private final Vertx vertx;
    private Logger logger;
    private final List<String> verticleIDs;

    /**
     * Constructor of class
     *
     * @param vertx vertx used for asynchronous processes
     */
    public AsyncProjectAnalyzer(final Vertx vertx) {
        this.vertx = vertx;
        logger = message -> vertx.eventBus().publish(CHANNEL_DEFAULT, message);
        this.verticleIDs = new ArrayList<>();
    }

    @Override
    public Future<InterfaceReport> getInterfaceReport(String srcInterfacePath) {
        return this.vertx.executeBlocking(ev -> {
            InterfacesVisitor interfaceVisitor = new InterfacesVisitor(logger);
            InterfaceReport interfaceReport = new InterfaceReportImpl();
            try {
                interfaceVisitor.visit(this.getCompilationUnit(srcInterfacePath), interfaceReport);
                logger.log(interfaceReport);
                ev.complete(interfaceReport);
            } catch (FileNotFoundException e) {
                ev.fail("EXCEPTION: getInterfaceReport has failed with message: " + e.getMessage());
            }
        });
    }

    @Override
    public Future<ClassReport> getClassReport(String srcClassPath) {
        return this.vertx.executeBlocking(ev -> {
            ClassesVisitor classVisitor = new ClassesVisitor(logger);
            ClassReport classReport = new ClassReportImpl();
            try {
                classVisitor.visit(this.getCompilationUnit(srcClassPath), classReport);
                logger.log(classReport);
                ev.complete(classReport);
            } catch (FileNotFoundException e) {
                ev.fail("EXCEPTION: getClassReport has failed with message: " + e.getMessage());
            }
        });
    }

    @Override
    public Future<PackageReport> getPackageReport(String srcPackagePath) {
        Promise<PackageReport> promise = new PromiseImpl<>();
        if (!new File(srcPackagePath).isDirectory()) {
            promise.fail("Package path is not a directory");
        } else {
            PackageVerticle vert = new PackageVerticle(this, promise, srcPackagePath, this.logger);
            this.vertx.deployVerticle(vert).onComplete(id -> this.verticleIDs.add(id.result()));
            promise.future().onFailure(res -> {
                if (!res.getMessage().equals(Logger.STOP_ANALYZING_PROJECT)) {
                    logger.logError(res.getMessage());
                }
            });
        }
        return promise.future();
    }


    @Override
    public Future<ProjectReport> getProjectReport(String srcProjectFolderPath) {
        Promise<ProjectReport> promise = new PromiseImpl<>();
        if (!new File(srcProjectFolderPath).isDirectory()) {
            promise.fail("Package path is not a directory");
        } else {
            ProjectVerticle vert = new ProjectVerticle(this, promise, srcProjectFolderPath, this.logger);
            this.vertx.deployVerticle(vert).onComplete(id -> this.verticleIDs.add(id.result()));
            promise.future().onFailure(res -> {
                if (!res.getMessage().equals(Logger.STOP_ANALYZING_PROJECT)) {
                    logger.logError(res.getMessage());
                }
            });
        }
        return promise.future();
    }

    @Override
    public void analyzeProject(String srcProjectFolderName, String topic) {
        this.vertx.eventBus().consumer(topic, m -> {
            if (m.body().toString().equals(Logger.STOP_ANALYZING_PROJECT)) this.stopLibrary();
        });
        this.logger = message -> vertx.eventBus().publish(topic, message);
        this.getProjectReport(srcProjectFolderName).onFailure(res -> {
            if (res.getMessage().equals(Logger.STOP_ANALYZING_PROJECT)) {
                logger.logInterrupt(res.getMessage());
            } else {
                logger.logError(res.getMessage());
            }
        });
    }

    private void stopLibrary() {
        this.verticleIDs.forEach(this.vertx::undeploy);
        this.verticleIDs.clear();
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
