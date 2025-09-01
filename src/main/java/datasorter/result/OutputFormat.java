package datasorter.result;

public enum OutputFormat {
    CONSOLE("console"),
    FILE("file");
    private final String value;
    private static final String UNKNOWN_OUTPUT_FORMAT_MESSAGE = "Unknown output format";

    OutputFormat(String value) {
        this.value = value;
    }

    public static OutputFormat fromString(String string) {
        for (OutputFormat outputFormat : OutputFormat.values()) {
            if (outputFormat.value.equalsIgnoreCase(string)) {
                return outputFormat;
            }
        }
        throw new IllegalArgumentException(UNKNOWN_OUTPUT_FORMAT_MESSAGE);
    }
}
