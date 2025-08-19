package datasorter.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.models.Manager;
import datasorter.statistics.DepartmentStatistics;
import datasorter.utils.FileWriter;
import datasorter.utils.FilesReader;
import datasorter.utils.ParseResult;

public class DepartmentService {
    private final Map<String, Department> departments = new HashMap<>();
    private final List<Employee> employeesWithOutDept = new ArrayList<>();
    private final String sortBy;
    private final String order;

    public DepartmentService(String sortBy, String order) {
        this.sortBy = sortBy;
        this.order = order;
    }

    public void start() {
        ParseResult result = FilesReader.readSbFilesAndParse();
        buildDepartments(result);
        if (sortBy != null && order != null) {
            for (Department dept : departments.values()) {
                List<Employee> employees = dept.getEmployees();
                sortEmployees(employees, sortBy, order);
            }
        }
        FileWriter.writeErrors(result.errors(), employeesWithOutDept);
        FileWriter.writeFiles(departments);
    }

    private void buildDepartments(ParseResult result) {
        for (Manager manager : result.managers()) {
            Department department = getOrCreateDepartment(manager.getDepartmentName());
            department.addManager(manager);
        }
        for (Employee employee : result.employees()) {
            Manager manager = findManagerById(result.managers(), employee.getManagerId());
            if (manager != null) {
                Department department = getOrCreateDepartment(manager.getDepartmentName());
                department.addEmployee(employee);
            } else {
                employeesWithOutDept.add(employee);
            }
        }
    }

    private Department getOrCreateDepartment(String name) {
        return departments.computeIfAbsent(name, Department::new);
    }

    private Manager findManagerById(List<Manager> managers, int id) {
        for (Manager m : managers) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    private void sortEmployees(List<Employee> employees, String sortBy, String order) {
        employees.sort((e1, e2) -> {
            int cmp;
            if ("name".equalsIgnoreCase(sortBy)) {
                cmp = e1.getName().compareToIgnoreCase(e2.getName());
            } else {
                cmp = Double.compare(e1.getSalary(), e2.getSalary());
            }
            return "asc".equalsIgnoreCase(order) ? cmp : -cmp;
        });
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