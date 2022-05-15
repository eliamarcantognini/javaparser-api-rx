package lib.reports.interfaces;

import lib.reports.info.interfaces.MethodInfo;

import java.util.List;

/**
 * Interface for interface report that extends from {@link Report}
 */
public interface InterfaceReport extends Report {

    /**
     * Get a list of methods inside the item (class/interface)
     *
     * @return a {@link List} of {@link MethodInfo}
     */
    List<MethodInfo> getMethodsInfo();

    /**
     * Add a {@link MethodInfo} to the report
     *
     * @param methodInfo the {@link MethodInfo}
     */
    void addMethodInfo(MethodInfo methodInfo);
}
