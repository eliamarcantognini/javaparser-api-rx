package view.GUI;

import lib.dto.*;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Static class to manage and get {@link javax.swing.tree.DefaultMutableTreeNode} from DTO report
 *
 * @see ClassInterfaceDTO
 * @see PackageDTO
 * @see ProjectDTO
 */
public final class Trees {

    private final static int CHARACTERS_TO_SUBSTRING_IN_METHODS = 10;
    private final static int CHARACTERS_TO_SUBSTRING_IN_METHODS_MODS_PRESENTS = 17;
    private final static int CHARACTERS_TO_SUBSTRING_IN_FIELDS = 9;

    private Trees() {
    }

    /**
     * Create and returns {@link DefaultMutableTreeNode} node with all the nested information about a class or an interface
     *
     * @param dto the Class or Interface DTO
     * @return a {@link DefaultMutableTreeNode} with all the data of the Class or the Interface
     */
    public static DefaultMutableTreeNode createClassOrInterfaceTreeNode(ClassInterfaceDTO dto) {
        var root = new DefaultMutableTreeNode(dto.name());
        root.add(new DefaultMutableTreeNode("path: " + dto.path()));
        var methodsNode = new DefaultMutableTreeNode(dto.methods().isEmpty() ? "no methods" : "methods");
        for (MethodDTO m : dto.methods()) {
            if (m.modifiers() != null)
                methodsNode.add(new DefaultMutableTreeNode(m.toString().substring(CHARACTERS_TO_SUBSTRING_IN_METHODS, m.toString().length() - 1)));
            else
                methodsNode.add(new DefaultMutableTreeNode(m.toString().substring(CHARACTERS_TO_SUBSTRING_IN_METHODS, m.toString().length() - CHARACTERS_TO_SUBSTRING_IN_METHODS_MODS_PRESENTS)));
        }
        root.add(methodsNode);
        if (dto.fields() != null) {
            var fieldsNode = new DefaultMutableTreeNode(dto.fields().isEmpty() ? "no fields" : "fields");
            for (FieldDTO f : dto.fields()) {
                fieldsNode.add(new DefaultMutableTreeNode(f.toString().substring(CHARACTERS_TO_SUBSTRING_IN_FIELDS, f.toString().length() - 1)));
            }
            root.add(fieldsNode);
        }
        return root;
    }

    /**
     * Create and returns {@link DefaultMutableTreeNode} node with all the nested information about the package
     *
     * @param dto the Package DTO
     * @return a {@link DefaultMutableTreeNode} with all the data of the Package
     */
    public static DefaultMutableTreeNode createPackageTreeNode(PackageDTO dto) {
        var root = new DefaultMutableTreeNode(dto.name());
        root.add(new DefaultMutableTreeNode("path: " + dto.path()));
        var interfaces = new DefaultMutableTreeNode(dto.interfaces().isEmpty() ? "no interfaces" : "interfaces");
        for (ClassInterfaceDTO c : dto.interfaces())
            interfaces.add(createClassOrInterfaceTreeNode(c));
        root.add(interfaces);
        var classes = new DefaultMutableTreeNode(dto.classes().isEmpty() ? "no classes" : "classes");
        for (ClassInterfaceDTO c : dto.classes())
            classes.add(createClassOrInterfaceTreeNode(c));
        root.add(classes);

        return root;
    }

    /**
     * Create and returns {@link DefaultMutableTreeNode} node with all the nested information about the project
     *
     * @param dto the Project DTO
     * @return a {@link DefaultMutableTreeNode} with all the data of the Project
     */
    public static DefaultMutableTreeNode createProjectTreeNode(ProjectDTO dto) {
        var root = new DefaultMutableTreeNode("Project");
        var mainClass = new DefaultMutableTreeNode(dto.mainClass().name().isBlank() ? "no Main Class" : "Main class");
        if (!dto.mainClass().name().isBlank()) mainClass.add(createClassOrInterfaceTreeNode(dto.mainClass()));
        root.add(mainClass);
        var packages = new DefaultMutableTreeNode(dto.packages().isEmpty() ? "no packages" : "packages");
        for (PackageDTO p : dto.packages())
            packages.add(createPackageTreeNode(p));
        root.add(packages);
        return root;
    }
}
