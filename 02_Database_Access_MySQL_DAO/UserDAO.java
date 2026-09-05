package com.expensetracker.dao;

import com.expensetracker.model.User;
import com.expensetracker.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for User operations using MySQL and JDBC PreparedStatements.
 * Fully protects against SQL injection attacks by strictly using parameterized queries.
 */
public class UserDAO {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());

    public UserDAO() {
    }

    /**
     * Creates a new user record in the MySQL database.
     * @param user The user object to persist.
     * @return true if created successfully, false otherwise.
     */
    public boolean createUser(User user) {
        if (user == null || user.getUserId() == null) {
            return false;
        }

        String sql = "INSERT INTO users (user_id, name, email, password_hash, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUserId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail().toLowerCase().trim());
            ps.setString(4, user.getPasswordHash());

            Date createdAt = user.getCreatedAt() != null ? user.getCreatedAt() : new Date();
            ps.setTimestamp(5, new Timestamp(createdAt.getTime()));

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error inserting user: " + e.getMessage(), e);
            return false;
        }
    }

    /**
     * Finds a user by their email address.
     * @param email The user's email address.
     * @return User object if found, or null.
     */
    public User findUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, name, email, password_hash, created_at FROM users WHERE LOWER(email) = LOWER(?) LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email.trim());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by email: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Finds a user by their unique User ID.
     * @param userId The unique User ID.
     * @return User object if found, or null.
     */
    public User findUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, name, email, password_hash, created_at FROM users WHERE user_id = ? LIMIT 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error finding user by ID: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Checks whether an email is already registered in the system.
     */
    public boolean isEmailRegistered(String email) {
        return findUserByEmail(email) != null;
    }

    private User mapRowToUser(ResultSet rs) throws Exception {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPasswordHash(rs.getString("password_hash"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            user.setCreatedAt(new Date(ts.getTime()));
        }
        return user;
    }
}
