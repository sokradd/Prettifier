import cli.ArgumentHelper;
import cli.ArgumentValidator;
import cli.UsageHelper;
import fileio.CSVReader;
import fileio.InputFileReader;
import fileio.OutputWriter;
import text_transform.TextFormatter;
import text_transform.WhitespaceCleaner;
import text_transform.AirportCodeConverter;
import text_transform.Color;
import text_transform.BlankSpaceCleaner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prettifier {
    public static void main(String[] args) {

        // Processing command-line arguments
        ArgumentHelper argumentHelper = new ArgumentHelper();
        argumentHelper.parseArgument(args);

        String inputFilePath = argumentHelper.getInputFilePath();
        String outputFilePath = argumentHelper.getOutputFilePath();
        String airportLookUpPath = argumentHelper.getAirportLookUpPath();

        // Check if files exist
        if (!ArgumentValidator.isInputFileExists(inputFilePath)) {
            System.err.println(Color.ANSI_RED + "Input file not found." + Color.ANSI_RESET);
            return; // Terminate the program
        }
        if (!ArgumentValidator.isAirportFileExists(airportLookUpPath)) {
            System.err.println(Color.ANSI_RED + "Airport lookup file not found." + Color.ANSI_RESET);
            return; // Terminate the program
        }

        CSVReader csvReader = new CSVReader();
        TextFormatter textFormatter = new TextFormatter();
        WhitespaceCleaner whitespaceCleaner = new WhitespaceCleaner();
        AirportCodeConverter airportCodeConverter = null;
        BlankSpaceCleaner blankSpaceCleaner = new BlankSpaceCleaner(); // Integrate the new class

        try {
            // Read airport data
            csvReader.readAirportData(airportLookUpPath);

            // Check for errors in reading airport data
            if (csvReader.isMalformed()) {
                System.err.println(Color.ANSI_RED + "Error: Malformed airport lookup data." + Color.ANSI_RESET);
                return; // Terminate the program without creating the file
            }

            airportCodeConverter = new AirportCodeConverter(csvReader.getAirportData());

            // Read data from the input file
            InputFileReader inputFileReader = new InputFileReader();
            inputFileReader.readInputData(inputFilePath);

            // List for formatted data
            List<String> formattedInputData = new ArrayList<>();
            for (String line : inputFileReader.getInputData()) {
                String cleanedLine = whitespaceCleaner.cleanWhitespace(line);
                String formattedLine = textFormatter.formatText(cleanedLine);

                // Convert airport codes (IATA and ICAO)
                formattedLine = replaceAirportCodes(formattedLine, airportCodeConverter, false); // ICAO
                formattedLine = replaceAirportCodes(formattedLine, airportCodeConverter, true);  // IATA

                formattedInputData.add(formattedLine);
            }

            // Process text to remove unnecessary blank lines
            String outputData = String.join("\n", formattedInputData);
            outputData = blankSpaceCleaner.cleanBlankSpaces(outputData); // Remove unnecessary blank lines

            // Write data to the output file (only if no errors occurred)
            OutputWriter outputWriter = new OutputWriter();
            outputWriter.writeToFile(Collections.singleton(outputData), outputFilePath);

        } catch (IOException e) {
            // If an error occurs while reading data, print a message and do not create the file
            System.err.println(Color.ANSI_RED + "Error in reading file. " + e.getMessage() + Color.ANSI_RESET);
            UsageHelper.printUsage();
            System.exit(1); // Terminate the program
        } catch (IllegalArgumentException e) {
            // If the data is invalid, print a message and do not create the file
            System.err.println(Color.ANSI_RED + "Error in data. " + e.getMessage() + Color.ANSI_RESET);
            System.exit(1); // Terminate the program
        } catch (Exception e) {
            // Handle any other unexpected errors
            System.err.println(Color.ANSI_RED + "Unexpected error. " + e.getMessage() + Color.ANSI_RESET);
            System.exit(1); // Terminate the program
        }
    }

    private static String replaceAirportCodes(String line, AirportCodeConverter converter, boolean isIATA) {
        boolean isReplacementForCityName = line.contains("*##") || line.contains("*#");

        String regex = getRegex(isIATA, isReplacementForCityName);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            String code = matcher.group(1);
            String replacement;

            if (isReplacementForCityName) {
                replacement = converter.convertCityName(code);
            } else {
                replacement = isIATA ? converter.convertIATACode(code) : converter.convertICAOCode(code);
            }

            matcher.appendReplacement(sb, replacement != null ? replacement : code);
        }

        matcher.appendTail(sb);

        return sb.toString();
    }

    private static String getRegex(boolean isIATA, boolean isReplacementForCityName) {
        String regex;
        if (isReplacementForCityName) {
            regex = isIATA ? "\\*#(\\w{3})" : "\\*##(\\w{4})";
        } else {
            regex = isIATA ? "#(\\w{3})" : "##(\\w{4})";
        }
        return regex;
    }
}
