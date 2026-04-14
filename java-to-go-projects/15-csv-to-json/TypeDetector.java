package csv2json;

/**
 * Detects the likely data type of a string value.
 * Used to produce typed JSON output (numbers, booleans, null) instead of all-strings.
 */
public class TypeDetector {

    public enum DataType {
        INTEGER, FLOAT, BOOLEAN, NULL, STRING
    }

    /**
     * Detect the data type of a CSV field value.
     */
    public static DataType detect(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("null")
                || value.equalsIgnoreCase("na") || value.equalsIgnoreCase("n/a")) {
            return DataType.NULL;
        }

        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return DataType.BOOLEAN;
        }

        try {
            Integer.parseInt(value);
            return DataType.INTEGER;
        } catch (NumberFormatException ignored) {}

        try {
            Double.parseDouble(value);
            return DataType.FLOAT;
        } catch (NumberFormatException ignored) {}

        return DataType.STRING;
    }

    /**
     * Convert a string value to its typed Java object based on detected type.
     */
    public static Object convert(String value) {
        DataType type = detect(value);
        return switch (type) {
            case INTEGER -> Integer.parseInt(value);
            case FLOAT -> Double.parseDouble(value);
            case BOOLEAN -> Boolean.parseBoolean(value);
            case NULL -> null;
            case STRING -> value;
        };
    }
}
