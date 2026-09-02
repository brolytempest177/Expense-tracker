package com.expensetracker.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Expense model representing an expense transaction.
 * Stored in Firestore sub-collection: "users/{userId}/expenses/{expenseId}"
 */
public class Expense implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private double amount;
    private String description;
    private String category;
    private String date; // Format: YYYY-MM-DD
    private String notes;
    private Date createdAt;

    // Default no-arg constructor required for Firestore
    public Expense() {
    }

    public Expense(String id, String userId, double amount, String description, String category, String date, String notes, Date createdAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.description = description;
        this.category = category;
        this.date = date;
        this.notes = notes;
        this.createdAt = createdAt != null ? createdAt : new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Helper to extract month (1-12) from date string (YYYY-MM-DD).
     */
    public int getMonth() {
        if (date != null && date.length() >= 7) {
            try {
                String[] parts = date.split("-");
                if (parts.length >= 2) {
                    return Integer.parseInt(parts[1]);
                }
            } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    /**
     * Helper to extract year from date string (YYYY-MM-DD).
     */
    public int getYear() {
        if (date != null && date.length() >= 4) {
            try {
                String[] parts = date.split("-");
                if (parts.length >= 1) {
                    return Integer.parseInt(parts[0]);
                }
            } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    /**
     * Formats amount with two decimal places.
     */
    public String getFormattedAmount() {
        return String.format("%.2f", amount);
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
