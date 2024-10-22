package text_transform;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFormatter {

    private static final Pattern DATE_PATTERN = Pattern.compile("D\\((\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2})([+-]\\d{2}:\\d{2}|Z)\\)");
    private static final Pattern TIME_12_PATTERN = Pattern.compile("T12\\((\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2})([+-]\\d{2}:\\d{2}|Z)\\)");
    private static final Pattern TIME_24_PATTERN = Pattern.compile("T24\\((\\d{4}-\\d{2}-\\d{2})T(\\d{2}:\\d{2})([+-]\\d{2}:\\d{2}|Z)\\)");

    public String formatText(String text) {
        text = replaceDates(text);
        text = replace12HourTime(text);
        text = replace24HourTime(text);
        return text;
    }

    private String replaceDates(String text) {
        Matcher matcher = DATE_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String isoDate = matcher.group(1);
            String time = matcher.group(2);
            String offset = matcher.group(3);

            // Check for valid date, time, and offset
            if (!isValidDate(isoDate) || !isValidOffset(offset) || !isValidTime(time)) {
                matcher.appendReplacement(result, matcher.group(0)); // Keep the original text
            } else {
                String formattedDate = formatDate(isoDate);
                matcher.appendReplacement(result, formattedDate);
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String replace12HourTime(String text) {
        Matcher matcher = TIME_12_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String isoDate = matcher.group(1);
            String time = matcher.group(2);
            String offset = matcher.group(3);

            // Check for valid date, time, and offset
            if (!isValidDate(isoDate) || !isValidOffset(offset) || !isValidTime(time)) {
                matcher.appendReplacement(result, matcher.group(0)); // Keep the original text
            } else {
                String formattedTime = format12HourTime(time, offset);
                matcher.appendReplacement(result, formattedTime);
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String replace24HourTime(String text) {
        Matcher matcher = TIME_24_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String isoDate = matcher.group(1);
            String time = matcher.group(2);
            String offset = matcher.group(3);

            // Check for valid date, time, and offset
            if (!isValidDate(isoDate) || !isValidOffset(offset) || !isValidTime(time)) {
                matcher.appendReplacement(result, matcher.group(0)); // Keep the original text
            } else {
                String formattedTime = format24HourTime(time, offset);
                matcher.appendReplacement(result, formattedTime);
            }
        }
        matcher.appendTail(result);
        return result.toString();
    }

    private String formatDate(String isoDate) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            inputFormat.setLenient(false); // Strict date validation
            Date date = inputFormat.parse(isoDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
            return outputFormat.format(date);
        } catch (Exception e) {
            return isoDate; // In case of error, return the original text
        }
    }

    private String format12HourTime(String time, String offset) {
        String[] parts = time.split(":");
        int hour = Integer.parseInt(parts[0]);
        String ampm = hour >= 12 ? "PM" : "AM";
        hour = hour % 12 == 0 ? 12 : hour % 12;

        return String.format("%02d:%s%s", hour, parts[1], ampm) + " " + formatOffset(offset);
    }

    private String format24HourTime(String time, String offset) {
        return time + " " + formatOffset(offset);
    }

    private String formatOffset(String offset) {
        return offset.equals("Z") ? "(+00:00)" : "(" + offset + ")";
    }

    // Check for valid time
    private boolean isValidTime(String time) {
        String[] parts = time.split(":");
        if (parts.length != 2) return false;

        int hour = Integer.parseInt(parts[0]);
        int minute = Integer.parseInt(parts[1]);

        return (hour >= 0 && hour < 24) && (minute >= 0 && minute < 60);
    }

    // Check for valid time offset
    private boolean isValidOffset(String offset) {
        return offset.matches("[+-](\\d{2}:\\d{2})|Z");
    }

    // Check for valid date
    private boolean isValidDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            inputFormat.setLenient(false); // Strict date validation
            inputFormat.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
