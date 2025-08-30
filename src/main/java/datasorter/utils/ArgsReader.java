package datasorter.utils;

import datasorter.result.Arguments;
import java.util.Scanner;

public class ArgsReader {
    private String[] args;
    private final Scanner scanner;

    public ArgsReader(Scanner scanner, String[] args) {
        this.scanner = scanner;
        this.args = args;
    }
    public Arguments readArguments(){
        Arguments arguments = null;
        if (args.length == 0) {
            System.out.println("The program was started without parameters.");
            arguments = Arguments.defaultArguments();
        } else {
            boolean argsValid = false;
            while (!argsValid) {
                try {
                    arguments = ArgsParser.parseArgs(args);
                    argsValid = true;
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid arguments: " + e.getMessage());
                    System.out.println("Please enter the correct arguments:");
                    String inputLine = scanner.nextLine();
                    args = inputLine.trim().split("\\s+");
                }
            }
        }
        return arguments;
    }
}
