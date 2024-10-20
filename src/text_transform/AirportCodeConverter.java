package text_transform;

import fileio.CSVReader;

import java.util.Map;

public class AirportCodeConverter {
    private Map<String, CSVReader.AirportInfo> airportData;

    public AirportCodeConverter(Map<String, CSVReader.AirportInfo> airportData) {
        this.airportData = airportData;
    }

    public String convertIATACode(String iataCode) {
        CSVReader.AirportInfo airportInfo = airportData.get(iataCode);
        return airportInfo != null ? airportInfo.getName() : iataCode;
    }

    public String convertICAOCode(String icaoCode) {
        CSVReader.AirportInfo airportInfo = airportData.get(icaoCode);
        return airportInfo != null ? airportInfo.getName() : icaoCode;
    }

    public String convertCityName(String cityCode) {
        String code = cityCode.startsWith("*") ? cityCode.substring(1) : cityCode;
        CSVReader.AirportInfo airportInfo = airportData.get(code);
        return airportInfo != null ? airportInfo.getMunicipality() : cityCode;
    }
}
