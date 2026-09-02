package com.expensetracker.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Category helper providing default expense categories, colors, and icons.
 */
public class Category implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String FOOD = "Food";
    public static final String TRANSPORT = "Transport";
    public static final String EDUCATION = "Education";
    public static final String SHOPPING = "Shopping";
    public static final String ENTERTAINMENT = "Entertainment";
    public static final String BILLS = "Bills";
    public static final String HEALTH = "Health";
    public static final String OTHER = "Other";

    public static final List<String> ALL_CATEGORIES = Arrays.asList(
            FOOD,
            TRANSPORT,
            EDUCATION,
            SHOPPING,
            ENTERTAINMENT,
            BILLS,
            HEALTH,
            OTHER
    );

    /**
     * Get a representative color for the category (used in charts & badges)
     */
    public static String getColor(String category) {
        if (category == null) return "#64748b";
        switch (category) {
            case FOOD:
                return "#f59e0b"; // Amber/Orange
            case TRANSPORT:
                return "#3b82f6"; // Blue
            case EDUCATION:
                return "#8b5cf6"; // Purple
            case SHOPPING:
                return "#ec4899"; // Pink
            case ENTERTAINMENT:
                return "#10b981"; // Emerald/Green
            case BILLS:
                return "#ef4444"; // Red
            case HEALTH:
                return "#06b6d4"; // Cyan
            case OTHER:
            default:
                return "#64748b"; // Slate Gray
        }
    }

    /**
     * Get an icon class name or emoji for the category
     */
    public static String getIcon(String category) {
        if (category == null) return "📌";
        switch (category) {
            case FOOD:
                return "🍔";
            case TRANSPORT:
                return "🚗";
            case EDUCATION:
                return "📚";
            case SHOPPING:
                return "🛍️";
            case ENTERTAINMENT:
                return "🎬";
            case BILLS:
                return "💡";
            case HEALTH:
                return "💊";
            case OTHER:
            default:
                return "🏷️";
        }
    }
}
