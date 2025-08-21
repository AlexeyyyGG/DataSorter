package datasorter.models;

public class Manager {
    private final int id;
    private final String name;
    private final double salary;
    private final String departmentName;

    public Manager(int id, String name, Double salary, String departmentName) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.departmentName = departmentName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}