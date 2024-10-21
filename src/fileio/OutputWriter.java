package fileio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class OutputWriter {
    public void writeToFile(Iterable<String> data, String outputFilePath) {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (String line : data) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to output file. " + e.getMessage());
        }
    }
}
