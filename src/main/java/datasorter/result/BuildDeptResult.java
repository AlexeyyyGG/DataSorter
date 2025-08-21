package datasorter.result;

import datasorter.models.Department;
import datasorter.models.Employee;
import java.util.List;
import java.util.Map;

public record BuildDeptResult(
        Map<String, Department> departments,
        List<Employee> employeesWithOutDept
) {
}
