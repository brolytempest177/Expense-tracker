package com.expensetracker.model;

import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.util.Date;

/**
 * Budget model representing a monthly spending budget set by a user.
 * Stored in Firestore sub-collection: "users/{userId}/budgets/{budgetId}"
 */
public class Budget implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String userId;
    private int month;   // 1 - 12
    private int year;    // e.g. 2026
    private double amount;
    private Date updatedAt;

    // Default constructor for Firestore
    public Budget() {
    }

    public Budget(String id, String userId, int month, int year, double amount, Date updatedAt) {
        this.id = id;
        this.userId = userId;
        this.month = month;
        this.year = year;
        this.amount = amount;
        this.updatedAt = updatedAt != null ? updatedAt : new Date();
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

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Returns full month name (e.g., "January", "September").
     */
    public String getMonthName() {
        if (month >= 1 && month <= 12) {
            return new DateFormatSymbols().getMonths()[month - 1];
        }
        return "Unknown";
    }

    public String getFormattedAmount() {
        return String.format("%.2f", amount);
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id='" + id + '\'' +
                ", userId='" + userId + '\'' +
                ", month=" + month +
                ", year=" + year +
                ", amount=" + amount +
                '}';
    }
}
