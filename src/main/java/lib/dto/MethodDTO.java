package lib.dto;

/**
 * Record representing a serializable version of a {@link lib.reports.info.interfaces.MethodInfo}.
 *
 * @param name         - name of the method
 * @param srcBeginLine - the number of the first line of the method inside the file
 * @param endBeginLine - the number of the last line of the method inside the file
 * @param modifiers    - the modifiers of the method, e.g. public, static, final...
 */
public record MethodDTO(String name, int srcBeginLine, int endBeginLine, String modifiers) {
    public MethodDTO(String name, int srcBeginLine, int endBeginLine) {
        this(name, srcBeginLine, endBeginLine, "");
    }
}
