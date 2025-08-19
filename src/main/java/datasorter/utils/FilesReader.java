package datasorter.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import datasorter.models.Employee;
import datasorter.models.Manager;
import datasorter.validator.SalaryValidator;

public class FilesReader {
    private static final String SYSTEM_PROPERTY_USER_DIR = "user.dir";
    private static final String FILE_PATTERN = "*.sb";
    private static final String ERROR_READING_MESSAGE = "Error while reading directory";
    private static final String MANAGER_STRING = "manager";
    private static final String EMPLOYEE_STRING = "employee";

    public static ParseResult readSbFilesAndParse() {
        List<Path> files = findSbFiles();
        List<Manager> managers = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        Set<Integer> managerIds = new HashSet<>();
        Set<Integer> employeeIds = new HashSet<>();
        for (Path filePath : files) {
            parseFile(filePath, managers, employees, errors, managerIds, employeeIds);
        }
        return new ParseResult(managers, employees, errors);
    }

    private static List<Path> findSbFiles() {
        Path directory = Paths.get(System.getProperty(SYSTEM_PROPERTY_USER_DIR));
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, FILE_PATTERN)) {
            for (Path path : stream) {
                files.add(path.toAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(ERROR_READING_MESSAGE, e);
        }
        return files;
    }

    private static void parseFile(
            Path filePath,
            List<Manager> managers,
            List<Employee> employees,
            List<String> errors,
            Set<Integer> managerIds,
            Set<Integer> employeeIds
    ) {
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            errors.add(ERROR_READING_MESSAGE);
            return;
        }
        for (String line : lines) {
            parseLine(line, managers, employees, errors, managerIds, employeeIds);
        }
    }

    private static void parseLine(
            String line,
            List<Manager> managers,
            List<Employee> employees,
            List<String> errors,
            Set<Integer> managerIds,
            Set<Integer> employeeIds
    ) {
        String[] args = line.split(",");
        if (args.length < 5) {
            errors.add(line);
            return;
        }
        String position = args[0].trim();
        try {
            int id = Integer.parseInt(args[1].trim());
            String name = args[2].trim();
            double salary = Double.parseDouble(args[3].trim());
            if (!SalaryValidator.validateSalary(salary)) {
                errors.add(line);
                return;
            }
            if (position.equalsIgnoreCase(MANAGER_STRING)) {
                createManager(id, name, salary, args[4], managers, errors, managerIds, line);
            } else if (position.equalsIgnoreCase(EMPLOYEE_STRING)) {
                createEmployee(id, name, salary, args[4], employees, errors, employeeIds, line);
            } else {
                errors.add(line);
            }
        } catch (NumberFormatException e) {
            errors.add(line);
        }
    }

    private static void createManager(
            int id,
            String name,
            double salary,
            String departmentName,
            List<Manager> managers,
            List<String> errors,
            Set<Integer> managerIds,
            String line
    ) {
        if (managerIds.contains(id)) {
            errors.add(line);
            return;
        }
        managerIds.add(id);
        Manager manager = new Manager(id, name.trim(), salary, departmentName.trim());
        managers.add(manager);
    }

    private static void createEmployee(
            int id,
            String name,
            double salary,
            String managerIdStr,
            List<Employee> employees,
            List<String> errors,
            Set<Integer> employeeIds,
            String line
    ) {
        int managerId;
        try {
            managerId = Integer.parseInt(managerIdStr.trim());
        } catch (NumberFormatException e) {
            errors.add(line);
            return;
        }
        if (employeeIds.contains(id)) {
            errors.add(line);
            return;
        }
        employeeIds.add(id);
        Employee employee = new Employee(id, name.trim(), salary, managerId);
        employees.add(employee);
    }
}