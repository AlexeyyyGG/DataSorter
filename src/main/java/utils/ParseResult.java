package utils;

import java.util.List;
import models.Employee;
import models.Manager;

public class ParseResult {
    private final List<Manager> managers;
    private final List<Employee> employees;
    private final List<String> errors;

    public ParseResult(List<Manager> managers, List<Employee> employees, List<String> errors) {
        this.managers = managers;
        this.employees = employees;
        this.errors = errors;
    }

    public List<Manager> getManagers() {
        return managers;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<String> getErrors() {
        return errors;
    }
}

