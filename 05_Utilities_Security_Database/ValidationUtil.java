package com.expensetracker.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Server-side input validation and sanitization utility.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ValidationUtil() {}

    /**
     * Validates email format.
     */
    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates password strength (minimum 6 characters).
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.trim().length() >= 6;
    }

    /**
     * Checks if a string represents a positive double amount.
     */
    public static boolean isValidAmount(String amountStr) {
        if (amountStr == null || amountStr.trim().isEmpty()) return false;
        try {
            double amount = Double.parseDouble(amountStr.trim());
            return amount > 0 && !Double.isNaN(amount) && !Double.isInfinite(amount);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Parses a string to a positive double amount, or returns -1 if invalid.
     */
    public static double parseAmount(String amountStr) {
        if (!isValidAmount(amountStr)) return -1;
        return Double.parseDouble(amountStr.trim());
    }

    /**
     * Validates date format YYYY-MM-DD and ensures it is a valid calendar date.
     */
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return false;
        try {
            LocalDate.parse(dateStr.trim(), DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates month integer (1 to 12).
     */
    public static boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }

    /**
     * Validates year integer (2000 to 2100).
     */
    public static boolean isValidYear(int year) {
        return year >= 2000 && year <= 2100;
    }

    /**
     * Sanitizes strings to prevent XSS.
     */
    public static String sanitize(String input) {
        if (input == null) return "";
        return input.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;")
                .trim();
    }
}
