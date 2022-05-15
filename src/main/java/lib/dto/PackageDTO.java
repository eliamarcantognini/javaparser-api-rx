package lib.dto;

import java.util.List;

/**
 * Record representing a serializable version of a {@link lib.reports.interfaces.PackageReport}.
 *
 * @param name       - name of the package
 * @param path       - path of the message
 * @param classes    - {@link List} of {@link ClassInterfaceDTO}, which are the classes of the package
 * @param interfaces - {@link List} of {@link ClassInterfaceDTO}, which are the interfaces of the package
 */
public record PackageDTO(String name, String path, List<ClassInterfaceDTO> classes,
                         List<ClassInterfaceDTO> interfaces) {
}
