package text_transform;

public class BlankSpaceCleaner {

    /**
     * Removes unnecessary blank lines, leaving no more than one between blocks of text.
     *
     * @param input Input text
     * @return Text without unnecessary blank lines
     */
    public String cleanBlankSpaces(String input) {
        if (input == null) {
            return null;
        }

        // Split the text into lines
        String[] lines = input.split("\n");
        StringBuilder cleanedText = new StringBuilder();

        boolean previousLineWasEmpty = false;

        for (String line : lines) {
            // Trim spaces around the line
            line = line.trim();

            // If the line is not empty
            if (!line.isEmpty()) {
                if (cleanedText.length() > 0) {
                    cleanedText.append("\n"); // Add a new line before non-empty lines
                }
                cleanedText.append(line);
                previousLineWasEmpty = false; // Reset the flag since the line is not empty
            } else if (!previousLineWasEmpty) {
                // Add one empty line if the previous line was not empty
                cleanedText.append("\n");
                previousLineWasEmpty = true; // Set the flag that this line is empty
            }
        }

        return cleanedText.toString();
    }
}
