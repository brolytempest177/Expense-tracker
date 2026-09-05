# 🛡️ 05 — Utilities, Security & Database Configuration (`com.expensetracker.util`)

## 🎯 Purpose of This Layer
This folder contains shared cross-cutting utility classes for **MySQL Database Connection & Pooling**, **Session State Management**, **BCrypt Password Security**, and **Server-Side Validation & XSS Sanitization**.

---

## 📂 Files in This Folder

### 1. `DBUtil.java`
* **Purpose**: Manages MySQL JDBC connection pooling, automated schema creation, and safe resource disposal.
* **Features**:
  * **HikariCP Connection Pooling**: Initializes a thread-safe connection pool for maximum performance and low latency.
  * **Configuration Hierarchy**: Reads from `src/main/resources/db.properties` with support for environment variable overrides (`MYSQL_URL`, `MYSQL_USER`, `MYSQL_PASSWORD`).
  * **Auto-Schema Initialization**: Automatically runs `CREATE TABLE IF NOT EXISTS` for `users`, `expenses`, and `budgets` on startup so new developers don't have to manually execute scripts.
  * **Safe Resource Cleanup**: Provides `close(AutoCloseable...)` to prevent database connection and memory leaks.

---

### 2. `SessionUtil.java`
* **Purpose**: Helper for managing HTTP Session attributes and user authentication state.
* **Key Methods**:
  * `setLoggedInUser(request, user)`: Stores `User` object and `userId` in `HttpSession`.
  * `getLoggedInUser(request)`: Retrieves the currently logged-in `User` entity.
  * `getLoggedInUserId(request)`: Extracts the active user's unique ID.
  * `isLoggedIn(request)`: Checks whether an active authenticated session exists.
  * `logout(request)`: Invalidates the `HttpSession`.
  * `setFlashMessage(request, type, message)`: Stores temporary alert notifications (`FLASH_SUCCESS`, `FLASH_ERROR`) that disappear after rendering.

---

### 3. `PasswordUtil.java`
* **Purpose**: High-security password hashing and validation.
* **Key Methods**:
  * `hashPassword(plainPassword)`: Generates a salted **BCrypt hash** (cost factor 12).
  * `verifyPassword(plainPassword, hashedPassword)`: Uses `BCrypt.checkpw()` to verify passwords in constant time (protecting against timing attacks).
  * Includes resilient fallback to SHA-256 with salt if BCrypt native binaries are unavailable.

---

### 4. `ValidationUtil.java`
* **Purpose**: Server-side validation and XSS prevention.
* **Key Methods**:
  * `isValidEmail(email)`: Validates email format using regex pattern `^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,63}$`.
  * `isValidPassword(password)`: Checks password length $\ge 6$.
  * `isValidAmount(amountStr)`: Checks for positive numeric values $> 0$.
  * `isValidDate(dateStr)`: Enforces valid `YYYY-MM-DD` ISO dates.
  * `isValidMonth(month)`: Ensures integer between 1 and 12.
  * `sanitize(input)`: Escapes `<`, `>`, `&`, `"`, `'`, `/` into safe HTML entities to prevent **Cross-Site Scripting (XSS)** attacks.

---

## 🎤 College Viva / Presentation Speaking Points

> **Q: Why use BCrypt for passwords instead of plain MD5 or SHA-256?**  
> *Answer*: MD5 and plain SHA-256 are fast hash algorithms susceptible to brute-force and rainbow table attacks using modern GPUs. BCrypt includes a configurable work factor (cost) that makes it intentionally slow and automatically generates a unique salt per password, protecting against precomputed table attacks.

> **Q: Why is server-side validation mandatory even when HTML5 client-side validation is present?**  
> *Answer*: Client-side JavaScript validation can be easily bypassed by disabling JavaScript or sending direct HTTP POST requests via tools like Postman or cURL. Validating on the server ensures data integrity and security at all times.

> **Q: How does `DBUtil` prevent connection leaks?**  
> *Answer*: All database interactions use Java 7+ **try-with-resources** blocks. This automatically guarantees that `Connection`, `PreparedStatement`, and `ResultSet` are returned to the HikariCP pool when the block exits, even if an unhandled exception occurs.
