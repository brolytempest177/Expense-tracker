# 📦 01 — Data Models & Entities Layer (`com.expensetracker.model`)

## 🎯 Purpose of This Layer
This folder contains the core **Domain Entities / POJOs (Plain Old Java Objects)** representing the fundamental data structures of the application. These classes model real-world concepts (Users, Expenses, Budgets, and Categories), provide getters/setters for data encapsulation, and support serialization when interacting with Google Cloud Firestore.

---

## 📂 Files in This Folder

### 1. `User.java`
* **Purpose**: Represents an authenticated user account in the system.
* **Database Path**: `users/{userId}`
* **Key Fields**:
  * `String userId`: Unique UUID identifying the user.
  * `String name`: User's display name.
  * `String email`: User's login email address (case-insensitive).
  * `String passwordHash`: BCrypt-encrypted password string (plain text is NEVER stored).
  * `Date createdAt`: Timestamp when the user registered.
* **Firestore Requirement**: Includes a default no-argument constructor `public User() {}` which Firestore requires for automatic document-to-object mapping.

---

### 2. `Expense.java`
* **Purpose**: Represents an individual financial transaction or expense entry.
* **Database Path**: `users/{userId}/expenses/{expenseId}`
* **Key Fields**:
  * `String id`: Unique UUID for the expense record.
  * `String userId`: ID of the owner user (ensuring strict data isolation).
  * `double amount`: Monetary cost of the transaction.
  * `String description`: Summary/title of the expense (e.g., "Grocery Shopping").
  * `String category`: Classification (e.g., "Food", "Transport", "Bills").
  * `String date`: Transaction date in ISO `YYYY-MM-DD` format.
  * `String notes`: Optional memo or description details.
  * `Date createdAt`: Timestamp when the record was added.
* **Helper Methods**:
  * `getMonth()`: Extracts month integer (1–12) from the date string.
  * `getYear()`: Extracts year integer (e.g. 2026) from the date string.
  * `getFormattedAmount()`: Returns formatted currency string (e.g., `450.00`).

---

### 3. `Budget.java`
* **Purpose**: Represents a monthly spending limit target set by a user.
* **Database Path**: `users/{userId}/budgets/{year}-{month}` (e.g. `2026-9`)
* **Key Fields**:
  * `String id`: Formatted as `{year}-{month}` to enforce one unique budget per month per user.
  * `String userId`: Owner user ID.
  * `int month`: Month number (1 for January, 12 for December).
  * `int year`: Target calendar year (e.g., 2026).
  * `double amount`: Maximum spending target for the month.
  * `Date updatedAt`: Timestamp when the budget was configured.
* **Helper Methods**:
  * `getMonthName()`: Returns the full name of the month (e.g., "September").
  * `getFormattedAmount()`: Returns two-decimal string format.

---

### 4. `Category.java`
* **Purpose**: Centralized constants and visual helpers for expense classification.
* **Categories Supported**:
  * 🍔 `Food` (Amber `#f59e0b`)
  * 🚗 `Transport` (Blue `#3b82f6`)
  * 📚 `Education` (Purple `#8b5cf6`)
  * 🛍️ `Shopping` (Pink `#ec4899`)
  * 🎬 `Entertainment` (Green `#10b981`)
  * 💡 `Bills` (Red `#ef4444`)
  * 💊 `Health` (Cyan `#06b6d4`)
  * 🏷️ `Other` (Slate Gray `#64748b`)
* **Methods**:
  * `getColor(category)`: Returns the hex color used in Chart.js doughnut graphs and badges.
  * `getIcon(category)`: Returns category emoji/icon for UI rendering.

---

## 🎤 College Viva / Presentation Speaking Points
> **Q: What is the role of the Model layer in MVC?**  
> *Answer*: The Model layer holds the business state, attributes, and data definitions. Models are passed from DAOs through Services to Servlets and finally into JSPs where JSTL and Expression Language (`${expense.description}`) render their values.
