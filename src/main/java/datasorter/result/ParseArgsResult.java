package datasorter.result;

public record ParseArgsResult(
        String sortBy,
        String order,
        boolean isStatMode,
        String outputFormat,
        String outputPath
) {
}
