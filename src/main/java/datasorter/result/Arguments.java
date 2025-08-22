package datasorter.result;

public record Arguments(
        SortBy sortBy,
        Order order,
        boolean isStatMode,
        OutputFormat outputFormat,
        String outputPath
) {
}
