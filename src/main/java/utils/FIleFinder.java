package utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FIleFinder {
    private static final String SYSTEM_PROPERTY_USER_DIR = "user.dir";
    private static final String FILE_PATTERN = "*.sb";
    private static final String ERROR_READING_MESSAGE = "Error while reading directory";

    public static List<Path> findSbFiles() {
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
}
