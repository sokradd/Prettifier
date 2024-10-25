package fileio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {
    private Map<String, AirportInfo> airportData = new HashMap<>();

    // Columns we need
    private static final String NAME_HEADER = "name";
    private static final String ISO_COUNTRY_HEADER = "iso_country";
    private static final String MUNICIPALITY_HEADER = "municipality";
    private static final String ICAO_CODE_HEADER = "icao_code";
    private static final String IATA_CODE_HEADER = "iata_code";
    private static final String COORDINATES_HEADER = "coordinates";

    private boolean isMalformed = false; // Flag for checking if there are any errors

    // Method to read airport data from a CSV file
    public void readAirportData(String airportLookUpPath) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(airportLookUpPath))) {
            String line;

            // Reading the header
            String headerLine = bufferedReader.readLine();
            String[] headers = parseCSVLine(headerLine);

            // Check if all required headers are present
            Map<String, Integer> columnIndices = getColumnIndices(headers);
            validateColumnIndices(columnIndices);

            // Reading the data
            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = parseCSVLine(line);

                // Check for empty cells in the row
                if (columns.length < headers.length || isRowMalformed(columns)) {
                    System.err.println("Malformed data: missing values in line: " + line);
                    isMalformed = true; // If the row is malformed, set the flag
                    continue;
                }

                // Extract data based on column indices
                String name = columns[columnIndices.get(NAME_HEADER)].trim();
                String isoCountry = columns[columnIndices.get(ISO_COUNTRY_HEADER)].trim();
                String municipality = columns[columnIndices.get(MUNICIPALITY_HEADER)].trim();
                String icaoCode = columns[columnIndices.get(ICAO_CODE_HEADER)].trim();
                String iataCode = columns[columnIndices.get(IATA_CODE_HEADER)].trim();
                String coordinates = columns[columnIndices.get(COORDINATES_HEADER)].trim();

                // Check if both IATA and ICAO codes are missing
                if (iataCode.isEmpty() && icaoCode.isEmpty()) {
                    System.err.println("Error: both IATA and ICAO codes are missing for airport: " + name);
                    continue;
                }

                // Add airport data to the map (for both IATA and ICAO codes)
                if (!iataCode.isEmpty()) {
                    airportData.put(iataCode, new AirportInfo(name, isoCountry, municipality, icaoCode, iataCode, coordinates));
                }

                if (!icaoCode.isEmpty()) {
                    airportData.put(icaoCode, new AirportInfo(name, isoCountry, municipality, icaoCode, iataCode, coordinates));
                }
            }

            if (isMalformed) {
                throw new IllegalArgumentException("Airport lookup malformed. Missing values found.");
            }
        } catch (IOException e) {
            throw new IOException("Error reading airport lookup file");
        }
    }

    // Method to map headers to column indices
    private Map<String, Integer> getColumnIndices(String[] headers) {
        Map<String, Integer> columnIndices = new HashMap<>();

        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].trim().toLowerCase();
            switch (header) {
                case "name":
                    columnIndices.put(NAME_HEADER, i);
                    break;
                case "iso_country":
                    columnIndices.put(ISO_COUNTRY_HEADER, i);
                    break;
                case "municipality":
                    columnIndices.put(MUNICIPALITY_HEADER, i);
                    break;
                case "icao_code":
                    columnIndices.put(ICAO_CODE_HEADER, i);
                    break;
                case "iata_code":
                    columnIndices.put(IATA_CODE_HEADER, i);
                    break;
                case "coordinates":
                    columnIndices.put(COORDINATES_HEADER, i);
                    break;
                default:
                    // Unknown header — skip
                    break;
            }
        }
        return columnIndices;
    }

    // Method to check for empty cells in a row
    private boolean isRowMalformed(String[] columns) {
        for (String column : columns) {
            if (column.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Method to ensure that all required headers are present
    private void validateColumnIndices(Map<String, Integer> columnIndices) {
        if (!columnIndices.containsKey(NAME_HEADER) ||
                !columnIndices.containsKey(ISO_COUNTRY_HEADER) ||
                !columnIndices.containsKey(MUNICIPALITY_HEADER) ||
                !columnIndices.containsKey(ICAO_CODE_HEADER) ||
                !columnIndices.containsKey(IATA_CODE_HEADER) ||
                !columnIndices.containsKey(COORDINATES_HEADER)) {
            throw new IllegalArgumentException("Airport lookup malformed. Missing columns.");
        }
    }

    // Simple CSV parser that handles quoted fields
    private String[] parseCSVLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    public Map<String, AirportInfo> getAirportData() {
        return airportData;
    }

    public boolean isMalformed() {
        return isMalformed;
    }

    // Class to store airport information
    public class AirportInfo {
        private String name;
        private String isoCode;
        private String municipality;
        private String icaoCode;
        private String iataCode;
        private String coordinates;

        public AirportInfo(String name, String isoCode, String municipality, String icaoCode, String iataCode, String coordinates) {
            this.name = name;
            this.isoCode = isoCode;
            this.municipality = municipality;
            this.icaoCode = icaoCode;
            this.iataCode = iataCode;
            this.coordinates = coordinates;
        }

        public String getName() {
            return name;
        }

        public String getIsoCode() {
            return isoCode;
        }

        public String getMunicipality() {
            return municipality;
        }

        public String getIcaoCode() {
            return icaoCode;
        }

        public String getIataCode() {
            return iataCode;
        }

        public String getCoordinates() {
            return coordinates;
        }
    }
}
