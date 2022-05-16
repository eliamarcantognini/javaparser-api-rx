import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import lib.ProjectAnalyzer;
import lib.rx.ReactiveProjectAnalyzer;

import java.util.concurrent.Future;

public class TestProjectAnalyzer {


    private final static String INTERFACE = "src/main/java/lib/reports/interfaces/ClassReport.java";
    private final static String CLASS = "src/main/java/controller/AnalysisController.java";
    private final static String PACKAGE = "src/main/java/lib/reports/";
    private final static String PROJECT = "src/main/java/";

    public static void main(String[] args) {
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("OBS SUB");
            }

            @Override
            public void onNext(@NonNull String s) {
                System.out.println("OBS:" + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println(e.toString());
            }

            @Override
            public void onComplete() {

            }
        };
        var projectAnalyzer = new ReactiveProjectAnalyzer(observer);

//        testClassReport(projectAnalyzer, CLASS);
//        testInterfaceReport(projectAnalyzer, INTERFACE);
//        testPackageReport(projectAnalyzer, PACKAGE);
        testProjectReport(projectAnalyzer, PROJECT);
//        var ps = projectAnalyzer.analyzeProject(PACKAGE, "default");
//        ps.subscribe(System.out::println);

    }

    private static void testInterfaceReport(ProjectAnalyzer projectAnalyzer, final String interfaceToAnalyze) {
        projectAnalyzer.getInterfaceReport(interfaceToAnalyze).subscribe(e -> System.out.println(e.toString()));
    }

    private static void testClassReport(ProjectAnalyzer projectAnalyzer, final String classToAnalyze) {
        projectAnalyzer.getClassReport(classToAnalyze).subscribe(e -> System.out.println(e.toString()));
    }

    private static void testPackageReport(ProjectAnalyzer projectAnalyzer, final String packageToAnalyze) {
        projectAnalyzer.getPackageReport(packageToAnalyze).subscribe(e -> System.out.println(e.toString()));
    }

    public static void testProjectReport(ProjectAnalyzer projectAnalyzer, final String projectToAnalyze) {
        projectAnalyzer.getProjectReport(projectToAnalyze).subscribe(o -> System.out.println("\n\nSSSS: " + o.toString() + "\n"));
    }

    private static <T> void futureOnFailureOnComplete(Future<T> future) {
    }

}
