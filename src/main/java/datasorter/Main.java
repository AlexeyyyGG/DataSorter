package datasorter;

import datasorter.utils.AppRunner;
import datasorter.utils.ArgsParser;
import datasorter.result.ParseArgsResult;

public class Main {
    public static void main(String[] args) {
        ArgsParser argsParser = new ArgsParser();
        ParseArgsResult parseArgsResult = argsParser.parseArgs(args);
        AppRunner appRunner = new AppRunner(parseArgsResult);
        appRunner.run();
    }
}