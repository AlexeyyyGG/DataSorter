package datasorter.statistics;

public class DepartmentStatistics {
    private final String departmentName;
    private final String minSalary;
    private final String maxSalary;
    private final String midSalary;

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

    public String getDepartmentName() {
        return departmentName;
    }

    public String getMinSalary() {
        return minSalary;
    }

    public String getMaxSalary() {
        return maxSalary;
    }

    public String getMidSalary() {
        return midSalary;
    }

    private String formatDouble(double value) {
        return String.format("%.2f", value);
    }
}