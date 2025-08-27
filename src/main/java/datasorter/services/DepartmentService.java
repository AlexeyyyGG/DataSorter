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
            Map<Integer, Manager> managers,
            List<Employee> employees,
            SortBy sortBy,
            Order order
    ) {
        Map<String, Department> departments = new HashMap<>();
        List<Manager> managersWithOutDept = new ArrayList<>();
        List<Employee> employeesWithOutDept = new ArrayList<>();
        for (Manager manager : managers.values()) {
            Department department = createDepartment(departments, manager.getDepartmentName());
            if (department.getManager() != null) {
                managersWithOutDept.add(manager);
            } else {
                department.addManager(manager);
            }
        }
        for (Employee employee : employees) {
            Manager manager = findManagerById(managers, employee.getManagerId());
            if (manager != null) {
                Department department = departments.get(manager.getDepartmentName());
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
        return new BuildDeptResult(departments, employeesWithOutDept, managersWithOutDept);
    }

    private Department createDepartment(Map<String, Department> map, String name) {
        Department department = new Department(name);
        map.put(name, department);
        return department;
    }

    private Manager findManagerById(Map<Integer, Manager> managers, int id) {
        return managers.get(id);
    }

    private void sortEmployees(List<Employee> employees, SortBy sortBy, Order order) {
        employees.sort((e1, e2) -> {
            int comparsion;
            if (sortBy == SortBy.NAME) {
                comparsion = e1.getName().compareToIgnoreCase(e2.getName());
            } else {
                comparsion = Double.compare(e1.getSalary(), e2.getSalary());
            }
            return order == Order.ASC ? comparsion : -comparsion;
        });
    }
}