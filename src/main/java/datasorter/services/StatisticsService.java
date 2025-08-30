package datasorter.services;

import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.result.Arguments;
import datasorter.result.OutputFormat;
import datasorter.statistics.DepartmentStatistics;
import datasorter.statistics.FormatStatistics;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    public void printStatistics(Arguments arguments, Map<String, Department> departments) {
        List<DepartmentStatistics> stats = createStatistics(departments);
        String outputContent = FormatStatistics.statForm(stats);
        if (arguments.outputFormat() == OutputFormat.CONSOLE) {
            System.out.println(outputContent);
        } else {
            printToFile(arguments.outputPath(), outputContent);
        }
    }

    private void printToFile(String pathStr, String Content) {
        try {
            Files.writeString(Path.of(pathStr), Content);
        } catch (Exception e) {
            System.out.println("Failed to write file" + e.getMessage());
        }
    }

    private List<DepartmentStatistics> createStatistics(
            Map<String, Department> departments
    ) {
        List<DepartmentStatistics> statsList = new ArrayList<>();
        for (Department dept : departments.values()) {
            String deptName = dept.getName();
            DoubleSummaryStatistics stats = dept.getEmployees().stream()
                    .mapToDouble(Employee::getSalary)
                    .summaryStatistics();
            double minSalary = stats.getCount() > 0 ? stats.getMin() : 0;
            double maxSalary = stats.getCount() > 0 ? stats.getMax() : 0;
            double midSalary = stats.getCount() > 0 ? stats.getAverage() : 0;
            statsList.add(new DepartmentStatistics(deptName,
                    roundUp(minSalary),
                    roundUp(maxSalary),
                    roundUp(midSalary)));
        }
        statsList.sort(Comparator.comparing(DepartmentStatistics::getDepartmentName));
        return statsList;
    }

    private double roundUp(double value) {
        return Math.ceil(value * 100) / 100.0;
    }
}