package lib.dto;

/**
 * Record representing a serializable version of a {@link lib.reports.info.interfaces.FieldInfo}.
 *
 * @param name      - name of the field
 * @param type      - the type of the field
 * @param modifiers - the modifiers of the filed, e.g. public, static, final...
 */
public record FieldDTO(String name, String type, String modifiers) {
}