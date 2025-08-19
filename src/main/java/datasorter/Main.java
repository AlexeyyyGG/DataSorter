package datasorter;

import datasorter.utils.AppRunner;
import datasorter.utils.ArgsParser;

public class Main {
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser();
        argsParser.parseArgs(args);
        AppRunner appRunner = new AppRunner(argsParser);
        appRunner.run();
    }
}