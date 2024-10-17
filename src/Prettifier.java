import cli.ArgumentHelper;
import cli.ArgumentValidator;
import cli.UsageHelper;
import fileio.CSVReader;
import fileio.InputFileReader;
import fileio.OutputWriter;
import text_transform.TextFormatter;
import text_transform.WhitespaceCleaner; // Импортируем WhitespaceCleaner
import text_transform.AirportCodeConverter;

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
        WhitespaceCleaner whitespaceCleaner = new WhitespaceCleaner(); // Создаем экземпляр WhitespaceCleaner
        AirportCodeConverter airportCodeConverter = null; // Объявляем AirportCodeConverter

        try {
            csvReader.readAirportData(airportLookUpPath);
            airportCodeConverter = new AirportCodeConverter(csvReader.getAirportData()); // Инициализируем AirportCodeConverter

            InputFileReader inputFileReader = new InputFileReader();
            inputFileReader.readInputData(inputFilePath);

            List<String> formattedInputData = new ArrayList<>();
            for (String line : inputFileReader.getInputData()) {
                String cleanedLine = whitespaceCleaner.cleanWhitespace(line); // Очищаем пробелы
                String formattedLine = textFormatter.formatText(cleanedLine);

                // Заменяем IATA коды
                formattedLine = replaceAirportCodes(formattedLine, airportCodeConverter, true); // Заменяем IATA
                // Заменяем ICAO коды
                formattedLine = replaceAirportCodes(formattedLine, airportCodeConverter, false); // Заменяем ICAO

                formattedInputData.add(formattedLine); // Добавляем отформатированную строку
            }

            OutputWriter outputWriter = new OutputWriter();
            outputWriter.writeToFile(formattedInputData, outputFilePath); // Пишем отформатированные данные в выходной файл

        } catch (IOException e) {
            System.err.println("Error in reading file. " + e.getMessage());

            UsageHelper.printUsage();
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.err.println("Error in data. " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error. " + e.getMessage());
            System.exit(1);
        }
    }

    private static String replaceAirportCodes(String line, AirportCodeConverter converter, boolean isIATA) {
        // Определяем регулярное выражение для IATA и ICAO кодов
        String regex = isIATA ? "#(\\w{3})" : "##(\\w{4})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String code = matcher.group(1);
            String replacement = isIATA ? converter.convertIATACode(code) : converter.convertICAOCode(code);
            matcher.appendReplacement(sb, replacement != null ? replacement : matcher.group(0)); // Если замена null, оставляем оригинал
        }
        matcher.appendTail(sb); // Завершаем замену

        return sb.toString();
    }
}
