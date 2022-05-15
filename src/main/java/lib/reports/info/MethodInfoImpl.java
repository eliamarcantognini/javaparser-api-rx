package lib.reports.info;

import lib.reports.info.interfaces.MethodInfo;
import lib.reports.interfaces.Report;

import java.util.Optional;

/**
 * Class that implements {@link MethodInfo}
 */
public class MethodInfoImpl implements MethodInfo {

    final String methodName;
    final int srcBeginLine;
    final int endBeginLine;
    final String modifiers;
    final Report parentClassOrInterfaceReport;

    /**
     * Class constructor. Build info with data passed as parameters
     *
     * @param methodName                   method name
     * @param srcBeginLine                 line where method begins
     * @param endBeginLine                 line where methd ends
     * @param modifiers                    method modifiers
     * @param parentClassOrInterfaceReport method parent class or interface
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     */
    public MethodInfoImpl(String methodName, int srcBeginLine, int endBeginLine, String modifiers, Report parentClassOrInterfaceReport) {
        this.methodName = methodName;
        this.srcBeginLine = srcBeginLine;
        this.endBeginLine = endBeginLine;
        this.modifiers = modifiers;
        this.parentClassOrInterfaceReport = parentClassOrInterfaceReport;
    }

    @Override
    public String getName() {
        return this.methodName;
    }

    @Override
    public int getSrcBeginLine() {
        return this.srcBeginLine;
    }

    @Override
    public int getEndBeginLine() {
        return this.endBeginLine;
    }

    @Override
    public Optional<String> getModifiers() {
        return this.modifiers == null ? Optional.empty() : Optional.of(this.modifiers);
    }

    @Override
    public Report getParentClassOrInterfaceReport() {
        return this.parentClassOrInterfaceReport;
    }

    @Override
    public String toString() {
        return "\n\t\tMethodInfoImpl{"
                + "methodName=" + methodName
                + ", srcBeginLine=" + srcBeginLine
                + ", endBeginLine=" + endBeginLine
                + (this.getModifiers().isPresent() ? ", modifiers=" + modifiers : "")
                + ", parent=" + parentClassOrInterfaceReport.getName();
    }
}
