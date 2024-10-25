package text_transform;

public class WhitespaceCleaner {
    public String cleanWhitespace(String input) {
        if (input == null) {
            return null;
        }

        String cleaned;

        // Replace vertical whitespace, form feeds, and carriage returns with a newline
        cleaned = input.replaceAll("[\\v\\f\\r]+", "\n");

        // Replace multiple spaces or tabs with a single space
        cleaned = cleaned.replaceAll("[ \t]+", " ");

        // Replace multiple newlines (more than one) with exactly two newlines
        cleaned = cleaned.replaceAll("(?m)(\\n\\s*){2,}", "\n\n");

        // Remove any leading or trailing whitespace
        cleaned = cleaned.trim();

        // Remove leading newlines at the beginning of the text
        cleaned = cleaned.replaceAll("(?m)^\n+", "");

        return cleaned;
    }
}
