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

    public Arguments parseArgs(String[] args) {
        String sortByStr = null;
        String orderStr = null;
        boolean isStatMode = false;
        String outputFormatStr = "console";
        String outputPath = null;
        for (String arg : args) {
            if (arg.startsWith("--sort") || arg.startsWith("-s")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2) {
                    String value = parts[1].toLowerCase();
                    if (value.equals("name") || value.equals("salary")) {
                        sortByStr = value;
                    } else {
                        throw new IllegalArgumentException(INVALID_SORT_OPTION_MESSAGE);
                    }
                }
            } else if (arg.startsWith("--order=")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2) {
                    String value = parts[1].toLowerCase();
                    if (value.equals("asc") || value.equals("desc")) {
                        orderStr = value;
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
                        outputFormatStr = value;
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
}
