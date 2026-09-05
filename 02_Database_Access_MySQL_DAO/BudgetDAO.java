package com.expensetracker.dao;

import com.expensetracker.model.Budget;
import com.expensetracker.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Budget operations using MySQL and JDBC PreparedStatements.
 * Uses atomic upsert (INSERT ... ON DUPLICATE KEY UPDATE) for thread-safe monthly budget allocations.
 */
public class BudgetDAO {
    private static final Logger LOGGER = Logger.getLogger(BudgetDAO.class.getName());

    public BudgetDAO() {
    }

    /**
     * Sets or updates a monthly budget for a user.
     */
    public boolean setOrUpdateBudget(Budget budget) {
        if (budget == null || budget.getUserId() == null) {
            return false;
        }

        String docId = budget.getUserId() + "_" + budget.getYear() + "_" + budget.getMonth();
        budget.setId(docId);

        String sql = "INSERT INTO budgets (id, user_id, month, year, amount, updated_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE amount = VALUES(amount), updated_at = VALUES(updated_at)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, docId);
            ps.setString(2, budget.getUserId());
            ps.setInt(3, budget.getMonth());
            ps.setInt(4, budget.getYear());
            ps.setDouble(5, budget.getAmount());

            Date updatedAt = budget.getUpdatedAt() != null ? budget.getUpdatedAt() : new Date();
            ps.setTimestamp(6, new Timestamp(updatedAt.getTime()));

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error setting budget for user " + budget.getUserId() + ": " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Retrieves the budget for a specific month and year.
     */
    public Budget getBudget(String userId, int month, int year) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT id, user_id, month, year, amount, updated_at " +
                     "FROM budgets WHERE user_id = ? AND month = ? AND year = ? LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);
            ps.setInt(2, month);
            ps.setInt(3, year);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToBudget(rs);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting budget for " + year + "-" + month + ": " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Retrieves all configured budgets for a user.
     */
    public List<Budget> getAllBudgets(String userId) {
        List<Budget> list = new ArrayList<>();
        if (userId == null || userId.trim().isEmpty()) {
            return list;
        }

        String sql = "SELECT id, user_id, month, year, amount, updated_at " +
                     "FROM budgets WHERE user_id = ? ORDER BY year DESC, month DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowToBudget(rs));
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error getting all budgets for user " + userId + ": " + e.getMessage(), e);
        }
        return list;
    }

    private Budget mapRowToBudget(ResultSet rs) throws Exception {
        Budget budget = new Budget();
        budget.setId(rs.getString("id"));
        budget.setUserId(rs.getString("user_id"));
        budget.setMonth(rs.getInt("month"));
        budget.setYear(rs.getInt("year"));
        budget.setAmount(rs.getDouble("amount"));

        Timestamp ts = rs.getTimestamp("updated_at");
        if (ts != null) {
            budget.setUpdatedAt(new Date(ts.getTime()));
        }
        return budget;
    }
}
