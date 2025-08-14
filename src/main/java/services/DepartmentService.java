package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Department;
import models.Employee;
import models.Manager;
import utils.FileWriter;
import utils.FilesReader;
import utils.ParseResult;

public class DepartmentService {
    private final FilesReader filesReader;
    private final Map<String, Department> departments = new HashMap<>();
    private final List<Employee> employeesWithOutDept = new ArrayList<>();

    public DepartmentService(FilesReader filesReader) {
        this.filesReader = filesReader;
    }

    public void start() {
        ParseResult result = filesReader.readSbFilesAndParse();
        buildDepartments(result);
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
}