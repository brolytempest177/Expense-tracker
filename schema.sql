-- ================================================================
-- Database Schema for Java Expense Tracker Application
-- Target: MySQL 8.0+ / MariaDB 10.3+
-- ================================================================

CREATE DATABASE IF NOT EXISTS expense_tracker_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE expense_tracker_db;

-- ----------------------------------------------------------------
-- 1. Table: users
-- Stores registered user accounts with salted & hashed passwords.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------
-- 2. Table: expenses
-- Stores expense records tied directly to a specific user.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS expenses (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    category VARCHAR(50) NOT NULL,
    expense_date DATE NOT NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_expenses_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_date (user_id, expense_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------
-- 3. Table: budgets
-- Stores user monthly budget allocations.
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS budgets (
    id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_budgets_user FOREIGN KEY (user_id)
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT uq_user_month_year UNIQUE (user_id, year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------
-- Seed Data: Pre-configured Demo Account (password: password123)
-- ----------------------------------------------------------------
INSERT INTO users (user_id, name, email, password_hash, created_at)
VALUES (
    'demo-user-001',
    'Demo Student',
    'demo@expensetracker.com',
    '$2a$12$T97nZ4k391OaY5C411e7kOUUo9nLlhZgP1H.4fM3dG2zBqYI2K.9u',
    NOW()
) ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Seed Initial Monthly Budget ($2,000 for current month)
INSERT INTO budgets (id, user_id, month, year, amount, updated_at)
VALUES (
    'demo-user-001-2026-9',
    'demo-user-001',
    9,
    2026,
    2000.00,
    NOW()
) ON DUPLICATE KEY UPDATE amount=VALUES(amount);

-- Seed Sample Expenses for Demo
INSERT INTO expenses (id, user_id, amount, description, category, expense_date, notes, created_at)
VALUES 
    ('exp-001', 'demo-user-001', 45.50, 'Grocery shopping at supermarket', 'Food & Dining', '2026-09-01', 'Weekly provisions', NOW()),
    ('exp-002', 'demo-user-001', 15.00, 'Metro commuter pass reload', 'Transportation', '2026-09-02', 'Public transit', NOW()),
    ('exp-003', 'demo-user-001', 65.00, 'High-speed internet monthly bill', 'Utilities', '2026-09-03', 'Work from home broadband', NOW()),
    ('exp-004', 'demo-user-001', 12.99, 'Movie streaming subscription', 'Entertainment', '2026-09-04', 'Monthly entertainment plan', NOW()),
    ('exp-005', 'demo-user-001', 120.00, 'Java Programming Reference Books', 'Education', '2026-09-05', 'Semester textbooks', NOW())
ON DUPLICATE KEY UPDATE amount=VALUES(amount);
