package datasorter.services;

import datasorter.result.BuildDeptResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.models.Manager;
import datasorter.result.ParseResult;

public class DepartmentService {
    public BuildDeptResult buildDepartments(ParseResult result, String sortBy, String order) {
        Map<String, Department> departments = new HashMap<>();
        List<Employee> employeesWithOutDept = new ArrayList<>();
        for (Manager manager : result.managers()) {
            Department department = getOrCreateDepartment(departments, manager.getDepartmentName());
            department.addManager(manager);
        }
        for (Employee employee : result.employees()) {
            Manager manager = findManagerById(result.managers(), employee.getManagerId());
            if (manager != null) {
                Department department = getOrCreateDepartment(
                        departments,
                        manager.getDepartmentName()
                );
                department.addEmployee(employee);
            } else {
                employeesWithOutDept.add(employee);
            }
        }
        if (sortBy != null && order != null) {
            for (Department dept : departments.values()) {
                List<Employee> employees = dept.getEmployees();
                sortEmployees(employees, sortBy, order);
            }
        }
        return new BuildDeptResult(departments, employeesWithOutDept);
    }

    private Department getOrCreateDepartment(Map<String, Department> map, String name) {
        return map.computeIfAbsent(name, Department::new);
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
}