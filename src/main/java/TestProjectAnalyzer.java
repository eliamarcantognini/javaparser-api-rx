import lib.dto.DTOParser;
import lib.dto.DTOs;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lib.ProjectAnalyzer;
import lib.async.AsyncProjectAnalyzer;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;

public class TestProjectAnalyzer {

    private static final Vertx vertx = Vertx.vertx();

    private final static String INTERFACE = "";
    private final static String CLASS = "";
    private final static String PACKAGE = "";
    private final static String PROJECT = "";

    public static void main(String[] args) {
        ProjectAnalyzer projectAnalyzer;
        projectAnalyzer = new AsyncProjectAnalyzer(vertx);
        // Select which getter of project analyzer test
         testInterfaceReport(projectAnalyzer, INTERFACE);
         testClassReport(projectAnalyzer, CLASS);
         testPackageReport(projectAnalyzer, PACKAGE);
         testProjectReport(projectAnalyzer, PROJECT);
    }

    private static void testInterfaceReport(ProjectAnalyzer projectAnalyzer, final String interfaceToAnalyze) {
        vertx.eventBus().consumer("default", m -> System.out.println(m.body()));
        Future<InterfaceReport> future = projectAnalyzer.getInterfaceReport(interfaceToAnalyze);
        future.onSuccess(interfaceReport -> {
            var json = DTOParser.parseString(DTOs.createInterfaceDTO(interfaceReport));
//            view.setText(json);
            var obj = DTOParser.parseClassInterfaceDTO(json);
//            view.renderTree(obj);
        });
        futureOnFailureOnComplete(future);
    }

    private static void testClassReport(ProjectAnalyzer projectAnalyzer, final String classToAnalyze) {
        vertx.eventBus().consumer("default", m -> System.out.println(m.body()));
        Future<ClassReport> future = projectAnalyzer.getClassReport(classToAnalyze);
        future.onSuccess(classReport -> {
            var json = DTOParser.parseString(DTOs.createClassDTO(classReport));
//            view.setText(json);
            var obj = DTOParser.parseClassInterfaceDTO(json);
//            view.renderTree(obj);
        });
        futureOnFailureOnComplete(future);
    }

    private static void testPackageReport(ProjectAnalyzer projectAnalyzer, final String packageToAnalyze) {
        vertx.eventBus().consumer("default", m -> System.out.println(m.body()));
        Future<PackageReport> future = projectAnalyzer.getPackageReport(packageToAnalyze);
        future.onSuccess(packageReport -> {
            var json = DTOParser.parseString(DTOs.createPackageDTO(packageReport));
//            view.setText(json);
            var obj = DTOParser.parsePackageDTO(json);
//            view.renderTree(obj);
        });
        futureOnFailureOnComplete(future);
    }

    public static void testProjectReport(ProjectAnalyzer projectAnalyzer, final String projectToAnalyze) {
        vertx.eventBus().consumer("default", m -> System.out.println(m.body()));
        Future<ProjectReport> future = projectAnalyzer.getProjectReport(projectToAnalyze);
        future.onSuccess(projectReport -> {
            var json = DTOParser.parseString(DTOs.createProjectDTO(projectReport));
//            view.setText(json);
            var obj = DTOParser.parseProjectDTO(json);
//            view.renderTree(obj);
        });
        futureOnFailureOnComplete(future);
    }

    private static <T> void futureOnFailureOnComplete(Future<T> future) {
        future.onFailure(event -> {
            System.out.println("Future Failure:");
            System.out.println(event.toString());
            System.out.println();
        });
        future.onComplete(event -> {
            System.out.println("Future Complete:");
            System.out.println(event.toString());
            System.out.println();
        });
        System.out.println("Future Result Without \"on\" construct" + future.result());
    }

}
