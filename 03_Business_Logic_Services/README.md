# ⚙️ 03 — Business Logic & Services Layer (`com.expensetracker.service`)

## 🎯 Purpose of This Layer
The **Service Layer** coordinates business workflows, calculations, data validation rules, filtering, and statistical aggregations. Servlets (Controllers) invoke Services to perform operations and receive clean DTOs (Data Transfer Objects) to pass to JSPs.

---

## 📂 Files in This Folder

### 1. `AuthService.java`
* **Purpose**: Orchestrates registration and credential authentication.
* **Key Functions**:
  * `register(name, email, password, confirmPassword)`:
    1. Validates non-empty name and email format.
    2. Enforces password strength ($\ge 6$ characters) and equality with confirmation.
    3. Checks if email is already taken.
    4. Hashes password using BCrypt.
    5. Saves user in Firestore via `UserDAO`.
  * `login(email, password)`:
    1. Looks up user by email.
    2. Verifies plain-text password against stored BCrypt hash using `PasswordUtil.verifyPassword()`.
    3. Returns authenticated `User` object or throws descriptive validation error.

---

### 2. `ExpenseService.java`
* **Purpose**: Business logic for expense creation, modification, multi-criteria filtering, search, and sorting.
* **Key Functions**:
  * `addExpense(userId, amount, description, category, date, notes)`: Validates positive amounts, required categories, and ISO dates before saving.
  * `getFilteredExpenses(userId, query, category, startDate, endDate, sortBy)`:
    * **Search Filter**: Case-insensitive substring matching across `description` and `notes`.
    * **Category Filter**: Matches specific category or `ALL`.
    * **Date Range Filter**: Limits results between `startDate` and `endDate`.
    * **Sorting**: Supports `date_desc` (newest), `date_asc` (oldest), `amount_desc` (highest), `amount_asc` (lowest), `description_asc` (A-Z).
  * `updateExpense(...)` & `deleteExpense(...)`: Validates record ownership and updates/deletes records.

---

### 3. `BudgetService.java`
* **Purpose**: Calculates monthly expenditure limits, remaining balances, and status warning triggers.
* **Key Functions**:
  * `setBudget(userId, month, year, amount)`: Validates month (1–12), year (2000–2100), and non-negative amount.
  * `computeBudgetStatus(userId, month, year, totalSpent)`:
    * Calculates `remainingAmount = budgetAmount - totalSpent`.
    * Calculates `percentageUsed = (totalSpent / budgetAmount) * 100`.
    * Computes `statusLevel`:
      * 🟢 `SAFE`: Spent $< 80\%$ of budget.
      * 🟡 `WARNING`: Spent between $80\%$ and $100\%$ of budget.
      * 🔴 `EXCEEDED`: Spent $> 100\%$ of budget (over budget).

---

### 4. `ReportService.java`
* **Purpose**: Computes comprehensive monthly analytics, category breakdown statistics, and Chart.js JSON payloads.
* **Key Functions**:
  * `getMonthlyAnalytics(userId, month, year)`:
    * Calculates `totalSpent` for the month.
    * Finds `highestExpense` transaction.
    * Computes `transactionCount` and `averageTransaction`.
    * Computes map of category totals (e.g., Food: ₹2,500, Transport: ₹1,200).
    * Computes map of daily spending timeline for trend bars.
    * Serializes Chart.js datasets into JSON (`categoryLabelsJson`, `categoryDataJson`, `categoryColorsJson`, `trendLabelsJson`, `trendDataJson`) via Google Gson.

---

## 🎤 College Viva / Presentation Speaking Points
> **Q: Why keep business logic in Services rather than directly inside Servlets?**  
> *Answer*: Separation of concerns. Keeping business rules, calculations, and filtering inside the Service layer makes the code reusable, easier to unit test, and keeps Servlets focused solely on handling HTTP requests and forwarding to JSP views.
