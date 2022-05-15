package lib.reports.interfaces;

/**
 * Interface for report
 */
public interface Report {

    /**
     * Get the name of the item (class/interface)
     *
     * @return the name of the item
     */
    String getName();

    /**
     * Set the name of the item (class/interface)
     *
     * @param name the name
     */
    void setName(String name);

    /**
     * Get the path from source root
     *
     * @return the path from source root
     */
    String getSourceFullPath();

    /**
     * Set the path from source root
     *
     * @param interfaceFullPath the path from source root
     */
    void setFullPath(String interfaceFullPath);
}
