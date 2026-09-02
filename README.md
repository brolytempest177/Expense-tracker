# 💸 Java Expense Tracker — Full-Stack Educational Web Application
### *Organized by Architectural Layers & Functional Modules*

Welcome to the **Java Expense Tracker** project! This project is organized with functional folder names, dedicated folder guides, and complete source code to make understanding, presenting, and defending the project in your college viva effortless.

---

## 🗂️ Project Structure & Functional Modules

Each directory in this repository represents a distinct functional layer of the application and contains its own dedicated **`README.md`** explaining the code inside:

```
Expense_Tracking_JAVA_AntigravtiyStyle/
│
├── 📄 pom.xml                                      # Maven Project Configuration & Dependencies
├── 📄 README.md                                    # Master Project Documentation & Viva Guide
├── 📄 index.html                                   # 1-Click Instant Browser App Launcher
├── ⚙️ run-server.bat                               # 1-Click Apache Tomcat Server Launcher
├── ⚙️ stop-server.bat                              # 1-Click Apache Tomcat Server Stopper
│
├── 📁 01_Data_Models_Entities/                     # [Layer 1] Data Models & POJO Entities
│   ├── 📄 README.md                                # Guide to User, Expense, Budget, Category models
│   ├── User.java, Expense.java, Budget.java, Category.java
│
├── 📁 02_Database_Access_Firestore_DAO/            # [Layer 2] Cloud Firestore Data Access (DAOs)
│   ├── 📄 README.md                                # Guide to Firestore CRUD & User Data Isolation
│   ├── UserDAO.java, ExpenseDAO.java, BudgetDAO.java
│
├── 📁 03_Business_Logic_Services/                  # [Layer 3] Business Rules & Financial Calculations
│   ├── 📄 README.md                                # Guide to Filters, Search, Analytics & Budget Logic
│   ├── AuthService.java, ExpenseService.java, BudgetService.java, ReportService.java
│
├── 📁 04_Controllers_HTTP_Servlets/                # [Layer 4] HTTP Servlets & Security Filter
│   ├── 📄 README.md                                # Guide to Servlet Lifecycle, Routing & Sessions
│   ├── AuthFilter.java, LoginServlet.java, RegisterServlet.java, LogoutServlet.java,
│   └── DashboardServlet.java, ExpenseListServlet.java, AddExpenseServlet.java,
│       EditExpenseServlet.java, DeleteExpenseServlet.java, BudgetServlet.java, ReportServlet.java
│
├── 📁 05_Utilities_Security_Firebase/              # [Layer 5] Cross-Cutting Utilities & Security
│   ├── 📄 README.md                                # Guide to BCrypt, Firebase Admin SDK, Validation
│   ├── FirebaseConfig.java, SessionUtil.java, PasswordUtil.java, ValidationUtil.java
│
├── 📁 06_Web_Views_JSP_Frontend/                   # [Layer 6] JSP Views & JSTL Templates
│   ├── 📄 README.md                                # Guide to JSPs, JSTL tags & Web Descriptor
│   ├── WEB-INF/web.xml, includes/ (header, sidebar, footer),
│   └── login.jsp, register.jsp, dashboard.jsp, expenses.jsp,
│       add-expense.jsp, edit-expense.jsp, budget.jsp, reports.jsp
│
├── 📁 07_Static_Assets_CSS_JS/                     # [Layer 7] Styling & Client-Side Scripting
│   ├── 📄 README.md                                # Guide to CSS3 Design System & Chart.js
│   ├── css/style.css, js/main.js
│
├── 📁 08_Instant_Browser_App_NoServer/             # [Layer 8] Standalone Browser App (Zero Setup)
│   ├── 📄 README.md                                # Guide to Instant Client-Side Prototype
│   ├── index.html, login.html, register.html, dashboard.html, expenses.html,
│   └── add-expense.html, edit-expense.html, budget.html, reports.html, js/storage.js
│
└── 📁 src/                                         # Standard Maven Build Tree
    └── main/ (java/ & webapp/)                     # Complete source tree for Tomcat WAR packaging
```

---

## 🚀 How to Run the Application

You have **two easy ways** to run and explore the application:

