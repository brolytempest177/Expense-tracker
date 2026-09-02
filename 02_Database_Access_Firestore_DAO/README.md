# 🗄 02 — Database Access Layer / Firestore DAOs (`com.expensetracker.dao`)

## 🎯 Purpose of This Layer
The **DAO (Data Access Object)** layer is the ONLY layer in the entire application that directly talks to Google Firebase Firestore. It isolates database query syntax and SDK calls from the business logic, ensuring that if database technology changes in the future, only this layer needs modification.

---

## 📂 Files in This Folder

### 1. `UserDAO.java`
* **Purpose**: Manages Firestore reads and writes on the `users` root collection.
* **Target Path**: `users/{userId}`
* **Key Methods**:
  * `createUser(User user)`: Inserts a new user document with name, lowercased email, and BCrypt password hash.
  * `findUserByEmail(String email)`: Queries the `users` collection where `email == email.toLowerCase()` to check login or registration uniqueness.
  * `findUserById(String userId)`: Retrieves user profile details by unique User ID.
  * `isEmailRegistered(String email)`: Helper returning `true`/`false` for email conflict prevention.

---

### 2. `ExpenseDAO.java`
* **Purpose**: Performs CRUD operations for expense records strictly scoped to the logged-in user.
* **Target Path**: `users/{userId}/expenses/{expenseId}` (Firestore Sub-Collection).
* **Key Methods**:
  * `addExpense(Expense expense)`: Writes a new expense document under the user's sub-collection.
  * `getExpenseById(String userId, String expenseId)`: Fetches a single expense record, guaranteeing that User A can NEVER view User B's record.
  * `getAllExpensesByUser(String userId)`: Retrieves all expenses for a user, sorted by date descending. Includes automatic graceful fallback if composite indexes are building.
  * `getExpensesByMonthAndYear(String userId, int month, int year)`: Filters expenses for monthly reporting and analytics.
  * `updateExpense(Expense expense)`: Updates amount, description, category, date, and notes.
  * `deleteExpense(String userId, String expenseId)`: Deletes an expense document permanently from Firestore.

---

### 3. `BudgetDAO.java`
* **Purpose**: Manages monthly target budgets under the user's document.
* **Target Path**: `users/{userId}/budgets/{year}-{month}` (e.g. `users/{userId}/budgets/2026-9`).
* **Key Methods**:
  * `setOrUpdateBudget(Budget budget)`: Uses document ID `{year}-{month}` to perform upsert (insert or update) operations in $O(1)$ time.
  * `getBudget(String userId, int month, int year)`: Directly fetches the monthly budget document for the specified month and year.
  * `getAllBudgets(String userId)`: Fetches all historical budgets configured by the user, sorted chronologically.

---

## 🔒 Security Architecture (User Isolation)
Every DAO query requires `userId` as an argument. By storing user documents inside sub-collections:
```
/users/{userId}/expenses/{expenseId}
/users/{userId}/budgets/{budgetId}
```
it is architecturally impossible for one user to read or tamper with another user's financial records.

---

## 🎤 College Viva / Presentation Speaking Points
> **Q: What is the DAO Design Pattern?**  
> *Answer*: The DAO pattern abstracts and encapsulates all access to the data source. It provides a clean interface (`addExpense`, `deleteExpense`, `getBudget`) to the Service Layer without exposing Firebase Firestore specifics like `CollectionReference` or `DocumentSnapshot`.
