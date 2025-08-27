package datasorter.result;

import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.models.Manager;
import java.util.List;
import java.util.Map;

public record BuildDeptResult(
        Map<String, Department> departments,
        List<Employee> employeesWithOutDept,
        List<Manager> managersWithOutDept
) {
}