### 🟢 Method 1: Instant Browser Mode (No Server Needed — 2 Seconds)
Double-click [`index.html`](file:///c:/Users/bj873/OneDrive/Documents/case%20study/Expense_Tracking_JAVA_AntigravtiyStyle/index.html) or open [`08_Instant_Browser_App_NoServer/login.html`](file:///c:/Users/bj873/OneDrive/Documents/case%20study/Expense_Tracking_JAVA_AntigravtiyStyle/08_Instant_Browser_App_NoServer/login.html) in your browser.
* Pre-loaded demo account is ready.
* Click **Sign In** to immediately view charts, add/edit/delete expenses, and test budget limits right in your browser using `localStorage`.

---

### 🔵 Method 2: Full Java + Apache Tomcat Server Mode
1. Double-click [`run-server.bat`](file:///c:/Users/bj873/OneDrive/Documents/case%20study/Expense_Tracking_JAVA_AntigravtiyStyle/run-server.bat).
2. Open your browser and go to:
   👉 **`http://localhost:8080/expense-tracker/`**

---

## 🏛️ System Architecture Diagram (MVC)

```
Browser (HTTP Request)
  │
  ▼
[ 04_Controllers_HTTP_Servlets ]  ◄── (Protected by AuthFilter)
  │   (LoginServlet, ExpenseListServlet, DashboardServlet, etc.)
  ▼
[ 03_Business_Logic_Services ]
  │   (AuthService, ExpenseService, BudgetService, ReportService)
  ▼
[ 02_Database_Access_Firestore_DAO ]
  │   (UserDAO, ExpenseDAO, BudgetDAO)
  ▼
[ Google Cloud Firestore ]
      users/{userId}/expenses/{expenseId}
      users/{userId}/budgets/{budgetId}
  │
  ▼  (Data encapsulated in 01_Data_Models_Entities)
[ 06_Web_Views_JSP_Frontend ]
      (dashboard.jsp, expenses.jsp, budget.jsp, reports.jsp)
```

---

## 👥 4-Person Team Responsibilities Breakdown

| Member | Role | Assigned Functional Folders & Tasks |
| :--- | :--- | :--- |
| **Member 1** | **Authentication & Security Lead** | `05_Utilities_Security_Firebase` (PasswordUtil, SessionUtil), `04_Controllers_HTTP_Servlets` (AuthFilter, LoginServlet, RegisterServlet, LogoutServlet), and `UserDAO`. |
| **Member 2** | **Expense Management Lead** | `01_Data_Models_Entities` (Expense.java, Category.java), `02_Database_Access_Firestore_DAO` (ExpenseDAO), `03_Business_Logic_Services` (ExpenseService), and expense CRUD Servlets. |
| **Member 3** | **Budgeting & Analytics Lead** | `Budget.java`, `BudgetDAO`, `BudgetService`, `ReportService`, `BudgetServlet`, `ReportServlet`, and spending calculations. |
| **Member 4** | **UI/UX & Frontend Lead** | `06_Web_Views_JSP_Frontend` (JSPs & Includes), `07_Static_Assets_CSS_JS` (CSS styling & Chart.js integration), and `08_Instant_Browser_App_NoServer`. |

---

## 🎓 College Viva & Interview Guide

### 1. What is MVC architecture and how is it implemented in this project?
* **Model** (`01_Data_Models_Entities`): Contains data structures (`User`, `Expense`, `Budget`, `Category`).
* **View** (`06_Web_Views_JSP_Frontend`): JSP pages rendering HTML using JSTL and Expression Language (`${...}`).
* **Controller** (`04_Controllers_HTTP_Servlets`): Java Servlets that handle HTTP GET/POST, validate input, call Services, and forward to Views.

### 2. How is User Authentication and Data Security maintained?
* **Session Management**: When a user logs in, their `userId` is saved in `HttpSession` via `SessionUtil`.
* **Security Filter**: `AuthFilter` intercepts every request to protected URLs. If no session exists, the user is redirected to `/login`.
* **Password Hashing**: Passwords are encrypted with **BCrypt** with automatic salt generation (`PasswordUtil`).
* **User Data Isolation**: In Firestore, all user records live under `users/{userId}/expenses` and `users/{userId}/budgets`. Every DAO query scopes to the logged-in user's ID.

### 3. How do the interactive Dashboard Charts work?
* `ReportService` aggregates monthly expenses into category totals and daily trend maps.
* `Gson` serializes the data into JSON strings.
* JSPs embed these JSON strings and `main.js` initializes **Chart.js** Doughnut and Bar charts on HTML5 `<canvas>` elements.
