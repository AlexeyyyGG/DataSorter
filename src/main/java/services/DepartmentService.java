package services;

import java.util.HashMap;
import java.util.Map;
import models.Department;

public class DepartmentService {
    private final Map<String, Department> departments = new HashMap<>();

    public Department getOrAddDepartment(String departmentName) {
        return departments.computeIfAbsent(departmentName, Department::new);
    }

    public Map<String, Department> getAllDepartments() {
        return departments;
    }
}
