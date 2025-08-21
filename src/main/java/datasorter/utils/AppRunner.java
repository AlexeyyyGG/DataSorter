package datasorter.utils;

import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.result.BuildDeptResult;
import datasorter.result.ParseArgsResult;
import datasorter.result.ParseResult;
import datasorter.services.DepartmentService;
import datasorter.services.StatisticsService;
import datasorter.statistics.DepartmentStatistics;
import datasorter.statistics.FormatStatistics;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class AppRunner {
    private final ParseArgsResult parseArgsResult;

    public AppRunner(ParseArgsResult parseArgsResult) {
        this.parseArgsResult = parseArgsResult;
    }

    public void run() {
        String sortBy = parseArgsResult.sortBy();
        String order = parseArgsResult.order();
        ParseResult result = FilesReader.readSbFilesAndParse();
        DepartmentService departmentService = new DepartmentService();
        BuildDeptResult buildDeptResult = departmentService.buildDepartments(result, sortBy, order);
        Map<String, Department> departments = buildDeptResult.departments();
        StatisticsService statisticsService = new StatisticsService(departments);
        if (parseArgsResult.isStatMode()) {
            List<DepartmentStatistics> stats = statisticsService.getStatistics();
            String outputContent = FormatStatistics.statForm(stats);
            if ("console".equals(parseArgsResult.outputFormat())) {
                System.out.println(outputContent);
            } else {
                String pathStr = parseArgsResult.outputPath();
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
        List<Employee> employeesWithOutDept = buildDeptResult.employeesWithOutDept();
        FileWriter.writeErrors(result.errors(), employeesWithOutDept);
        FileWriter.writeFiles(departments);
    }
}