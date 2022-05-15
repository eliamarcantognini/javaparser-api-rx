import lib.ProjectAnalyzer;
import lib.reports.interfaces.InterfaceReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class ProjectAnalyzerTest {

    ProjectAnalyzer projectAnalyzer;

    @BeforeEach
    void initProjectAnalyzer() {
        projectAnalyzer = null;
    }

    @Test
    void testGetInterfaceReportNotNull() {
        Future<InterfaceReport> future = projectAnalyzer.getInterfaceReport("src/test/java/InterfaceForTest.java");
        assertNotNull(future);
    }

    @Test
    void testGetInterfaceReport() {

    }
}
