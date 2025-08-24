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
import java.util.Collections;
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
        if (pathStr == null || pathStr.isEmpty()) {
            System.out.println("Path to file for statistics output is not specified");
            return;
        }
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
            List<Double> salaries = new ArrayList<>();
            for (Employee emp : dept.getEmployees()) {
                double salaryVal = emp.getSalary();
                salaries.add(salaryVal);
            }
            double minSalary = 0.0, maxSalary = 0.0, midSalary = 0.0;
            if (!salaries.isEmpty()) {
                minSalary = Collections.min(salaries);
                maxSalary = Collections.max(salaries);
                midSalary = calculateAverageSalary(salaries);
            }
            statsList.add(new DepartmentStatistics(deptName,
                    roundUp(minSalary),
                    roundUp(maxSalary),
                    roundUp(midSalary)));
        }
        Collections.sort(statsList, (d1, d2) ->
                d1.getDepartmentName().compareTo(d2.getDepartmentName()));
        return statsList;
    }

    private double calculateAverageSalary(List<Double> salaries) {
        if (salaries.isEmpty()) {
            return 0.0;
        }
        double sum = 0.0;
        for (double s : salaries) {
            sum += s;
        }
        return sum / salaries.size();
    }

    private double roundUp(double value) {
        return Math.ceil(value * 100) / 100.0;
    }
}