package datasorter;

import datasorter.models.Department;
import datasorter.result.BuildDeptResult;
import datasorter.result.ParseResult;
import datasorter.services.DepartmentService;
import datasorter.services.StatisticsService;
import datasorter.result.Arguments;
import datasorter.utils.ArgsReader;
import datasorter.utils.FileWriter;
import datasorter.utils.FilesReader;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArgsReader argsReader = new ArgsReader(scanner, args);
        Arguments arguments = argsReader.readArguments();
        DepartmentService departmentService = new DepartmentService();
        StatisticsService statisticsService = new StatisticsService();
        ParseResult result = FilesReader.readSbFilesAndParse();
        BuildDeptResult buildDeptResult = departmentService.buildDepartments(
                result.managers(),
                result.employees(),
                arguments.sortBy(),
                arguments.order()
        );
        Map<String, Department> departments = buildDeptResult.departments();
        if (arguments.isStatMode()) {
            statisticsService.printStatistics(arguments, departments);
        }
        FileWriter.writeErrors(
                result.errors(),
                buildDeptResult.employeesWithOutDept(),
                buildDeptResult.managersWithOutDept()
        );
        FileWriter.writeFiles(departments);
    }
}