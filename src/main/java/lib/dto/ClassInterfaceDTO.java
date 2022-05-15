package lib.dto;

import java.util.LinkedList;
import java.util.List;

/**
 * Record representing a serializable version of a {@link lib.reports.interfaces.ClassReport}.
 *
 * @param name    - name of the class
 * @param path    - path of the class
 * @param methods - {@link List} that contains tne class' methods
 * @param fields  - {@link List} that contains tne class' fields
 */
public record ClassInterfaceDTO(String name, String path, List<MethodDTO> methods, List<FieldDTO> fields) {

    /**
     * Different constructor for tne record used to create a serializable version of a {@link lib.reports.interfaces.InterfaceReport}.
     * It use the constructor for the class record they have almost the same elements, but it does not take a param representing the fields,
     * because Interface cannot have them.
     *
     * @param name    - name of the interface
     * @param path    - path of the interface
     * @param methods - {@link List} that contains tne interface's methods
     */
    public ClassInterfaceDTO(String name, String path, List<MethodDTO> methods) {
        this(name, path, methods, new LinkedList<>());
    }
}
