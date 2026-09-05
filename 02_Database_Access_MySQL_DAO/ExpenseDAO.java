package com.expensetracker.dao;

import com.expensetracker.model.Expense;
import com.expensetracker.util.DBUtil;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Expense operations using MySQL and JDBC PreparedStatements.
 * Enforces strict user isolation by scoping every query and operation to the user's ID.
 */
public class ExpenseDAO {
    private static final Logger LOGGER = Logger.getLogger(ExpenseDAO.class.getName());

    public ExpenseDAO() {
    }

    /**
     * Adds a new expense record for a user.
     */
    public boolean addExpense(Expense expense) {
        if (expense == null || expense.getUserId() == null) {
            return false;
        }

        String expenseId = expense.getId();
        if (expenseId == null || expenseId.trim().isEmpty()) {
            expenseId = UUID.randomUUID().toString();
            expense.setId(expenseId);
        }

        String sql = "INSERT INTO expenses (id, user_id, amount, description, category, expense_date, notes, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, expenseId);
            ps.setString(2, expense.getUserId());
            ps.setDouble(3, expense.getAmount());
            ps.setString(4, expense.getDescription());
            ps.setString(5, expense.getCategory());

            // Convert string date YYYY-MM-DD to SQL Date
            Date sqlDate = Date.valueOf(expense.getDate());
            ps.setDate(6, sqlDate);

            ps.setString(7, expense.getNotes() != null ? expense.getNotes() : "");

            java.util.Date createdAt = expense.getCreatedAt() != null ? expense.getCreatedAt() : new java.util.Date();
            ps.setTimestamp(8, new Timestamp(createdAt.getTime()));

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting expense for user " + expense.getUserId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves an expense by its unique ID for a specific user.
     */
    public Expense getExpenseById(String userId, String expenseId) {
        if (userId == null || expenseId == null || expenseId.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT id, user_id, amount, description, category, expense_date, notes, created_at " +
                     "FROM expenses WHERE user_id = ? AND id = ? LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setString(2, expenseId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToExpense(rs);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting expense " + expenseId + ": " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Retrieves all expenses for a user, sorted by date descending.
     */
    public List<Expense> getAllExpensesByUser(String userId) {
        List<Expense> list = new ArrayList<>();
        if (userId == null || userId.trim().isEmpty()) {
            return list;
        }

        String sql = "SELECT id, user_id, amount, description, category, expense_date, notes, created_at " +
                     "FROM expenses WHERE user_id = ? ORDER BY expense_date DESC, created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToExpense(rs));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error querying all expenses for user " + userId + ": " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * Retrieves expenses for a specific month and year using native SQL functions.
     * @param month 1-12
     * @param year e.g. 2026
     */
    public List<Expense> getExpensesByMonthAndYear(String userId, int month, int year) {
        List<Expense> list = new ArrayList<>();
        if (userId == null || userId.trim().isEmpty()) {
            return list;
        }

        String sql = "SELECT id, user_id, amount, description, category, expense_date, notes, created_at " +
                     "FROM expenses WHERE user_id = ? AND MONTH(expense_date) = ? AND YEAR(expense_date) = ? " +
                     "ORDER BY expense_date DESC, created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setInt(2, month);
            ps.setInt(3, year);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToExpense(rs));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error querying expenses by month/year: " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * Updates an existing expense record.
     */
    public boolean updateExpense(Expense expense) {
        if (expense == null || expense.getId() == null || expense.getUserId() == null) {
            return false;
        }

        String sql = "UPDATE expenses SET amount = ?, description = ?, category = ?, expense_date = ?, notes = ? " +
                     "WHERE id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, expense.getAmount());
            ps.setString(2, expense.getDescription());
            ps.setString(3, expense.getCategory());
            ps.setDate(4, Date.valueOf(expense.getDate()));
            ps.setString(5, expense.getNotes() != null ? expense.getNotes() : "");
            ps.setString(6, expense.getId());
            ps.setString(7, expense.getUserId());

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating expense " + expense.getId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Deletes an expense by ID for a specific user.
     */
    public boolean deleteExpense(String userId, String expenseId) {
        if (userId == null || expenseId == null || expenseId.trim().isEmpty()) {
            return false;
        }

        String sql = "DELETE FROM expenses WHERE id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, expenseId);
            ps.setString(2, userId);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting expense " + expenseId + ": " + e.getMessage(), e);
            return false;
        }
    }

    private Expense mapRowToExpense(ResultSet rs) throws Exception {
        Expense expense = new Expense();
        expense.setId(rs.getString("id"));
        expense.setUserId(rs.getString("user_id"));
        expense.setAmount(rs.getDouble("amount"));
        expense.setDescription(rs.getString("description"));
        expense.setCategory(rs.getString("category"));

        Date sqlDate = rs.getDate("expense_date");
        expense.setDate(sqlDate != null ? sqlDate.toString() : "");

        expense.setNotes(rs.getString("notes"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            expense.setCreatedAt(new java.util.Date(ts.getTime()));
        }
        return expense;
    }
}
