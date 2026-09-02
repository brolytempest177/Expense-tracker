package com.expensetracker.service;

import com.expensetracker.dao.BudgetDAO;
import com.expensetracker.model.Budget;
import com.expensetracker.util.ValidationUtil;

import java.util.Date;
import java.util.List;

/**
 * Service class handling Budget business logic and budget tracking status.
 */
public class BudgetService {
    private final BudgetDAO budgetDAO;

    public BudgetService() {
        this.budgetDAO = new BudgetDAO();
    }

    public BudgetService(BudgetDAO budgetDAO) {
        this.budgetDAO = budgetDAO;
    }

    /**
     * Sets or updates monthly budget for a user.
     */
    public Budget setBudget(String userId, int month, int year, double amount) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User session is required.");
        }
        if (!ValidationUtil.isValidMonth(month)) {
            throw new IllegalArgumentException("Month must be between 1 and 12.");
        }
        if (!ValidationUtil.isValidYear(year)) {
            throw new IllegalArgumentException("Year must be between 2000 and 2100.");
        }
        if (amount < 0) {
            throw new IllegalArgumentException("Budget amount cannot be negative.");
        }

        Budget budget = new Budget(year + "-" + month, userId, month, year, amount, new Date());
        boolean saved = budgetDAO.setOrUpdateBudget(budget);
        if (!saved) {
            throw new IllegalStateException("Failed to update budget. Please check database connection.");
        }
        return budget;
    }

    /**
     * Retrieves monthly budget for a user.
     */
    public Budget getBudget(String userId, int month, int year) {
        if (userId == null) return null;
        return budgetDAO.getBudget(userId, month, year);
    }

    /**
     * Retrieves all recorded budgets for a user.
     */
    public List<Budget> getAllBudgets(String userId) {
        if (userId == null) return List.of();
        return budgetDAO.getAllBudgets(userId);
    }

    /**
     * Helper DTO class representing the current status of a budget against expenditures.
     */
    public static class BudgetStatus {
        private final double budgetAmount;
        private final double spentAmount;
        private final double remainingAmount;
        private final double percentageUsed;
        private final String statusLevel; // "SAFE", "WARNING", "EXCEEDED", "NO_BUDGET"

        public BudgetStatus(double budgetAmount, double spentAmount) {
            this.budgetAmount = budgetAmount;
            this.spentAmount = spentAmount;

            if (budgetAmount > 0) {
                this.remainingAmount = budgetAmount - spentAmount;
                this.percentageUsed = Math.min((spentAmount / budgetAmount) * 100.0, 100.0);
                if (spentAmount > budgetAmount) {
                    this.statusLevel = "EXCEEDED";
                } else if (spentAmount >= (0.80 * budgetAmount)) {
                    this.statusLevel = "WARNING";
                } else {
                    this.statusLevel = "SAFE";
                }
            } else {
                this.remainingAmount = -spentAmount;
                this.percentageUsed = 0.0;
                this.statusLevel = "NO_BUDGET";
            }
        }

        public double getBudgetAmount() {
            return budgetAmount;
        }

        public double getSpentAmount() {
            return spentAmount;
        }

        public double getRemainingAmount() {
            return remainingAmount;
        }

        public double getPercentageUsed() {
            return percentageUsed;
        }

        public String getStatusLevel() {
            return statusLevel;
        }

        public String getFormattedBudget() {
            return String.format("%.2f", budgetAmount);
        }

        public String getFormattedSpent() {
            return String.format("%.2f", spentAmount);
        }

        public String getFormattedRemaining() {
            return String.format("%.2f", remainingAmount);
        }

        public String getFormattedPercentage() {
            return String.format("%.1f", percentageUsed);
        }
    }

    public BudgetStatus computeBudgetStatus(String userId, int month, int year, double totalSpent) {
        Budget budget = getBudget(userId, month, year);
        double budgetAmt = budget != null ? budget.getAmount() : 0.0;
        return new BudgetStatus(budgetAmt, totalSpent);
    }
}
