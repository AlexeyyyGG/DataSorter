package datasorter.utils;

import datasorter.result.ParseResult;
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
        for (Path filePath : files) {
            ParseResult parseResult = parseFile(filePath);
            managers.addAll(parseResult.managers());
            employees.addAll(parseResult.employees());
            errors.addAll(parseResult.errors());
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

    private static ParseResult parseFile(Path filePath) {
        List<Manager> managers = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (IOException e) {
            errors.add(ERROR_READING_MESSAGE);
            return new ParseResult(managers, employees, errors);
        }
        Set<Integer> managerIds = new HashSet<>();
        Set<Integer> employeeIds = new HashSet<>();
        for (String line : lines) {
            ParseResult result = parseLine(line);
            errors.addAll(result.errors());
            for (Manager manager : result.managers()) {
                if (managerIds.contains(manager.getId())) {
                    errors.add(line);
                } else {
                    managerIds.add(manager.getId());
                }
                managers.add(manager);
            }
            for (Employee employee : result.employees()) {
                if (employeeIds.contains(employee.getId())) {
                    errors.add(line);
                } else {
                    employeeIds.add(employee.getId());
                }
                employees.add(employee);
            }
        }
        return new ParseResult(managers, employees, errors);
    }

    private static ParseResult parseLine(String line) {
        List<String> errors = new ArrayList<>();
        List<Manager> managers = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        String[] args = line.split(",");
        if (args.length < 5) {
            errors.add(line);
            return new ParseResult(managers, employees, errors);
        }
        String position = args[0].trim();
        int id;
        double salary;
        try {
            id = Integer.parseInt(args[1].trim());
            salary = Double.parseDouble(args[3].trim());
        } catch (NumberFormatException e) {
            errors.add(line);
            return new ParseResult(managers, employees, errors);
        }
        String name = args[2].trim();
        if (!SalaryValidator.validateSalary(salary)) {
            errors.add(line);
            return new ParseResult(managers, employees, errors);
        }
        if (position.equalsIgnoreCase(MANAGER_STRING)) {
            Manager manager = new Manager(id, name, salary, args[4].trim());
            managers.add(manager);
        } else if (position.equalsIgnoreCase(EMPLOYEE_STRING)) {
            int managerId;
            try {
                managerId = Integer.parseInt(args[4].trim());
            } catch (NumberFormatException e) {
                errors.add(line);
                return new ParseResult(managers, employees, errors);
            }
            Employee employee = new Employee(id, name, salary, managerId);
            employees.add(employee);
        } else {
            errors.add(line);
        }
        return new ParseResult(managers, employees, errors);
    }
}