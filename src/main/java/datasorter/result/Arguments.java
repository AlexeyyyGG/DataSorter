package datasorter.result;

public record Arguments(
        SortBy sortBy,
        Order order,
        boolean isStatMode,
        OutputFormat outputFormat,
        String outputPath
) {
    public static Arguments defaultArguments() {
        return new Arguments(
                null,
                null,
                false,
                OutputFormat.CONSOLE,
                null
        );
    }
}
