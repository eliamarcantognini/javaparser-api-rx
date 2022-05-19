package lib;

import lib.dto.DTOParser;
import lib.dto.DTOs;
import lib.reports.info.interfaces.FieldInfo;
import lib.reports.info.interfaces.MethodInfo;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;

/**
 * Functional Interface that represents the Logger of the library. The target method is {@link #log(String)} which is used to define how to log a String.
 * The other default methods used the target one to log the classes that the library construct during a specific analysis.
 */
@FunctionalInterface
public interface Logger {

    /**
     * Message to sent to stop project analysis
     */
    String STOP_ANALYZING_PROJECT = ">>STOP<<";

    /**
     * Target method of the Functional Interface. It must be implemented to specify how to log a String, which
     * represents the message that has to be logged.
     *
     * @param message - the message to log
     */
    void log(String message);

    /**
     * Log a json representing a {@link MethodInfo}. It uses {@link DTOs} and {@link DTOParser} to create the json
     * from the param. Before the json log also an identifier represented by a value of the {@link CodeElementFound}
     * to make understandable what type of object is logged.
     *
     * @param method - the method to log
     */
    default void log(MethodInfo method) {
        log(createMessage(CodeElementFound.METHOD.getCode(), DTOParser.parseString(DTOs.createMethodDTO(method))));
    }

    /**
     * Log a json representing a {@link FieldInfo}. It uses {@link DTOs} and {@link DTOParser} to create the json
     * from the param. Before the json log also an identifier represented by a value of the {@link CodeElementFound}
     * to make understandable what type of object is logged.
     *
     * @param field - the field to log
     */
    default void log(FieldInfo field) {
        log(createMessage(CodeElementFound.FIELD.getCode(), DTOParser.parseString(DTOs.createFieldDTO(field))));
    }

    /**
     * Log a json representing a {@link InterfaceReport}. It uses {@link DTOs} and {@link DTOParser} to create the json
     * from the param. Before the json log also an identifier represented by a value of the {@link CodeElementFound}
     * to make understandable what type of object is logged.
     *
     * @param interfaceReport - the interface report to log
     */
    default void log(InterfaceReport interfaceReport) {
        log(createMessage(CodeElementFound.INTERFACE.getCode(), DTOParser.parseString(DTOs.createInterfaceDTO(interfaceReport))));
    }

    /**
     * Log a json representing a {@link ClassReport}. It uses {@link DTOs} and {@link DTOParser} to create the json
     * from the param. Before the json log also an identifier represented by a value of the {@link CodeElementFound}
     * to make understandable what type of object is logged.
     *
     * @param classReport - the class report to log
     */
    default void log(ClassReport classReport) {
        log(createMessage(CodeElementFound.CLASS.getCode(), DTOParser.parseString(DTOs.createClassDTO(classReport))));
    }

    /**
     * Log a json representing a {@link PackageReport}. It uses {@link DTOs} and {@link DTOParser} to create the json
     * from the param. Before the json log also an identifier represented by a value of the {@link CodeElementFound}
     * to make understandable what type of object is logged.
     *
     * @param packageReport - the package report to log
     */
    default void log(PackageReport packageReport) {
        log(createMessage(CodeElementFound.PACKAGE.getCode(), DTOParser.parseString(DTOs.createPackageDTO(packageReport))));
    }

    /**
     * Log a json representing a {@link ProjectReport}. It uses {@link DTOs} and {@link DTOParser} to create the json
     * from the param. Before the json log also an identifier represented by a value of the {@link CodeElementFound}
     * to make understandable what type of object is logged.
     *
     * @param projectReport - the project report to log
     */
    default void log(ProjectReport projectReport) {
        log(createMessage(CodeElementFound.PROJECT.getCode(), DTOParser.parseString(DTOs.createProjectDTO(projectReport))));
    }

    /**
     * Log an error message.
     *
     * @param errorMessage - the message to log
     */
    default void logError(String errorMessage) {
        log(createMessage(CodeElementFound.ERROR.getCode(), errorMessage));
    }

    /**
     * Log an user interrupt message.
     *
     * @param interruptMessage - the message to log
     */
    default void logInterrupt(String interruptMessage) {
        log(createMessage(CodeElementFound.INTERRUPT.getCode(), interruptMessage));
    }

    private String createMessage(String id, String json) {
        return id + json;
    }

    /**
     * Enum that represents the identifiers used to understand which object is logged.
     */
    enum CodeElementFound{
        /**
         * Method found code.
         */
        METHOD("METHOD_REPORT:"),
        /**
         * Field found code.
         */
        FIELD("FIELD_REPORT:"),
        /**
         * Class found code.
         */
        CLASS("CLASS_REPORT:"),
        /**
         * Interface found code.
         */
        INTERFACE("INTERFACE_REPORT:"),
        /**
         * Package found code.
         */
        PACKAGE("PACKAGE_REPORT:"),
        /**
         * Project found code.
         */
        PROJECT("PROJECT_REPORT:"),
        /**
         * Error code.
         */
        ERROR("ERROR:"),
        /**
         * Stop interrupt code.
         */
        INTERRUPT("INTERRUPT:");

        private final String code;

        CodeElementFound (final String code){
            this.code = code;
        }

        /**
         * Get code of element found as string
         *
         * @return code as string
         */
        public String getCode(){
            return this.code;
        }
    }

}
