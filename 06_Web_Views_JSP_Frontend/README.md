# 🖥️ 06 — Web Views & JSP Presentation Layer

## 🎯 Purpose of This Layer
This folder contains the **JSP (JavaServer Pages)** views responsible for rendering HTML in the user's browser. It uses **JSTL (JavaServer Pages Standard Tag Library)** (`<c:forEach>`, `<c:if>`, `<c:out>`) and **EL (Expression Language)** (`${...}`) to display dynamic data passed by the Servlets.

---

## 📂 Files in This Folder

### 1. `login.jsp`
* **Features**: Clean login form with email, password, error notifications, flash messages, and registration link.

### 2. `register.jsp`
* **Features**: User registration form with name, email, password, and confirm-password verification.

### 3. `dashboard.jsp`
* **Features**:
  * 4 Summary KPI cards: Total Spent This Month, Total Transactions, Remaining Budget, Highest Single Expense.
  * Budget progress bar with dynamic color changes (Green / Amber / Red).
  * Chart.js Doughnut Chart for Category Breakdown.
  * Chart.js Bar Chart for Daily Spending Trend.
  * Table of 5 most recent transactions with quick-edit links.

### 4. `expenses.jsp`
* **Features**:
  * Complete tabular transaction list with category badge pills.
  * Search bar (keyword matching in description and notes).
  * Category dropdown filter.
  * Date range filter (From / To).
  * Sort dropdown (Date newest/oldest, Amount high/low, Description A-Z).
  * Summary ribbon with total filtered transactions and amount.
  * Edit button and Delete button with confirmation dialog.

### 5. `add-expense.jsp`
* **Features**: Form for adding a new expense with amount, date, description, category selector, and optional notes.

### 6. `edit-expense.jsp`
* **Features**: Pre-filled modification form for updating an existing expense.

### 7. `budget.jsp`
* **Features**:
  * Month and Year selector.
  * Budget amount input and update form.
  * Spending vs target metrics (Allocated Budget, Total Spent, Remaining Balance, % Used).
  * Historical budget targets list.

### 8. `reports.jsp`
* **Features**:
  * Monthly financial summary metrics.
  * Category spending distribution chart and daily trend timeline.
  * Detailed category breakdown table with spending intensity progress bars.
  * **Print Report** button with print stylesheet formatting.

### 9. `WEB-INF/web.xml`
* **Features**: Servlet deployment descriptor defining welcome pages, session timeout (30 mins), and cookie security.

---

## 🎤 College Viva / Presentation Speaking Points
> **Q: Why use JSTL and Expression Language (`${...}`) instead of Java Scriptlets (`<% ... %>`) in JSP?**  
> *Answer*: Scriptlets embed raw Java code directly into HTML, leading to messy, hard-to-maintain code. JSTL tags (`<c:forEach>`, `<c:if>`) and EL provide clean, readable separation of presentation logic from Java backend code.
