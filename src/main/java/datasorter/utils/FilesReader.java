package datasorter.utils;

import datasorter.result.ParseData;
import datasorter.result.ParseResult;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        Map<Integer, Manager> managers = new HashMap<>();
        List<Employee> employees = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (Path filePath : files) {
            ParseResult parseResult = parseFile(filePath);
            for (Manager manager : parseResult.managers().values()) {
                if (managers.containsKey(manager.getId())) {
                    errors.add(manager.toString());
                } else {
                    managers.put(manager.getId(), manager);
                }
            }
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
        Map<Integer, Manager> managers = new HashMap<>();
        List<Employee> employees = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<String> lines = readLinesFromFile(filePath);
        Set<Integer> employeeIds = new HashSet<>();
        for (String line : lines) {
            ParseResult result = parseLine(line);
            errors.addAll(result.errors());
            for (Manager manager : result.managers().values()) {
                if (managers.containsKey(manager.getId())) {
                    errors.add(line);
                } else {
                    managers.put(manager.getId(), manager);
                }
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

    private static List<String> readLinesFromFile(Path filePath) {
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new RuntimeException(ERROR_READING_MESSAGE, e);
        }
    }

    private static ParseResult parseLine(String line) {
        List<String> errors = new ArrayList<>();
        Map<Integer, Manager> managers = new HashMap<>();
        List<Employee> employees = new ArrayList<>();
        String[] args = cleanLine(line);
        String cleanedLine = String.join(",", args);
        if (args.length < 5) {
            errors.add(cleanedLine);
            return new ParseResult(managers, employees, errors);
        }
        String position = args[0].trim();
        ParseData data = parseCommonFields(args, errors, cleanedLine);
        if (data == null) {
            return new ParseResult(managers, employees, errors);
        }
        if (position.equalsIgnoreCase(MANAGER_STRING)) {
            Manager manager = createManager(data, args);
            managers.put(data.getId(), manager);
        } else if (position.equalsIgnoreCase(EMPLOYEE_STRING)) {
            try {
                Employee employee = createEmployee(data, args);
                employees.add(employee);
            } catch (NumberFormatException e) {
                errors.add(cleanedLine);
                return new ParseResult(managers, employees, errors);
            }
        } else {
            errors.add(cleanedLine);
        }
        return new ParseResult(managers, employees, errors);
    }

    private static Manager createManager(ParseData data, String[] args) {
        return new Manager(
                data.getId(),
                data.getName(),
                data.getSalary(),
                args[4].trim()
        );
    }

    private static Employee createEmployee(ParseData data, String[] args)
            throws NumberFormatException {
        int managerId = Integer.parseInt(args[4].trim());
        return new Employee(
                data.getId(),
                data.getName(),
                data.getSalary(),
                managerId
        );
    }

    private static ParseData parseCommonFields(
            String[] args,
            List<String> errors,
            String cleanedLine
    ) {
        int id;
        double salary;
        String name;
        try {
            id = Integer.parseInt(args[1].trim());
            salary = Double.parseDouble(args[3].trim());
            name = args[2].trim();
        } catch (NumberFormatException e) {
            errors.add(cleanedLine);
            return null;
        }
        if (!SalaryValidator.validateSalary(salary)) {
            errors.add(cleanedLine);
            return null;
        }
        return new ParseData(id, name, salary);
    }

    private static String[] cleanLine(String line) {
        String[] args = line.split(",");
        for (int i = 0; i < args.length; i++) {
            args[i] = args[i].trim();
        }
        return args;
    }
}