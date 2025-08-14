package utils;

import java.util.List;
import models.Employee;
import models.Manager;

public record ParseResult(List<Manager> managers, List<Employee> employees, List<String> errors) {
}