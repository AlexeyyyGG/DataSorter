package datasorter;

import datasorter.models.Department;
import datasorter.models.Employee;
import datasorter.result.BuildDeptResult;
import datasorter.result.Order;
import datasorter.result.ParseResult;
import datasorter.result.SortBy;
import datasorter.services.DepartmentService;
import datasorter.services.StatisticsService;
import datasorter.utils.ArgsParser;
import datasorter.result.Arguments;
import datasorter.utils.FileWriter;
import datasorter.utils.FilesReader;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser();
        Arguments arguments = argsParser.parseArgs(args);
        SortBy sortBy = arguments.sortBy();
        Order order = arguments.order();
        ParseResult result = FilesReader.readSbFilesAndParse();
        BuildDeptResult buildDeptResult = DepartmentService.buildDepartments(
                result.managers(),
                result.employees(),
                sortBy,
                order
        );
        Map<String, Department> departments = buildDeptResult.departments();
        if (arguments.isStatMode()) {
            StatisticsService.printStatistics(arguments, departments);
        }
        List<Employee> employeesWithOutDept = buildDeptResult.employeesWithOutDept();
        FileWriter.writeErrors(result.errors(), employeesWithOutDept);
        FileWriter.writeFiles(departments);
    }
}