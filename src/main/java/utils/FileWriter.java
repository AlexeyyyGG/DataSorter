package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import models.Department;
import models.Employee;
import models.Manager;

public class FileWriter {
    private static final String MANAGER_PARAMS = "Manager, %d, %s, %.2f, %s";
    private static final String EMPLOYEE_PARAMS = "Employee, %d, %s, %.2f, %d";
    private static final String FAILED_TO_WRITE_MESSAGE = "Failed to write file";

    public void writeFiles(Map<String, Department> departments) {
        for (String departmentName : departments.keySet()) {
            Department department = departments.get(departmentName);
            String fileName = departmentName + ".sb";
            try (BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(fileName))) {
                if (department.getManager() != null) {
                    Manager manager = department.getManager();
                    bw.write(String.format(
                            Locale.US,
                            MANAGER_PARAMS,
                            manager.getId(),
                            manager.getName(),
                            manager.getSalary(),
                            manager.getDepartmentName())
                    );
                    bw.newLine();
                }
                for (Employee employee : department.getEmployees()) {
                    bw.write(String.format(
                            Locale.US,
                            EMPLOYEE_PARAMS,
                            employee.getId(),
                            employee.getName(),
                            employee.getSalary(),
                            employee.getManagerId())
                    );
                    bw.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(FAILED_TO_WRITE_MESSAGE, e);
            }
        }
    }
}
