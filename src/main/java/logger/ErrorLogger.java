package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import models.Employee;

public class ErrorLogger {
    private static final String FAILED_TO_WRITE = "Failed to write to error log";
    private static final String ERROR_LOG_FILE = "error.log";

    public static void errorLogString(List<String> errors) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ERROR_LOG_FILE, true))) {
            for (String error : errors) {
                bw.write(error);
                bw.newLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(FAILED_TO_WRITE, ioe);
        }
    }

    public static void errorLogEmployee(List<Employee> employees) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ERROR_LOG_FILE, true))) {
            for (Employee employee : employees) {
                bw.write(employee.toString());
                bw.newLine();
            }
        } catch (IOException ioe) {
            throw new RuntimeException(FAILED_TO_WRITE, ioe);
        }
    }
}
