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

    public static Arguments parseArgs(String[] args) {
        String sortByStr = null;
        String orderStr = null;
        boolean isStatMode = false;
        String outputFormatStr = "console";
        String outputPath = null;
        for (String arg : args) {
            if (arg.startsWith("--sort") || arg.startsWith("-s")) {
                sortByStr = parseSort(arg);
            } else if (arg.startsWith("--order=")) {
                orderStr = parseOrder(arg);
            } else if (arg.equals("--stat")) {
                isStatMode = true;
            } else if (arg.startsWith("--output=") || arg.startsWith("-o=")) {
                outputFormatStr = parseOutputFormat(arg);
            } else if (arg.startsWith("--path=")) {
                outputPath = parseOutputPath(arg);
            }
        }
        if (orderStr != null && sortByStr == null) {
            throw new IllegalArgumentException(MISSING_SORT_FOR_ORDER_MESSAGE);
        }
        SortBy sortBy = null;
        if (sortByStr != null) {
            sortBy = SortBy.fromString(sortByStr);
        }
        Order order = null;
        if (orderStr != null) {
            order = Order.fromString(orderStr);
        }
        return new Arguments(
                sortBy,
                order,
                isStatMode,
                OutputFormat.fromString(outputFormatStr),
                outputPath
        );
    }

    private static String parseSort(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length != 2 || !(parts[1].equalsIgnoreCase("name")
                || parts[1].equalsIgnoreCase("salary"))) {
            throw new IllegalArgumentException(INVALID_SORT_OPTION_MESSAGE);
        }
        return parts[1].toLowerCase();
    }

    private static String parseOrder(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length != 2 || !(parts[1].equalsIgnoreCase("asc")
                || parts[1].equalsIgnoreCase("desc"))) {
            throw new IllegalArgumentException(INVALID_ORDER_OPTION_MESSAGE);
        }
        return parts[1].toLowerCase();
    }

    private static String parseOutputFormat(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length != 2 || !(parts[1].equalsIgnoreCase("console")
                || parts[1].equalsIgnoreCase("file"))) {
            throw new IllegalArgumentException(INVALID_OUTPUT_FORMAT_MESSAGE);
        }
        return parts[1].toLowerCase();
    }

    private static String parseOutputPath(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length != 2 || parts[1].isEmpty()) {
            throw new IllegalArgumentException(INVALID_OUTPUT_PATH_MESSAGE);
        }
        return parts[1];
    }
}