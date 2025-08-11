package utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import models.Department;
import models.Employee;
import models.Manager;

public class FileWriter {
    private Map<String, Department> departments;
    private static final String MANAGER_PARAMS = "Manager, %d, %s, %.2f, %s";
    private static final String EMPLOYEE_PARAMS = "Employee, %d, %s, %.2f, %d";

    public void setDepartments(Map<String, Department> departments) {
        this.departments = departments;
    }

    public void writeFiles() {
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
                throw new RuntimeException("Failed to write file", e);
            }
        }
    }
}
