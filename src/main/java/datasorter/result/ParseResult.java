package datasorter.result;

import java.util.List;
import datasorter.models.Employee;
import datasorter.models.Manager;
import java.util.Map;

public record ParseResult(
        Map<Integer, Manager> managers,
        List<Employee> employees,
        List<String> errors
) {
}