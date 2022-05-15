package lib.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HexFormat;
import java.util.LinkedList;

/**
 * Static class which parse DTOs from/to byte[] and JSON
 */
public final class DTOParser {

    private DTOParser() {
    }

    private static final String HEX_INT_ERROR = "ffffffffffffffff";
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Create a {@link ProjectDTO} from JSON string
     *
     * @param string the JSON
     * @return a {@link ProjectDTO}
     */
    public static ProjectDTO parseProjectDTO(final String string) {
        try {
            return mapper.readValue(string, ProjectDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ProjectDTO(new ClassInterfaceDTO("", "", new LinkedList<>()), new LinkedList<>());
    }

    /**
     * Create a {@link ProjectDTO} from bytes
     *
     * @param bytes the array of bytes
     * @return a {@link ProjectDTO}
     */
    public static ProjectDTO parseProjectDTO(final byte[] bytes) {
        try {
            return mapper.readValue(bytes, ProjectDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ProjectDTO(new ClassInterfaceDTO("", "", new LinkedList<>()), new LinkedList<>());
    }

    /**
     * Create a {@link PackageDTO} from JSON string
     *
     * @param string the JSON
     * @return a {@link PackageDTO}
     */
    public static PackageDTO parsePackageDTO(final String string) {
        try {
            return mapper.readValue(string, PackageDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new PackageDTO("ParseError", "", new LinkedList<>(), new LinkedList<>());
    }

    /**
     * Create a {@link PackageDTO} from bytes
     *
     * @param bytes the array of bytes
     * @return a {@link PackageDTO}
     */
    public static PackageDTO parsePackageDTO(final byte[] bytes) {
        try {
            return mapper.readValue(bytes, PackageDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new PackageDTO("ParseError", "", new LinkedList<>(), new LinkedList<>());
    }

    /**
     * Create a {@link ClassInterfaceDTO} from JSON string
     *
     * @param string the JSON
     * @return a {@link ClassInterfaceDTO}
     */
    public static ClassInterfaceDTO parseClassInterfaceDTO(final String string) {
        try {
            return mapper.readValue(string, ClassInterfaceDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ClassInterfaceDTO("ParseError", "", new LinkedList<>(), new LinkedList<>());
    }

    /**
     * Create a {@link ClassInterfaceDTO} from bytes
     *
     * @param bytes the array of bytes
     * @return a {@link ClassInterfaceDTO}
     */
    public static ClassInterfaceDTO parseClassInterfaceDTO(final byte[] bytes) {
        try {
            return mapper.readValue(bytes, ClassInterfaceDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ClassInterfaceDTO("ParseError", "", new LinkedList<>(), new LinkedList<>());
    }

    /**
     * Create a {@link MethodDTO} from JSON string
     *
     * @param string the JSON
     * @return a {@link MethodDTO}
     */
    public static MethodDTO parseMethodDTO(final String string) {
        try {
            return mapper.readValue(string, MethodDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new MethodDTO("ParseError", -1, -1);
    }

    /**
     * Create a {@link MethodDTO} from bytes
     *
     * @param bytes the array of bytes
     * @return a {@link MethodDTO}
     */
    public static MethodDTO parseMethodDTO(final byte[] bytes) {
        try {
            return mapper.readValue(bytes, MethodDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new MethodDTO("ParseError", -1, -1);
    }

    /**
     * Create a {@link FieldDTO} from JSON string
     *
     * @param string the JSON
     * @return a {@link FieldDTO}
     */
    public static FieldDTO parseFieldDTO(final String string) {
        try {
            return mapper.readValue(string, FieldDTO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new FieldDTO("ParseError", "", "");
    }

    /**
     * Create a {@link FieldDTO} from bytes
     *
     * @param bytes the array of bytes
     * @return a {@link FieldDTO}
     */
    public static FieldDTO parseFieldDTO(final byte[] bytes) {
        try {
            return mapper.readValue(bytes, FieldDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FieldDTO("ParseError", "", "");
    }

    /**
     * Create a JSON String which represents the object passed
     *
     * @param dto the dto to be parsed
     * @return a JSON String
     */
    public static String parseString(final Object dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Create a formatted JSON String which represents the object passed
     *
     * @param dto the dto to be parsed
     * @return a formatted JSON String
     */
    public static String parseStringToPrettyJSON(final Object dto) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Create an array of bytes which represents the object passed
     *
     * @param dto the dto to be parsed
     * @return an array of bytes
     */
    public static byte[] parseBytes(final Object dto) {
        try {
            return mapper.writeValueAsBytes(dto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return HexFormat.of().parseHex(HEX_INT_ERROR);
    }

}
