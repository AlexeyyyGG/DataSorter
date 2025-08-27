package datasorter;

import datasorter.models.Department;
import datasorter.result.BuildDeptResult;
import datasorter.result.ParseResult;
import datasorter.services.DepartmentService;
import datasorter.services.StatisticsService;
import datasorter.utils.ArgsParser;
import datasorter.result.Arguments;
import datasorter.utils.FileWriter;
import datasorter.utils.FilesReader;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Arguments arguments = null;
        if (args.length == 0) {
            System.out.println("Запущена программа без параметров.");
            arguments = Arguments.defaultArguments();
        } else {
            boolean argsValid = false;
            while (!argsValid) {
                try {
                    arguments = ArgsParser.parseArgs(args);
                    argsValid = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Некорректные аргументы: " + e.getMessage());
                    System.out.println("Введите правильные аргументы:");
                    String inputLine = scanner.nextLine();
                    args = inputLine.trim().split("\\s+");
                }
            }
        }
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