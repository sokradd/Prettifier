package fileio;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InputFileReader {
    private List<String> inputData = new ArrayList<>();

    public void readInputData(String inputFilePath) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                inputData.add(line.trim());
            }
        } catch (IOException e) {
            throw new IOException("Input file malformed. " + e.getMessage());
        }
    }

    public List<String> getInputData() {
        return inputData;
    }
}
