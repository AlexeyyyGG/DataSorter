package datasorter.statistics;

import java.util.List;

public class FormatStatistics {
    public static String statForm(List<DepartmentStatistics> stats) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-20s %-10s %-10s %-10s%n", "department", "min", "max", "mid"));
        for (DepartmentStatistics ds : stats) {
            sb.append(String.format("%-20s %-10s %-10s %-10s%n",
                    ds.departmentName,
                    ds.minSalary,
                    ds.maxSalary,
                    ds.midSalary));
        }
        return sb.toString();
    }
}