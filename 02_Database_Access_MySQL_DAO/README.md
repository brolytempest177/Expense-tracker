# 🗄 02 — Database Access Layer / MySQL DAOs (`com.expensetracker.dao`)

## 🎯 Purpose of This Layer
The **DAO (Data Access Object)** layer is the ONLY layer in the entire application that directly interacts with the **MySQL Relational Database**. It encapsulates all database query syntax (`SELECT`, `INSERT`, `UPDATE`, `DELETE`), connection management, and ResultSet-to-Object mapping away from the business logic.

Because the system was built using the DAO pattern, switching from Firebase to MySQL was completed cleanly without having to modify a single controller, servlet, or view!

---

## 📂 Files in This Folder

### 1. `UserDAO.java`
* **Purpose**: Manages user persistence in the `users` table.
* **SQL Queries Executed**:
  * `createUser(User user)`: 
    ```sql
    INSERT INTO users (user_id, name, email, password_hash, created_at) VALUES (?, ?, ?, ?, ?)
    ```
  * `findUserByEmail(String email)`: 
    ```sql
    SELECT user_id, name, email, password_hash, created_at FROM users WHERE LOWER(email) = LOWER(?) LIMIT 1
    ```
  * `findUserById(String userId)`: 
    ```sql
    SELECT user_id, name, email, password_hash, created_at FROM users WHERE user_id = ? LIMIT 1
    ```
  * `isEmailRegistered(String email)`: Checks whether an account already exists for the given email.

---

### 2. `ExpenseDAO.java`
* **Purpose**: Performs CRUD operations on the `expenses` table, strictly scoped by `user_id`.
* **Key Methods**:
  * `addExpense(Expense expense)`: Inserts a new record into `expenses` with parameterized values and UUID primary key.
  * `getExpenseById(String userId, String expenseId)`: Scopes queries to `user_id = ? AND id = ?`, ensuring complete data privacy between users.
  * `getAllExpensesByUser(String userId)`: Retrieves all expenses for a user sorted chronologically (`ORDER BY expense_date DESC`).
  * `getExpensesByMonthAndYear(String userId, int month, int year)`: Employs MySQL native date functions `MONTH(expense_date) = ? AND YEAR(expense_date) = ?` for high performance.
  * `updateExpense(Expense expense)`: Updates amount, description, category, date, and notes.
  * `deleteExpense(String userId, String expenseId)`: Securely deletes an expense scoped to the user ID.

---

### 3. `BudgetDAO.java`
* **Purpose**: Manages monthly target budgets in the `budgets` table.
* **Key Methods**:
  * `setOrUpdateBudget(Budget budget)`: Uses atomic upsert syntax:
    ```sql
    INSERT INTO budgets (id, user_id, month, year, amount, updated_at)
    VALUES (?, ?, ?, ?, ?, ?)
    ON DUPLICATE KEY UPDATE amount = VALUES(amount), updated_at = VALUES(updated_at);
    ```
  * `getBudget(String userId, int month, int year)`: Queries monthly budget by `user_id`, `month`, and `year`.
  * `getAllBudgets(String userId)`: Retrieves historical budgets ordered by year and month descending.

---

## 🔒 Security Architecture (SQL Injection & Isolation)

1. **PreparedStatement Parameterization**:
   Every query uses `?` place-holders via `PreparedStatement`. User inputs are sent separately from the SQL statement structure to the MySQL parser, completely neutralizing **SQL Injection (SQLi)** attacks.
2. **Strict User Isolation**:
   Every `SELECT`, `UPDATE`, and `DELETE` query requires both `user_id` and the record identifier, ensuring that User A can never manipulate or view User B's financial transactions.

---

## 🎤 College Viva / Presentation Speaking Points

> **Q: What is the DAO (Data Access Object) Design Pattern?**  
> *Answer*: The DAO pattern provides an abstract interface to the database. The business logic layer (Services) calls methods like `expenseDAO.addExpense(expense)` without needing to know anything about SQL queries, JDBC connections, or table schemas.

> **Q: How did you prevent SQL Injection in your Java project?**  
> *Answer*: We strictly avoided string concatenation (e.g. `"SELECT * FROM users WHERE email = '" + email + "'"`). Instead, we used Java's `java.sql.PreparedStatement` with parameterized placeholders (`?`). The database compiles the query structure first, and values are bound safely as literals.

> **Q: Why did you use Connection Pooling (HikariCP) instead of opening a new Connection every time?**  
> *Answer*: Creating a new TCP connection to MySQL for every HTTP request has high latency (handshake, authentication, socket allocation). Connection pooling maintains a ready pool of active connections in memory, significantly lowering response time and resource overhead.

> **Q: What database normalization forms does the schema satisfy?**  
> *Answer*: The database satisfies **3NF (Third Normal Form)**:
> - **1NF**: Atomic column values, primary keys defined for all tables.
> - **2NF**: No partial dependencies; all non-key attributes depend on the entire primary key.
> - **3NF**: No transitive dependencies; non-key columns depend only on the primary key (e.g., user details are stored in `users`, not duplicated inside `expenses`).
