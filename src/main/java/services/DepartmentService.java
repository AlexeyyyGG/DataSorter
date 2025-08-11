package services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import logger.ErrorLogger;
import models.Department;
import models.Employee;
import models.Manager;
import utils.FileWriter;
import utils.FilesReader;
import utils.ParseResult;

public class DepartmentService {
    private final FilesReader filesReader;
    private final FileWriter fileWriter;
    private final Map<String, Department> departments = new HashMap<>();

    public DepartmentService(FilesReader filesReader, FileWriter fileWriter) {
        this.filesReader = filesReader;
        this.fileWriter = fileWriter;
    }

    public void start() {
        ParseResult result = filesReader.readSbFilesAndParse();
        buildDepartments(result);
        fileWriter.setDepartments(departments);
        fileWriter.writeFiles();
    }

    public void buildDepartments(ParseResult result) {
        for (Manager manager : result.getManagers()) {
            Department department = getOrCreateDepartment(manager.getDepartmentName());
            department.addManager(manager);
        }
        for (Employee employee : result.getEmployees()) {
            Manager manager = findManagerById(result.getManagers(), employee.getManagerId());
            if (manager != null) {
                Department department = getOrCreateDepartment(manager.getDepartmentName());
                department.addEmployee(employee);
            } else {
                ErrorLogger.errorLog(employee.toString());
            }
        }
    }

    public Department getOrCreateDepartment(String name) {
        return departments.computeIfAbsent(name, Department::new);
    }

    public Manager findManagerById(List<Manager> managers, int id) {
        for (Manager m : managers) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }
}
