package lib.async;

import com.github.javaparser.ast.CompilationUnit;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import lib.Logger;
import lib.reports.PackageReportImpl;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * Class that manage package analysis. It extends from {@link AbstractVerticle} and when the verticle
 * is deployed start the analysis with parameter passed in constructor.
 *
 * @see io.vertx.core.AbstractVerticle
 */
public class PackageVerticle extends AbstractVerticle {

    private final Promise<PackageReport> promise;
    private final PackageReport packageReport;
    private final AsyncProjectAnalyzer analyzer;
    private final String srcPackagePath;
    private final Logger logger;

    /**
     * Class constructor
     *
     * @param analyzer       analyzer to use
     * @param promise        promise where {@link PackageReport} where will be present
     * @param srcPackagePath path to package to analyze
     * @param logger         logger used to send message during analysis
     */
    public PackageVerticle(AsyncProjectAnalyzer analyzer, Promise<PackageReport> promise, String srcPackagePath, Logger logger) {
        this.analyzer = analyzer;
        this.promise = promise;
        this.srcPackagePath = srcPackagePath;
        this.logger = logger;
        this.packageReport = new PackageReportImpl();
    }

    /**
     * Start execution of the verticle. Analyze the package found at the path set in the constructor and complete the Future also
     * passed in the Constructor with the results of the analysis as a {@link PackageReport}.
     */
    @Override
    public void start() {
        AtomicBoolean set = new AtomicBoolean(false);
        final List<Future<ClassReport>> classReports = new ArrayList<>();
        final List<Future<InterfaceReport>> interfaceReports = new ArrayList<>();

        File folder = new File(srcPackagePath);
        List<String> list = Stream.of(Objects.requireNonNull(
                        folder.listFiles((dir, name) -> name.endsWith(".java"))))
                .map(File::getPath)
                .toList();
        list.forEach(path -> {
            CompilationUnit cu;
            try {
                cu = analyzer.getCompilationUnit(path);
                packageReport.setName("");
                packageReport.setFullPath("");
                if (cu.getType(0).asClassOrInterfaceDeclaration().isInterface()) {
                    Future<InterfaceReport> f = analyzer.getInterfaceReport(path);
                    var futureCompose = f.compose(report -> {
                        packageReport.addInterfaceReport(report);
                        setPackageNameAndPath(packageReport, set, report.getName(), report.getSourceFullPath());
                        return Future.succeededFuture(report);
                    });
                    interfaceReports.add(futureCompose);
                } else {
                    Future<ClassReport> f = analyzer.getClassReport(path);
                    var futureCompose = f.compose(report -> {
                        packageReport.addClassReport(report);
                        setPackageNameAndPath(packageReport, set, report.getName(), report.getSourceFullPath());
                        return Future.succeededFuture(report);
                    });
                    classReports.add(futureCompose);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });

        var classReportsFuture = MyCompositeFuture.join(classReports);
        var interfaceReportsFuture = MyCompositeFuture.join(interfaceReports);
        CompositeFuture.all(classReportsFuture, interfaceReportsFuture).onSuccess(r -> {
            if (promise.tryComplete(packageReport)) {
                logger.log(packageReport);
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

    private void setPackageNameAndPath(PackageReport packageReport, AtomicBoolean set, String name, String sourceFullPath) {
        if (!set.get()) {
            var s = sourceFullPath.split("\\.");
            packageReport.setName(s.length == 1 ? "." : (s[s.length - 2]));
            packageReport.setFullPath(s.length == 1 ? "" : sourceFullPath.substring(0, sourceFullPath.length() - name.length() - 1));
            set.set(true);
        }
    }

}
