import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import services.DepartmentService;
import services.ManagerService;
import utils.FIleFinder;
import utils.FileParser;
import utils.FileWriter;

public class Main {
    public static void main(String[] args) {
        DepartmentService departmentService = new DepartmentService();
        ManagerService managerService = new ManagerService();
        FileParser parser = new FileParser(departmentService, managerService);
        FileWriter writer = new FileWriter(departmentService.getAllDepartments());
        List<Path> files = FIleFinder.findSbFiles();
        for (Path file : files) {
            System.out.println(file.getFileName());
            try {
                List<String> lines = Files.readAllLines(file);
                parser.loadLinesFromFile(lines);
                parser.parseLines();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.writeFiles();
    }
}