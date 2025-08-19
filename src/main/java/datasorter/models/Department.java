package datasorter.models;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private Manager manager;
    private final List<Employee> employeesList;

    public Department(String name) {
        this.name = name;
        this.employeesList = new ArrayList<>();
    }

    public void addManager(Manager manager) {
        this.manager = manager;
    }

    public void addEmployee(Employee employee) {
        this.employeesList.add(employee);
    }

    public String getName() {
        return name;
    }

    public Manager getManager() {
        return manager;
    }

    public List<Employee> getEmployees() {
        return employeesList;
    }
}