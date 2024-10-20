package text_transform;

public class WhitespaceCleaner {
    public String cleanWhitespace(String input) {
        if (input == null) {
            return null;
        }

        String cleaned;

        cleaned = input.replaceAll("[\\v\\f\\r]+", "\n");

        cleaned = cleaned.replaceAll("[ \t]+", " ");

        cleaned = cleaned.replaceAll("(?m)(\\n\\s*){2,}", "\n\n");

        cleaned = cleaned.trim();

        return cleaned;
    }
}
