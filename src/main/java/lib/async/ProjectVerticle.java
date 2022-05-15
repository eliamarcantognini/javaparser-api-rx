package lib.async;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import lib.Logger;
import lib.reports.ProjectReportImpl;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Class that manage project analysis. It extends from {@link AbstractVerticle} and when the verticle
 * is deployed start the analysis with parameter passed in constructor.
 *
 * @see io.vertx.core.AbstractVerticle
 */
public class ProjectVerticle extends AbstractVerticle {

    private static final String NO_MAIN_CLASS_MESSAGE = "Main class not found in this project";

    private final Promise<ProjectReport> promise;
    private final ProjectReport projectReport = new ProjectReportImpl();
    private final AsyncProjectAnalyzer analyzer;
    private final String projectPath;
    private final Logger logger;

    /**
     * Class constructor
     *
     * @param analyzer    analyzer to use
     * @param promise     promise where {@link ProjectReport} where will be present
     * @param projectPath path to project to analyze
     * @param logger      logger used to send message during analysis
     */
    public ProjectVerticle(AsyncProjectAnalyzer analyzer, Promise<ProjectReport> promise, String projectPath, Logger logger) {
        this.analyzer = analyzer;
        this.promise = promise;
        this.projectPath = projectPath;
        this.logger = logger;
    }

    /**
     * Start execution of the verticle. Analyze the project found at the path set in the constructor and complete the Future also
     * passed in the Constructor with the results of the analysis as a {@link ProjectReport}.
     */
    @Override
    public void start() {
        final List<Future<PackageReport>> packageReports = new ArrayList<>();
        final File folder = new File(projectPath);

        var list = Stream
                .concat(Stream.of(folder.toString()),
                        Stream.of(Objects.requireNonNull(folder.listFiles()))
                                .filter(File::isDirectory)
                                .map(File::getPath))
                .toList();

        list.forEach(path -> {
            Future<PackageReport> f = analyzer.getPackageReport(path);
            var futureCompose = f.compose(report -> {
                projectReport.addPackageReport(report);
                report.getClassesReports().forEach(c -> c.getMethodsInfo().forEach(m -> {
                    if (m.getName().equals("main")) projectReport.setMainClass(c);
                }));

                return Future.succeededFuture(report);

            });
            packageReports.add(futureCompose);
        });

        MyCompositeFuture.join(packageReports).onSuccess(r -> {
            if (projectReport.getMainClass().getName().isBlank()) logger.log(NO_MAIN_CLASS_MESSAGE);
            if (promise.tryComplete(projectReport)) {
                logger.log(projectReport);
            }
        });
    }

    /**
     * Stop the verticle execution and set the Promise as failed with an explication message.
     *
     * @throws Exception - Throws an execution if Vertx fail to stop this verticle. Check Vertx documentation for more information.
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        promise.tryFail(Logger.STOP_ANALYZING_PROJECT);
    }
}
