import services.DepartmentService;
import utils.FilesReader;
import utils.FileWriter;

public class Main {
    public static void main(String[] args) {
        FilesReader filesReader = new FilesReader();
        FileWriter fileWriter = new FileWriter();
        DepartmentService service = new DepartmentService(filesReader, fileWriter);
        service.start();
    }
}