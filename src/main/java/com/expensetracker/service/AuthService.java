package com.expensetracker.service;

import com.expensetracker.dao.UserDAO;
import com.expensetracker.model.User;
import com.expensetracker.util.PasswordUtil;
import com.expensetracker.util.ValidationUtil;

import java.util.Date;
import java.util.UUID;

/**
 * Service class handling authentication business logic.
 */
public class AuthService {
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Registers a new user account.
     * @param name Full name
     * @param email User email
     * @param password Password
     * @param confirmPassword Confirmation password
     * @return Created User object
     * @throws IllegalArgumentException on validation or business rule failure
     */
    public User register(String name, String email, String password, String confirmPassword) throws IllegalArgumentException {
        // Validation
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name is required.");
        }
        if (email == null || !ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (password == null || !ValidationUtil.isValidPassword(password)) {
            throw new IllegalArgumentException("Password must be at least 6 characters long.");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        // Check if email already registered
        if (userDAO.isEmailRegistered(email)) {
            throw new IllegalArgumentException("An account with this email already exists.");
        }

        // Hash password and build user
        String userId = UUID.randomUUID().toString();
        String hashedPassword = PasswordUtil.hashPassword(password);
        User newUser = new User(userId, name.trim(), email.toLowerCase().trim(), hashedPassword, new Date());

        boolean created = userDAO.createUser(newUser);
        if (!created) {
            throw new IllegalStateException("Failed to create user account due to a database error. Please verify Firebase connection.");
        }

        return newUser;
    }

    /**
     * Authenticates user with email and password.
     * @param email User email
     * @param password User password
     * @return Authenticated User object
     * @throws IllegalArgumentException on invalid credentials
     */
    public User login(String email, String password) throws IllegalArgumentException {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Email and password are required.");
        }

        User user = userDAO.findUserByEmail(email.trim());
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        return user;
    }

    /**
     * Retrieves user profile by user ID.
     */
    public User getUserById(String userId) {
        if (userId == null || userId.trim().isEmpty()) return null;
        return userDAO.findUserById(userId);
    }
}
