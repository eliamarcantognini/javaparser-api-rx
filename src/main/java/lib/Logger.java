package lib;

import utils.dto.DTOParser;
import utils.dto.DTOs;
import lib.reports.info.interfaces.FieldInfo;
import lib.reports.info.interfaces.MethodInfo;
import lib.reports.interfaces.ClassReport;
import lib.reports.interfaces.InterfaceReport;
import lib.reports.interfaces.PackageReport;
import lib.reports.interfaces.ProjectReport;

@FunctionalInterface
public interface Logger {

    void log(String message);

    default void log(MethodInfo method) {
        log(createMessage("METHOD", DTOParser.parseString(DTOs.createMethodDTO(method))));
//        log(DTOParser.parseString(DTOs.createMethodDTO(method)));
    }

    default void log(FieldInfo field) {
//        log(DTOParser.parseString(DTOs.createFieldDTO(field)));
        log(createMessage("FIELD", DTOParser.parseString(DTOs.createFieldDTO(field))));
    }

    default void log(InterfaceReport interfaceReport) {
//        log(DTOParser.parseString(DTOs.createInterfaceDTO(interfaceReport)));
        log(createMessage("INTERFACE", DTOParser.parseString(DTOs.createInterfaceDTO(interfaceReport))));
    }

    default void log(ClassReport classReport) {
//        log(DTOParser.parseString(DTOs.createClassDTO(classReport)));
        log(createMessage("CLASS", DTOParser.parseString(DTOs.createClassDTO(classReport))));
    }


    default void log(PackageReport packageReport) {
//        log(DTOParser.parseString(DTOs.createPackageDTO(packageReport)));
        log(createMessage("PACKAGE", DTOParser.parseString(DTOs.createPackageDTO(packageReport))));
    }

    default void log(ProjectReport projectReport) {
        log(createMessage("PROJECT", DTOParser.parseString(DTOs.createProjectDTO(projectReport))));
    }

    private String createMessage(String id, String json) {
        return id + " : " + json;
    }

}
