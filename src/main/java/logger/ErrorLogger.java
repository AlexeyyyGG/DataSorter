package logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorLogger {
    private static final String FAILED_TO_WRITE = "Failed to write to error log";
    private static final String ERROR_LOG_FILE = "error.log";

    public static void errorLog(String line) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ERROR_LOG_FILE, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException ioe) {
            throw new RuntimeException(FAILED_TO_WRITE, ioe);
        }
    }
}
