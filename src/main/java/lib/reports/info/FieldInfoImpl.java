package lib.reports.info;

import lib.reports.info.interfaces.FieldInfo;
import lib.reports.interfaces.Report;

import java.util.Optional;

/**
 * Class that implements {@link FieldInfo}
 */
public class FieldInfoImpl implements FieldInfo {

    private final String fieldName;
    private final String fieldType;
    private final String fieldModifiers;
    private final Report parentClassReport;

    /**
     * Class constructor. Build info with data passed as parameters
     *
     * @param fieldName         field name
     * @param fieldType         field type
     * @param fieldModifiers    field modifiers
     * @param parentClassReport field parent class report
     * @see com.github.javaparser.ast.nodeTypes.NodeWithModifiers
     */
    public FieldInfoImpl(String fieldName, String fieldType, String fieldModifiers, Report parentClassReport) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.fieldModifiers = fieldModifiers;
        this.parentClassReport = parentClassReport;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getFieldType() {
        return fieldType;
    }

    @Override
    public Optional<String> getFieldModifiers() {
        return this.fieldModifiers != null ? Optional.of(this.fieldModifiers) : Optional.empty();
    }

    @Override
    public Report getParentClassReport() {
        return parentClassReport;
    }

    @Override
    public String toString() {
        return "\n\t\tFieldInfoImpl{"
                + "fieldName=" + this.getFieldName()
                + ", fieldType=" + this.getFieldType()
                + ", fieldModifiers=" + this.getFieldModifiers().orElse("")
                + ", parent=" + this.getParentClassReport().getName();
    }
}
