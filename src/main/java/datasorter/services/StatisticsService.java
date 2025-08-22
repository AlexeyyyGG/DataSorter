package datasorter.services;

import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.result.Arguments;
import datasorter.statistics.DepartmentStatistics;
import datasorter.statistics.FormatStatistics;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    public static void printStatistics(Arguments arguments, Map<String, Department> departments) {
        List<DepartmentStatistics> stats = createStatistics(departments);
        String outputContent = FormatStatistics.statForm(stats);
        if ("console".equalsIgnoreCase(arguments.outputFormat().toString())) {
            System.out.println(outputContent);
        } else {
            String pathStr = arguments.outputPath();
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

    private static List<DepartmentStatistics> createStatistics(
            Map<String, Department> departments
    ) {
        List<DepartmentStatistics> statsList = new ArrayList<>();
        List<String> deptNames = new ArrayList<>(departments.keySet());
        Collections.sort(deptNames);
        for (String deptName : deptNames) {
            Department dept = departments.get(deptName);
            List<Double> salaries = new ArrayList<>();
            for (Employee emp : dept.getEmployees()) {
                double salaryVal = emp.getSalary();
                if (!Double.isNaN(salaryVal)) {
                    salaries.add(salaryVal);
                }
            }
            double minSalary = 0.0, maxSalary = 0.0, midSalary = 0.0;
            if (!salaries.isEmpty()) {
                minSalary = Collections.min(salaries);
                maxSalary = Collections.max(salaries);
                double sum = 0.0;
                for (double s : salaries) {
                    sum += s;
                }
                midSalary = sum / salaries.size();
            }
            statsList.add(new DepartmentStatistics(deptName,
                    roundUp(minSalary),
                    roundUp(maxSalary),
                    roundUp(midSalary)));
        }
        return statsList;
    }

    private static double roundUp(double value) {
        return Math.ceil(value * 100) / 100.0;
    }
}