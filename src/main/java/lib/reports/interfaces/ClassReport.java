package lib.reports.interfaces;

import lib.reports.info.interfaces.FieldInfo;

import java.util.List;

/**
 * Interface for class report that extends from {@link InterfaceReport}
 */
public interface ClassReport extends InterfaceReport {

    /**
     * Get a list of fields inside the class
     *
     * @return a {@link List} of {@link FieldInfo}
     */
    List<FieldInfo> getFieldsInfo();

    /**
     * Add a {@link FieldInfo} to the report
     *
     * @param fieldInfo the {@link FieldInfo}
     */
    void addFieldInfo(FieldInfo fieldInfo);
}
