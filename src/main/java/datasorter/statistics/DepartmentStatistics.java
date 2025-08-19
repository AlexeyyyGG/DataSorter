package datasorter.statistics;

public class DepartmentStatistics {
    public final String departmentName;
    public final String minSalary;
    public final String maxSalary;
    public final String midSalary;

    public DepartmentStatistics(
            String departmentName,
            double minSalary,
            double maxSalary,
            double midSalary
    ) {
        this.departmentName = departmentName;
        this.minSalary = formatDouble(minSalary);
        this.maxSalary = formatDouble(maxSalary);
        this.midSalary = formatDouble(midSalary);
    }

    private static String formatDouble(double value) {
        return String.format("%.2f", value);
    }
}