# 🚦 04 — HTTP Servlets & Controllers Layer (`com.expensetracker.servlet`)

## 🎯 Purpose of This Layer
This folder contains the **Java Servlets (Controllers)** and **Filters**. Servlets receive HTTP GET and POST requests from the browser, extract parameters, call the Service Layer, populate `request.setAttribute()`, and forward (`request.getRequestDispatcher().forward()`) or redirect (`response.sendRedirect()`) to the corresponding JSP views.

---

## 📂 Files in This Folder

### 1. `AuthFilter.java` (`@WebFilter("/*")`)
* **Role**: Security gatekeeper for all HTTP requests.
* **Logic**:
  * Allows public routes: `/login`, `/register`, `/logout`, `/css/*`, `/js/*`, `/images/*`.
  * For all protected routes (`/dashboard`, `/expenses`, `/budget`, `/reports`, etc.), checks `SessionUtil.isLoggedIn(request)`.
  * If unauthenticated, sets a flash error and redirects to `/login`.

---

### 2. `LoginServlet.java` (`@WebServlet({"/login", ""})`)
* **GET**: If logged in, redirects to `/dashboard`; otherwise forwards to `login.jsp`.
* **POST**: Extracts `email` and `password`, authenticates via `AuthService.login()`, stores user in `HttpSession`, and redirects to `/dashboard`.

---

### 3. `RegisterServlet.java` (`@WebServlet("/register")`)
* **GET**: Forwards to `register.jsp`.
* **POST**: Validates registration fields, calls `AuthService.register()`, logs in the new user automatically into session, and redirects to `/dashboard`.

---

### 4. `LogoutServlet.java` (`@WebServlet("/logout")`)
* **GET/POST**: Invalidates the current `HttpSession` via `SessionUtil.logout()` and redirects to `/login?logout=true`.

---

### 5. `DashboardServlet.java` (`@WebServlet("/dashboard")`)
* **GET**:
  1. Extracts logged-in user ID from session.
  2. Queries `ReportService` for this month's analytics (total spent, transaction count, highest expense, category breakdown, daily trend).
  3. Queries `BudgetService` for budget status and progress.
  4. Queries `ExpenseService` for recent 5 transactions.
  5. Sets request attributes and forwards to `dashboard.jsp`.

---

### 6. `ExpenseListServlet.java` (`@WebServlet("/expenses")`)
* **GET**: Extracts search parameters (`query`, `category`, `startDate`, `endDate`, `sortBy`), retrieves filtered expenses from `ExpenseService`, computes total filtered sum, and forwards to `expenses.jsp`.

---

### 7. `AddExpenseServlet.java` (`@WebServlet("/add-expense")`)
* **GET**: Loads category list and today's date, then forwards to `add-expense.jsp`.
* **POST**: Validates input (`amount`, `description`, `category`, `date`, `notes`), saves to Firestore, sets success flash alert, and redirects to `/expenses`.

---

### 8. `EditExpenseServlet.java` (`@WebServlet("/edit-expense")`)
* **GET**: Retrieves existing expense by ID (verifying ownership) and forwards to `edit-expense.jsp`.
* **POST**: Updates the expense record in Firestore and redirects to `/expenses`.

---

### 9. `DeleteExpenseServlet.java` (`@WebServlet("/delete-expense")`)
* **POST**: Extracts `id`, verifies user ownership, deletes the record from Firestore, and redirects to `/expenses` with confirmation notice.

---

### 10. `BudgetServlet.java` (`@WebServlet("/budget")`)
* **GET**: Fetches current month's budget, calculates spending vs target, retrieves historical budget records, and forwards to `budget.jsp`.
* **POST**: Updates or inserts monthly budget in Firestore and redirects to `/budget`.

---

### 11. `ReportServlet.java` (`@WebServlet("/reports")`)
* **GET**: Calculates monthly statistics, category percentages, Chart.js dataset JSON, and forwards to `reports.jsp`.

---

## 🎤 College Viva / Presentation Speaking Points
> **Q: What is the lifecycle of a Java Servlet?**  
> *Answer*:  
> 1. **Loading & Instantiation**: Tomcat loads the servlet class.  
> 2. **Initialization (`init()`)**: Runs once when the servlet is first created.  
> 3. **Request Handling (`service() -> doGet()/doPost()`)**: Executes for every client HTTP request in a separate thread.  
> 4. **Destruction (`destroy()`)**: Runs when Tomcat shuts down or the application is undeployed.
