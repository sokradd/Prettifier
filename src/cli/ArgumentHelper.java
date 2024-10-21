/*
This class will generally parse and manage arguments that are passed via the command line...
...(e.g. input.txt, output.txt, Airport-lookup.csv).
Flags such as -h to display help will also be handled.
 */
package cli;

import static cli.UsageHelper.printUsage;

public class ArgumentHelper {

    private String inputFilePath;
    private String outputFilePath;
    private String airportLookUpPath;

    public void parseArgument(String[] args) {

        if (args == null || args.length == 0 || (args.length == 1 && args[0].trim().isEmpty())) {
            printUsage();
            System.exit(0);
        }

        if (args.length == 1 & args[0].equals("-h")) {
            printUsage();
            System.exit(0);
        }
        if (args.length != 3) {
            printUsage();
            System.exit(0);
        }

        inputFilePath = args[0];
        outputFilePath = args[1];
        airportLookUpPath = args[2];

    }

    public String getInputFilePath() {
        return inputFilePath;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }

    public String getAirportLookUpPath() {
        return airportLookUpPath;
    }
}
