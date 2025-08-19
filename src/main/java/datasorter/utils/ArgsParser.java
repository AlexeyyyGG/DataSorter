package datasorter.utils;

public class ArgsParser {
    private String sortBy;
    private String order;
    private boolean isStatMode;
    private String outputFormat = "console";
    private String outputPath;

    public void parseArgs(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--sort") || arg.startsWith("-s")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2) {
                    String value = parts[1].toLowerCase();
                    if (value.equals("name") || value.equals("salary")) {
                        sortBy = value;
                    } else {
                        throw new IllegalArgumentException("Invalid sort option");
                    }
                }
            } else if (arg.startsWith("--order=")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2) {
                    String value = parts[1].toLowerCase();
                    if (value.equals("asc") || value.equals("desc")) {
                        order = value;
                    } else {
                        throw new IllegalArgumentException("Invalid order option");
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
                        throw new IllegalArgumentException("Invalid output format");
                    }
                }
            } else if (arg.startsWith("--path=")) {
                String[] parts = arg.split("=", 2);
                if (parts.length == 2 && !parts[1].isEmpty()) {
                    outputPath = parts[1];
                } else {
                    throw new IllegalArgumentException("Invalid output path");
                }
            }
        }
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getOrder() {
        return order;
    }

    public boolean isStatMode() {
        return isStatMode;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public String getOutputPath() {
        return outputPath;
    }
}
