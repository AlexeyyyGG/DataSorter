package datasorter.services;

import datasorter.result.BuildDeptResult;
import datasorter.result.Order;
import datasorter.result.SortBy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.models.Manager;

public class DepartmentService {
    public BuildDeptResult buildDepartments(
            List<Manager> managers,
            List<Employee> employees,
            SortBy sortBy,
            Order order
    ) {
        Map<String, Department> departments = new HashMap<>();
        List<Employee> employeesWithOutDept = new ArrayList<>();
        for (Manager manager : managers) {
            Department department = getOrCreateDepartment(departments, manager.getDepartmentName());
            department.addManager(manager);
        }
        for (Employee employee : employees) {
            Manager manager = findManagerById(managers, employee.getManagerId());
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
                List<Employee> deptEmployees = dept.getEmployees();
                sortEmployees(deptEmployees, sortBy, order);
            }
        }
        return new BuildDeptResult(departments, employeesWithOutDept);
    }

    private Department getOrCreateDepartment(Map<String, Department> map, String name) {
        if (map.containsKey(name)) {
            return map.get(name);
        } else {
            Department department = new Department(name);
            map.put(name, department);
            return department;
        }
    }

    private Manager findManagerById(List<Manager> managers, int id) {
        for (Manager m : managers) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    private void sortEmployees(List<Employee> employees, SortBy sortBy, Order order) {
        employees.sort((e1, e2) -> {
            int cmp;
            if (sortBy == SortBy.NAME) {
                cmp = e1.getName().compareToIgnoreCase(e2.getName());
            } else {
                cmp = Double.compare(e1.getSalary(), e2.getSalary());
            }
            return "asc".equalsIgnoreCase(String.valueOf(order)) ? cmp : -cmp;
        });
    }
}