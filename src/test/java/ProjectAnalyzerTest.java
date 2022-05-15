import io.vertx.core.Future;
import io.vertx.core.Vertx;
import lib.async.AsyncProjectAnalyzer;
import lib.ProjectAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lib.reports.interfaces.InterfaceReport;

import static org.junit.jupiter.api.Assertions.*;

public class ProjectAnalyzerTest {

    ProjectAnalyzer projectAnalyzer;

    @BeforeEach
    void initProjectAnalyzer(){
        projectAnalyzer = new AsyncProjectAnalyzer(Vertx.vertx());
    }

    @Test
    void testGetInterfaceReportNotNull(){
        Future<InterfaceReport> future = projectAnalyzer.getInterfaceReport("src/test/java/InterfaceForTest.java");
        assertNotNull(future);
    }

    @Test
    void testGetInterfaceReport(){
        Future<InterfaceReport> future = projectAnalyzer.getInterfaceReport("src/test/java/InterfaceForTest.java");
//        future.result().interfaceName();
//        future.result()
        future.onSuccess(event -> {
            System.out.println("kebab1");
//            assertEquals("InterfaceForTest", event.getInterfaceName());
        });
        future.onFailure(event -> {
            System.out.println("kebab2");
            fail();
        });
        future.onComplete(event -> {
            System.out.println("hamburger");
        });
        System.out.println(future.result());
    }
}
