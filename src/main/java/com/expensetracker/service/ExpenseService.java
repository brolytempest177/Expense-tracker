package com.expensetracker.service;

import com.expensetracker.dao.ExpenseDAO;
import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.util.ValidationUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class handling Expense business logic, filtering, searching, and sorting.
 */
public class ExpenseService {
    private final ExpenseDAO expenseDAO;

    public ExpenseService() {
        this.expenseDAO = new ExpenseDAO();
    }

    public ExpenseService(ExpenseDAO expenseDAO) {
        this.expenseDAO = expenseDAO;
    }

    /**
     * Adds an expense with validation.
     */
    public Expense addExpense(String userId, double amount, String description, String category, String date, String notes) {
        validateExpenseInputs(userId, amount, description, category, date);

        String expenseId = UUID.randomUUID().toString();
        Expense expense = new Expense(
                expenseId,
                userId,
                amount,
                description.trim(),
                category.trim(),
                date.trim(),
                notes != null ? notes.trim() : "",
                new Date()
        );

        boolean created = expenseDAO.addExpense(expense);
        if (!created) {
            throw new IllegalStateException("Failed to save expense. Please check your database connection.");
        }
        return expense;
    }

    /**
     * Retrieves an expense ensuring user ownership.
     */
    public Expense getExpense(String userId, String expenseId) {
        if (userId == null || expenseId == null) return null;
        return expenseDAO.getExpenseById(userId, expenseId);
    }

    /**
     * Retrieves all expenses for a user.
     */
    public List<Expense> getAllExpenses(String userId) {
        if (userId == null) return Collections.emptyList();
        return expenseDAO.getAllExpensesByUser(userId);
    }

    /**
     * Filters, searches, and sorts expenses for a user.
     */
    public List<Expense> getFilteredExpenses(String userId, String query, String category,
                                             String startDate, String endDate, String sortBy) {
        List<Expense> expenses = getAllExpenses(userId);

        // 1. Text Search Filter (in description or notes)
        if (query != null && !query.trim().isEmpty()) {
            String lowerQuery = query.toLowerCase().trim();
            expenses = expenses.stream()
                    .filter(e -> (e.getDescription() != null && e.getDescription().toLowerCase().contains(lowerQuery))
                            || (e.getNotes() != null && e.getNotes().toLowerCase().contains(lowerQuery)))
                    .collect(Collectors.toList());
        }

        // 2. Category Filter
        if (category != null && !category.trim().isEmpty() && !"ALL".equalsIgnoreCase(category.trim())) {
            expenses = expenses.stream()
                    .filter(e -> category.equalsIgnoreCase(e.getCategory()))
                    .collect(Collectors.toList());
        }

        // 3. Date Range Filter
        if (startDate != null && !startDate.trim().isEmpty()) {
            expenses = expenses.stream()
                    .filter(e -> e.getDate() != null && e.getDate().compareTo(startDate.trim()) >= 0)
                    .collect(Collectors.toList());
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            expenses = expenses.stream()
                    .filter(e -> e.getDate() != null && e.getDate().compareTo(endDate.trim()) <= 0)
                    .collect(Collectors.toList());
        }

        // 4. Sorting
        if (sortBy != null) {
            switch (sortBy) {
                case "date_asc":
                    expenses.sort(Comparator.comparing(Expense::getDate, Comparator.nullsLast(String::compareTo)));
                    break;
                case "amount_desc":
                    expenses.sort((a, b) -> Double.compare(b.getAmount(), a.getAmount()));
                    break;
                case "amount_asc":
                    expenses.sort(Comparator.comparingDouble(Expense::getAmount));
                    break;
                case "description_asc":
                    expenses.sort(Comparator.comparing(Expense::getDescription, Comparator.nullsLast(String.CASE_INSENSITIVE_ORDER)));
                    break;
                case "date_desc":
                default:
                    expenses.sort((a, b) -> {
                        if (a.getDate() == null || b.getDate() == null) return 0;
                        return b.getDate().compareTo(a.getDate());
                    });
                    break;
            }
        }

        return expenses;
    }

    /**
     * Updates an existing expense.
     */
    public boolean updateExpense(String userId, String expenseId, double amount, String description,
                                  String category, String date, String notes) {
        validateExpenseInputs(userId, amount, description, category, date);

        Expense existing = expenseDAO.getExpenseById(userId, expenseId);
        if (existing == null) {
            throw new IllegalArgumentException("Expense record not found or access denied.");
        }

        existing.setAmount(amount);
        existing.setDescription(description.trim());
        existing.setCategory(category.trim());
        existing.setDate(date.trim());
        existing.setNotes(notes != null ? notes.trim() : "");

        return expenseDAO.updateExpense(existing);
    }

    /**
     * Deletes an expense ensuring user ownership.
     */
    public boolean deleteExpense(String userId, String expenseId) {
        if (userId == null || expenseId == null) return false;
        return expenseDAO.deleteExpense(userId, expenseId);
    }

    private void validateExpenseInputs(String userId, double amount, String description, String category, String date) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("Unauthorized user session.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description is required.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Category is required.");
        }
        if (date == null || !ValidationUtil.isValidDate(date)) {
            throw new IllegalArgumentException("A valid date in YYYY-MM-DD format is required.");
        }
    }
}
