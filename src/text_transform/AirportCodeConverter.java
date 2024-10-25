package text_transform;

import fileio.CSVReader;

import java.util.Map;

public class AirportCodeConverter {
    private Map<String, CSVReader.AirportInfo> airportData;

    // Constructor to initialize the converter with a map of airport data
    public AirportCodeConverter(Map<String, CSVReader.AirportInfo> airportData) {
        this.airportData = airportData;
    }

    // Method to convert an IATA code to the airport name
    public String convertIATACode(String iataCode) {
        CSVReader.AirportInfo airportInfo = airportData.get(iataCode);
        if (airportInfo == null) {
            System.err.println("IATA code not found: " + iataCode);  // Print an error if the IATA code is not found
        }
        return airportInfo != null ? airportInfo.getName() : iataCode;  // Return airport name if found, otherwise return the IATA code
    }

    // Method to convert an ICAO code to the airport name
    public String convertICAOCode(String icaoCode) {
        CSVReader.AirportInfo airportInfo = airportData.get(icaoCode);
        if (airportInfo == null) {
            System.err.println("ICAO code not found: " + icaoCode);  // Print an error if the ICAO code is not found
        }
        return airportInfo != null ? airportInfo.getName() : icaoCode;  // Return airport name if found, otherwise return the ICAO code
    }

    // Method to convert a city code to the airport's municipality (city name)
    public String convertCityName(String cityCode) {
        // Check if the city code starts with '*' and remove it if it does
        String code = cityCode.startsWith("*") ? cityCode.substring(1) : cityCode;
        CSVReader.AirportInfo airportInfo = airportData.get(code);
        if (airportInfo == null) {
            System.err.println("City code not found: " + cityCode);  // Print an error if the city code is not found
        }
        return airportInfo != null ? airportInfo.getMunicipality() : cityCode;  // Return municipality name if found, otherwise return the city code
    }
}
