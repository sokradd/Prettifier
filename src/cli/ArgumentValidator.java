package cli;

import text_transform.Color;

import java.io.*;

public class ArgumentValidator {
    public static boolean isInputFileExists(String inputFilePath) {
        File inputFile = new File(inputFilePath);

        if (!inputFile.exists()) {
            System.out.println(Color.ANSI_RED + "Input file has not found." + Color.ANSI_RESET);
            return false;
        }
        return true;
    }
    public static boolean isOutputFileExists(String outputFilePath) {
        File outputFile = new File(outputFilePath);

        if (!outputFile.exists()) {
            System.out.println(Color.ANSI_RED + "Output file has not found." + Color.ANSI_RESET);
            return false;
        }
        return true;
    }
    public static boolean isAirportFileExists (String airportLookUpPath) {
        File airportFile = new File(airportLookUpPath);

        if(!airportFile.exists()) {
            System.out.println(Color.ANSI_RED + "Airport file has not found." + Color.ANSI_RESET);
            return false;
        }
        return true;
    }
}
