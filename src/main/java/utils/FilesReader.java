package utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import logger.ErrorLogger;
import models.Employee;
import models.Manager;

public class FilesReader {
    private static final String SYSTEM_PROPERTY_USER_DIR = "user.dir";
    private static final String FILE_PATTERN = "*.sb";
    private static final String ERROR_READING_MESSAGE = "Error while reading directory";

    private List<Path> findSbFiles() {
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

    public ParseResult readSbFilesAndParse() {
        List<Path> files = findSbFiles();
        List<Manager> managers = new ArrayList<>();
        List<Employee> employees = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        for (Path filePath : files) {
            List<String> lines;
            try {
                lines = Files.readAllLines(filePath);
            } catch (IOException e) {
                errors.add(ERROR_READING_MESSAGE);
                continue;
            }
            for (String line : lines) {
                String[] args = line.split(",");
                if (args.length < 5) {
                    errors.add(line);
                    continue;
                }
                String position = args[0].trim();
                try {
                    if (position.equalsIgnoreCase("manager")) {
                        int id = Integer.parseInt(args[1].trim());
                        String name = args[2].trim();
                        double salary = Double.parseDouble(args[3].trim());
                        String departmentName = args[4].trim();
                        Manager manager = new Manager(id, name, salary, departmentName);
                        managers.add(manager);
                    } else if (position.equalsIgnoreCase("employee")) {
                        int id = Integer.parseInt(args[1].trim());
                        String name = args[2].trim();
                        double salary = Double.parseDouble(args[3].trim());
                        int managerId = Integer.parseInt(args[4].trim());
                        Employee employee = new Employee(id, name, salary, managerId);
                        employees.add(employee);
                    } else {
                        errors.add(line);
                    }
                } catch (NumberFormatException e) {
                    errors.add(line);
                }
            }
        }
        return new ParseResult(managers, employees, errors);
    }
}
