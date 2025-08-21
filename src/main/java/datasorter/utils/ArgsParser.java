package datasorter.utils;

import datasorter.result.ParseArgsResult;

public class ArgsParser {
    private static final String INVALID_SORT_OPTION_MESSAGE = "Invalid sort option";
    private static final String INVALID_ORDER_OPTION_MESSAGE = "Invalid order option";
    private static final String INVALID_OUTPUT_FORMAT_MESSAGE = "Invalid output format";
    private static final String INVALID_OUTPUT_PATH_MESSAGE = "Invalid output path";

    public ParseArgsResult parseArgs(String[] args) {
        String sortBy = null;
        String order = null;
        boolean isStatMode = false;
        String outputFormat = "console";
        String outputPath = null;
        for (String arg : args) {
            if (arg.startsWith("--sort") || arg.startsWith("-s")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2) {
                    String value = parts[1].toLowerCase();
                    if (value.equals("name") || value.equals("salary")) {
                        sortBy = value;
                    } else {
                        throw new IllegalArgumentException(INVALID_SORT_OPTION_MESSAGE);
                    }
                }
            } else if (arg.startsWith("--order=")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2) {
                    String value = parts[1].toLowerCase();
                    if (value.equals("asc") || value.equals("desc")) {
                        order = value;
                    } else {
                        throw new IllegalArgumentException(INVALID_ORDER_OPTION_MESSAGE);
                    }
                }
            } else if (arg.equals("--stat")) {
                isStatMode = true;
            } else if (arg.startsWith("--output=") || arg.startsWith("-o=")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2) {
                    String value = parts[1].toLowerCase();
                    if (value.equals("console") || value.equals("file")) {
                        outputFormat = value;
                    } else {
                        throw new IllegalArgumentException(INVALID_OUTPUT_FORMAT_MESSAGE);
                    }
                }
            } else if (arg.startsWith("--path=")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2 && !parts[1].isEmpty()) {
                    outputPath = parts[1];
                } else {
                    throw new IllegalArgumentException(INVALID_OUTPUT_PATH_MESSAGE);
                }
            }
        }
        return new ParseArgsResult(sortBy, order, isStatMode, outputFormat, outputPath);
    }
}
