package datasorter.services;

import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.statistics.DepartmentStatistics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class StatisticsService {
    Map<String, Department> departments;

    public StatisticsService(Map<String, Department> departments) {
        this.departments = departments;
    }

    public List<DepartmentStatistics> getStatistics() {
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

    private double roundUp(double value) {
        return Math.ceil(value * 100) / 100.0;
    }
}