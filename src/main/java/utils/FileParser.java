package utils;

import java.util.ArrayList;
import java.util.List;
import logger.ErrorLogger;
import models.Department;
import models.Employee;
import models.Manager;
import services.DepartmentService;
import services.ManagerService;

public class FileParser {
    private final DepartmentService departmentService;
    private final ManagerService managerService;
    private List<String> allLines = new ArrayList<>();

    public FileParser(DepartmentService departmentService, ManagerService managerService) {
        this.departmentService = departmentService;
        this.managerService = managerService;
    }

    public void loadLinesFromFile(List<String> lines) {
        this.allLines = lines;
    }

    public void parseLines() {
        for (String line : allLines) {
            String[] args = line.split(",");
            if (args.length < 5) {
                ErrorLogger.errorLog(line);
                continue;
            }
            String position = args[0].trim();
            if (position.equalsIgnoreCase("manager")) {
                try {
                    int id = Integer.parseInt(args[1].trim());
                    String name = args[2].trim();
                    double salary = Double.parseDouble(args[3].trim());
                    String departmentName = args[4].trim();
                    Manager manager = new Manager(id, name, salary, departmentName);
                    managerService.addManager(manager);
                    Department department = departmentService.getOrAddDepartment(departmentName);
                    department.addManager(manager);
                } catch (NumberFormatException e) {
                    ErrorLogger.errorLog(line);
                }
            }
        }
        for (String line : allLines) {
            String[] args = line.split(",");
            if (args.length < 5) {
                ErrorLogger.errorLog(line);
                continue;
            }
            String position = args[0].trim();
            if (position.equalsIgnoreCase("employee")) {
                try {
                    int id = Integer.parseInt(args[1].trim());
                    String name = args[2].trim();
                    double salary = Double.parseDouble(args[3].trim());
                    int managerId = Integer.parseInt(args[4].trim());
                    Manager manager = null;
                    for (Manager m : managerService.getManagers()) {
                        if (m.getId() == managerId) {
                            manager = m;
                            break;
                        }
                    }
                    if (manager != null) {
                        String departmentName = manager.getDepartmentName();
                        Employee employee = new Employee(id, name, salary, managerId);
                        Department department = departmentService.getOrAddDepartment(
                                departmentName);
                        department.addEmployee(employee);
                    } else {
                        ErrorLogger.errorLog(line);
                    }
                } catch (NumberFormatException e) {
                    ErrorLogger.errorLog(line);
                }
            }
        }
    }
}
