package datasorter.statistics;

import java.util.List;

public class FormatStatistics {
    private static final String HEADER_FORMAT = "%-20s %-10s %-10s %-10s%n";

    public static String statForm(List<DepartmentStatistics> stats) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(HEADER_FORMAT, "department", "min", "max", "mid"));
        for (DepartmentStatistics ds : stats) {
            sb.append(String.format(HEADER_FORMAT,
                    ds.getDepartmentName(),
                    ds.getMinSalary(),
                    ds.getMaxSalary(),
                    ds.getMidSalary()));
        }
        return sb.toString();
    }
}