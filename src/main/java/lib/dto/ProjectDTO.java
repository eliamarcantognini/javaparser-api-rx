package lib.dto;

import java.util.List;

/**
 * Record representing a serializable version of a {@link lib.reports.interfaces.ProjectReport}.
 *
 * @param mainClass - the entry point of the project
 * @param packages  - {@link List} of {@link PackageDTO}, which are the packages inside the project
 */
public record ProjectDTO(ClassInterfaceDTO mainClass, List<PackageDTO> packages) {

    /**
     * Method to retrieve a Class of the project by the name.
     *
     * @param name - name of the class to retrieve
     * @return the {@link ClassInterfaceDTO} representing the class
     */
    ClassInterfaceDTO getClass(String name) {
        final ClassInterfaceDTO[] classDTO = new ClassInterfaceDTO[1];
        classDTO[0] = null;
        this.packages.forEach(p -> p.classes().forEach(c -> {
            if (c.name().equals(name)) classDTO[0] = c;
        }));
        return classDTO[0];
    }
}
