-- Expense Tracker MySQL Schema
-- Run this file to create all required tables.

CREATE DATABASE IF NOT EXISTS expense_tracker;
USE expense_tracker;

-- Users table
CREATE TABLE IF NOT EXISTS users (
    user_id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Expenses table (per-user, linked by user_id)
CREATE TABLE IF NOT EXISTS expenses (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    amount DOUBLE NOT NULL,
    description VARCHAR(500) NOT NULL,
    category VARCHAR(50) NOT NULL,
    date VARCHAR(10) NOT NULL,       -- YYYY-MM-DD format
    notes TEXT DEFAULT '',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_expenses_user_date (user_id, date DESC),
    INDEX idx_expenses_user_category (user_id, category)
);

-- Budgets table (one per user per month)
CREATE TABLE IF NOT EXISTS budgets (
    id VARCHAR(10) PRIMARY KEY,      -- format: YYYY-M
    user_id VARCHAR(36) NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    amount DOUBLE NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY uk_budget_user_period (user_id, month, year)
);
