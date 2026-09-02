package com.expensetracker.service;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.google.gson.Gson;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for computing dashboard statistics, monthly summaries, and report analytics.
 */
public class ReportService {
    private final ExpenseDAO expenseDAO;
    private final Gson gson;

    public ReportService() {
        this.expenseDAO = new ExpenseDAO();
        this.gson = new Gson();
    }

    public ReportService(ExpenseDAO expenseDAO) {
        this.expenseDAO = expenseDAO;
        this.gson = new Gson();
    }

    /**
     * DTO containing all monthly financial analytics.
     */
    public static class MonthlyAnalytics {
        private final int month;
        private final int year;
        private final double totalSpent;
        private final int transactionCount;
        private final double averageTransaction;
        private final Expense highestExpense;
        private final Map<String, Double> categoryTotals;
        private final Map<String, Double> dailyTotals;
        private final List<Expense> expenses;

        // JSON strings for Chart.js
        private String categoryLabelsJson;
        private String categoryDataJson;
        private String categoryColorsJson;
        private String trendLabelsJson;
        private String trendDataJson;

        public MonthlyAnalytics(int month, int year, double totalSpent, int transactionCount,
                                double averageTransaction, Expense highestExpense,
                                Map<String, Double> categoryTotals, Map<String, Double> dailyTotals,
                                List<Expense> expenses) {
            this.month = month;
            this.year = year;
            this.totalSpent = totalSpent;
            this.transactionCount = transactionCount;
            this.averageTransaction = averageTransaction;
            this.highestExpense = highestExpense;
            this.categoryTotals = categoryTotals;
            this.dailyTotals = dailyTotals;
            this.expenses = expenses;
        }

        public int getMonth() { return month; }
        public int getYear() { return year; }
        public double getTotalSpent() { return totalSpent; }
        public int getTransactionCount() { return transactionCount; }
        public double getAverageTransaction() { return averageTransaction; }
        public Expense getHighestExpense() { return highestExpense; }
        public Map<String, Double> getCategoryTotals() { return categoryTotals; }
        public Map<String, Double> getDailyTotals() { return dailyTotals; }
        public List<Expense> getExpenses() { return expenses; }

        public String getCategoryLabelsJson() { return categoryLabelsJson; }
        public void setCategoryLabelsJson(String categoryLabelsJson) { this.categoryLabelsJson = categoryLabelsJson; }

        public String getCategoryDataJson() { return categoryDataJson; }
        public void setCategoryDataJson(String categoryDataJson) { this.categoryDataJson = categoryDataJson; }

        public String getCategoryColorsJson() { return categoryColorsJson; }
        public void setCategoryColorsJson(String categoryColorsJson) { this.categoryColorsJson = categoryColorsJson; }

        public String getTrendLabelsJson() { return trendLabelsJson; }
        public void setTrendLabelsJson(String trendLabelsJson) { this.trendLabelsJson = trendLabelsJson; }

        public String getTrendDataJson() { return trendDataJson; }
        public void setTrendDataJson(String trendDataJson) { this.trendDataJson = trendDataJson; }

        public String getFormattedTotalSpent() { return String.format("%.2f", totalSpent); }
        public String getFormattedAverage() { return String.format("%.2f", averageTransaction); }
    }

    /**
     * Computes analytics for a specific month and year.
     */
    public MonthlyAnalytics getMonthlyAnalytics(String userId, int month, int year) {
        List<Expense> monthlyExpenses = expenseDAO.getExpensesByMonthAndYear(userId, month, year);

        double totalSpent = 0.0;
        Expense highestExpense = null;
        double maxAmount = -1.0;

        Map<String, Double> categoryMap = new LinkedHashMap<>();
        // Initialize all categories with 0
        for (String cat : Category.ALL_CATEGORIES) {
            categoryMap.put(cat, 0.0);
        }

        Map<String, Double> dailyMap = new TreeMap<>();

        for (Expense exp : monthlyExpenses) {
            double amt = exp.getAmount();
            totalSpent += amt;

            // Check highest
            if (amt > maxAmount) {
                maxAmount = amt;
                highestExpense = exp;
            }

            // Category breakdown
            String cat = exp.getCategory();
            if (cat != null) {
                categoryMap.put(cat, categoryMap.getOrDefault(cat, 0.0) + amt);
            }

            // Daily trend
            String date = exp.getDate();
            if (date != null) {
                dailyMap.put(date, dailyMap.getOrDefault(date, 0.0) + amt);
            }
        }

        int count = monthlyExpenses.size();
        double avg = count > 0 ? (totalSpent / count) : 0.0;

        MonthlyAnalytics analytics = new MonthlyAnalytics(
                month, year, totalSpent, count, avg, highestExpense, categoryMap, dailyMap, monthlyExpenses
        );

        // Prepare JSON for Chart.js
        // Filter out zero categories for doughnut chart cleanly, or include active ones
        List<String> activeCats = new ArrayList<>();
        List<Double> activeAmounts = new ArrayList<>();
        List<String> activeColors = new ArrayList<>();

        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            if (entry.getValue() > 0) {
                activeCats.add(entry.getKey());
                activeAmounts.add(entry.getValue());
                activeColors.add(Category.getColor(entry.getKey()));
            }
        }

        analytics.setCategoryLabelsJson(gson.toJson(activeCats));
        analytics.setCategoryDataJson(gson.toJson(activeAmounts));
        analytics.setCategoryColorsJson(gson.toJson(activeColors));

        // Daily trend JSON
        List<String> days = new ArrayList<>(dailyMap.keySet());
        List<Double> dayAmounts = new ArrayList<>();
        for (String day : days) {
            dayAmounts.add(dailyMap.get(day));
        }

        analytics.setTrendLabelsJson(gson.toJson(days));
        analytics.setTrendDataJson(gson.toJson(dayAmounts));

        return analytics;
    }
}
