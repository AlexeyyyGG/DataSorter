package datasorter.utils;

import datasorter.result.Arguments;
import datasorter.result.Order;
import datasorter.result.OutputFormat;
import datasorter.result.SortBy;

public class ArgsParser {
    private static final String INVALID_SORT_OPTION_MESSAGE = "Invalid sort option";
    private static final String INVALID_ORDER_OPTION_MESSAGE = "Invalid order option";
    private static final String INVALID_OUTPUT_FORMAT_MESSAGE = "Invalid output format";
    private static final String INVALID_OUTPUT_PATH_MESSAGE = "Invalid output path";
    private static final String MISSING_SORT_FOR_ORDER_MESSAGE =
            "The parameter --order is specified, but the sorting parameter --sort is missing";
    private static final String MISSING_OUTPUT_PATH = "Missing output path";

    public static Arguments parseArgs(String[] args) {
        SortBy sortBy = null;
        Order order = null;
        boolean isStatMode = false;
        OutputFormat outputFormat = OutputFormat.CONSOLE;
        String outputPath = null;
        for (String arg : args) {
            String key = arg.split("=")[0];
            switch (key) {
                case "--sort":
                case "-s":
                    sortBy = parseSort(arg);
                    break;
                case "--order":
                    order = parseOrder(arg);
                    break;
                case "--stat":
                    isStatMode = true;
                    break;
                case "--output":
                case "-o":
                    outputFormat = parseOutputFormat(arg);
                    break;
                case "--path":
                    outputPath = parseOutputPath(arg);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid argument: " + arg);
            }
        }
        if (order != null && sortBy == null) {
            throw new IllegalArgumentException(MISSING_SORT_FOR_ORDER_MESSAGE);
        }
        if (outputFormat == OutputFormat.FILE && (outputPath == null || outputPath.isEmpty())) {
            throw new IllegalArgumentException(MISSING_OUTPUT_PATH);
        }
        return new Arguments(
                sortBy,
                order,
                isStatMode,
                outputFormat,
                outputPath
        );
    }

    private static SortBy parseSort(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException(INVALID_SORT_OPTION_MESSAGE);
        }
        return SortBy.fromString(parts[1]);
    }

    private static Order parseOrder(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException(INVALID_ORDER_OPTION_MESSAGE);
        }
        return Order.fromString(parts[1]);
    }

    private static OutputFormat parseOutputFormat(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException(INVALID_OUTPUT_FORMAT_MESSAGE);
        }
        return OutputFormat.fromString(parts[1]);
    }

    private static String parseOutputPath(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length != 2 || parts[1].isEmpty()) {
            throw new IllegalArgumentException(INVALID_OUTPUT_PATH_MESSAGE);
        }
        return parts[1];
    }
}