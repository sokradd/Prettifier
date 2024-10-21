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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Prettifier {
    public static void main(String[] args) {

        ArgumentHelper argumentHelper = new ArgumentHelper();
        argumentHelper.parseArgument(args);

        String inputFilePath = argumentHelper.getInputFilePath();
        String outputFilePath = argumentHelper.getOutputFilePath();
        String airportLookUpPath = argumentHelper.getAirportLookUpPath();

        if (!ArgumentValidator.isInputFileExists(inputFilePath)) {
            return;
        }
        if (!ArgumentValidator.isOutputFileExists(outputFilePath)) {
            return;
        }
        if (!ArgumentValidator.isAirportFileExists(airportLookUpPath)) {
            return;
        }

        CSVReader csvReader = new CSVReader();
        TextFormatter textFormatter = new TextFormatter();
        WhitespaceCleaner whitespaceCleaner = new WhitespaceCleaner();
        AirportCodeConverter airportCodeConverter = null;

        try {
            csvReader.readAirportData(airportLookUpPath);
            airportCodeConverter = new AirportCodeConverter(csvReader.getAirportData());

            InputFileReader inputFileReader = new InputFileReader();
            inputFileReader.readInputData(inputFilePath);

            List<String> formattedInputData = new ArrayList<>();
            for (String line : inputFileReader.getInputData()) {
                String cleanedLine = whitespaceCleaner.cleanWhitespace(line);
                String formattedLine = textFormatter.formatText(cleanedLine);


                formattedLine = replaceAirportCodes(formattedLine, airportCodeConverter, false);
                formattedLine = replaceAirportCodes(formattedLine, airportCodeConverter, true);

                formattedInputData.add(formattedLine);
            }

            OutputWriter outputWriter = new OutputWriter();
            outputWriter.writeToFile(formattedInputData, outputFilePath);

        } catch (IOException e) {
            System.err.println(Color.ANSI_RED + "Error in reading file. " + e.getMessage() + Color.ANSI_RESET );

            UsageHelper.printUsage();
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println(Color.ANSI_RED + "Error in data. " + e.getMessage() + Color.ANSI_RESET);
            System.exit(1);
        } catch (Exception e) {
            System.err.println(Color.ANSI_RED + "Unexpected error. " + e.getMessage() + Color.ANSI_RESET );
            System.exit(1);
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
