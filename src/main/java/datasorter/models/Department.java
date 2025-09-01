package datasorter.models;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final String name;
    private Manager manager;
    private final List<Employee> employees;

    public Department(String name) {
        this.name = name;
        this.employees = new ArrayList<>();
    }

    public void addManager(Manager manager) {
        this.manager = manager;
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public String getName() {
        return name;
    }

    public Manager getManager() {
        return manager;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}