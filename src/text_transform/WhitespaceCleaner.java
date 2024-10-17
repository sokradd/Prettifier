package text_transform;

public class WhitespaceCleaner {
    public String cleanWhitespace(String input) {
        if (input == null) {
            return null;
        }

        String cleaned = input.replaceAll("[\\v\\f\\r]+", "\n");

        cleaned = cleaned.trim();

        cleaned = cleaned.replaceAll("(?m)(\\n\\s*){2,}", "\n\n");

        return cleaned;
    }
}
