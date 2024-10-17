package fileio;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {
    private Map<String, AirportInfo> airportData = new HashMap<>();

    private static final String[] REQUIRED_HEADERS = {
            "name", "iso_country", "municipality", "icao_code", "iata_code", "coordinates"
    };

    public void readAirportData(String airportLookUpPath) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(airportLookUpPath))) {
            String line;

            String headerLine = bufferedReader.readLine();
            if (!validateHeaders(headerLine)) {
                throw new IllegalArgumentException("Invalid CSV header: " + headerLine);
            }

            while ((line = bufferedReader.readLine()) != null) {
                String[] columns = line.split(",");

                if (columns.length < REQUIRED_HEADERS.length) {
                    System.err.println("Malformed data: " + line);
                    continue;
                }

                String name = columns[0].trim();
                String iso_country = columns[1].trim();
                String municipality = columns[2].trim();
                String icao_code = columns[3].trim();
                String iata_code = columns[4].trim();
                String coordinates = columns[5].trim();

                if (!iata_code.isEmpty()) {
                    airportData.put(iata_code, new AirportInfo(name, iso_country, municipality, icao_code, iata_code, coordinates));
                } else if (!icao_code.isEmpty()) {
                    airportData.put(icao_code, new AirportInfo(name, iso_country, municipality, icao_code, iata_code, coordinates));
                } else {
                    System.err.println("Error: IATA or ICAO code is required for airport: " + name);
                }
            }
        } catch (IOException e) {
            throw new IOException("Error reading airport lookup file: " + e.getMessage());
        }
    }

    private boolean validateHeaders(String headerLine) {
        String[] headers = headerLine.split(",");
        for (String requiredHeader : REQUIRED_HEADERS) {
            boolean found = false;
            for (String header : headers) {
                if (header.trim().equalsIgnoreCase(requiredHeader)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    public Map<String, AirportInfo> getAirportData() {
        return airportData;
    }

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

        public String getName() { return name; }
        public String getIsoCode() { return isoCode; }
        public String getMunicipality() { return municipality; }
        public String getIcaoCode() { return icaoCode; }
        public String getIataCode() { return iataCode; }
        public String getCoordinates() { return coordinates; }
    }
}
