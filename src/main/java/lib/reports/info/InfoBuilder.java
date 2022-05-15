package lib.reports.info;

import lib.reports.info.interfaces.FieldInfo;
import lib.reports.info.interfaces.MethodInfo;
import lib.reports.interfaces.Report;

/**
 * Builder Pattern used to create {@link MethodInfo} and {@link FieldInfo}
 */
public class InfoBuilder {

    private Report report;
    private String name;
    private int beginLine;
    private int endLine;
    private String modifiers;
    private String type;

    /**
     * Set the full parent report, {@link lib.reports.interfaces.ClassReport} or {@link lib.reports.interfaces.InterfaceReport}
     *
     * @param report the report
     * @return the builder
     */
    public InfoBuilder report(final Report report) {
        this.report = report;
        return this;
    }

    /**
     * Set the name of the Info
     *
     * @param name the name
     * @return the builder
     */
    public InfoBuilder name(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Set the beginning line of the Info
     *
     * @param line the beginning line
     * @return the builder
     */
    public InfoBuilder beginLine(final int line) {
        this.beginLine = line;
        return this;
    }

    /**
     * Set the end line of the Info
     *
     * @param line the end line
     * @return the builder
     */
    public InfoBuilder endLine(final int line) {
        this.endLine = line;
        return this;
    }

    /**
     * Set the modifiers of the Info
     *
     * @param modifiers the modifiers
     * @return the builder
     */
    public InfoBuilder modifiers(final String modifiers) {
        this.modifiers = modifiers;
        return this;
    }

    /**
     * Set the type of the Info
     *
     * @param type the type
     * @return the builder
     */
    public InfoBuilder type(final String type) {
        this.type = type;
        return this;
    }

    /**
     * Create a {@link MethodInfoImpl}
     *
     * @return a {@link MethodInfo}
     */
    public MethodInfo buildMethod() {
        return new MethodInfoImpl(this.name, this.beginLine, this.endLine,
                this.modifiers, this.report);
    }

    /**
     * Create a {@link FieldInfoImpl}
     *
     * @return a {@link FieldInfo}
     */
    public FieldInfo buildField() {
        return new FieldInfoImpl(this.name, this.type,
                this.modifiers, this.report);
    }

}
