import services.DepartmentService;
import utils.FilesReader;

public class Main {
    public static void main(String[] args) {
        FilesReader filesReader = new FilesReader();
        DepartmentService service = new DepartmentService(filesReader);
        service.start();
    }
}