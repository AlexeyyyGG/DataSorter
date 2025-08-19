package datasorter.utils;

import datasorter.services.DepartmentService;
import datasorter.statistics.DepartmentStatistics;
import datasorter.statistics.FormatStatistics;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AppRunner {
    private final ArgsParser argsParser;

    public AppRunner(ArgsParser argsParser) {
        this.argsParser = argsParser;
    }

    public void run() {
        DepartmentService service = new DepartmentService(
                argsParser.getSortBy(),
                argsParser.getOrder()
        );
        service.start();
        if (argsParser.isStatMode()) {
            List<DepartmentStatistics> stats = service.getStatistics();
            String outputContent = FormatStatistics.statForm(stats);
            if ("console".equals(argsParser.getOutputFormat())) {
                System.out.println(outputContent);
            } else {
                String pathStr = argsParser.getOutputPath();
                if (pathStr == null || pathStr.isEmpty()) {
                    System.out.println("Path to file for statistics output is not specified");
                    return;
                }
                try {
                    Files.writeString(Path.of(pathStr), outputContent);
                } catch (Exception e) {
                    System.out.println("Failed to write file" + e.getMessage());
                }
            }
        }
    }
}