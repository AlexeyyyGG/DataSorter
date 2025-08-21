package datasorter.result;

import java.util.List;
import datasorter.models.Employee;
import datasorter.models.Manager;

public record ParseResult(List<Manager> managers, List<Employee> employees, List<String> errors) {
}