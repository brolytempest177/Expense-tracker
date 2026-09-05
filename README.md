# 💸 Java Expense Tracker — Full-Stack Educational Web Application
### *Organized by Architectural Layers & Functional Modules (MySQL + Servlets + JSP)*

Welcome to the **Java Expense Tracker** project! This project is organized with functional folder names, dedicated folder guides, and complete source code to make understanding, presenting, and defending the project in your college viva effortless.

---

## 🗂️ Project Structure & Functional Modules

Each directory in this repository represents a distinct functional layer of the application and contains its own dedicated **`README.md`** explaining the code inside:

```
Expense_Tracking_JAVA_AntigravtiyStyle/
│
├── 📄 pom.xml                                      # Maven Project Configuration (MySQL + HikariCP)
├── 📄 README.md                                    # Master Project Documentation & Viva Guide
├── 📄 schema.sql                                   # MySQL DDL Schema, Tables, Indexes & Seed Data
├── 📄 index.html                                   # 1-Click Instant Browser App Launcher
├── ⚙️ run-server.bat                               # 1-Click Apache Tomcat Server Launcher (Auto-Deploys WAR)
├── ⚙️ stop-server.bat                              # 1-Click Apache Tomcat Server Stopper
│
├── 📁 01_Data_Models_Entities/                     # [Layer 1] Data Models & POJO Entities
│   ├── 📄 README.md                                # Guide to User, Expense, Budget, Category models
│   ├── User.java, Expense.java, Budget.java, Category.java
│
├── 📁 02_Database_Access_MySQL_DAO/                # [Layer 2] MySQL Relational Data Access (DAOs)
│   ├── 📄 README.md                                # Guide to JDBC, PreparedStatements & User Data Isolation
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
├── 📁 05_Utilities_Security_Database/              # [Layer 5] Cross-Cutting Utilities, Security & DB Pooling
│   ├── 📄 README.md                                # Guide to BCrypt, DBUtil, HikariCP, Validation
│   ├── DBUtil.java, SessionUtil.java, PasswordUtil.java, ValidationUtil.java
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
    └── main/
        ├── java/                                   # Complete Java source tree
        ├── resources/                              # db.properties (database configuration)
        └── webapp/                                 # JSPs, WEB-INF/web.xml, static assets
```

---

## 🗄️ Database Setup (MySQL)

### Option A: Automatic Setup (Zero Friction)
1. Ensure your local MySQL server is running (e.g. start MySQL in **XAMPP** Control Panel or MySQL Service).
2. The application is preconfigured for `localhost:3306` with user `root` and an empty password.
3. On application startup, **`DBUtil` will automatically verify and create all required tables** (`users`, `expenses`, `budgets`) if they do not already exist!

### Option B: Manual Setup via `schema.sql`
If your professor or evaluator requests importing the database through **phpMyAdmin** or **MySQL Command Line**:
1. Open phpMyAdmin (`http://localhost/phpmyadmin`) or open your MySQL terminal.
2. Import or run the [`schema.sql`](file:///c:/Users/bj873/OneDrive/Documents/case%20study/Expense_Tracking_JAVA_AntigravtiyStyle/schema.sql) file located in the root of the project:
   ```bash
   mysql -u root -p < schema.sql
   ```
3. This creates the `expense_tracker_db` database and loads demo seed accounts and sample expenses.

### Database Credentials Customization
To configure custom database credentials (e.g., if your MySQL has a password), simply edit:
👉 [`src/main/resources/db.properties`](file:///c:/Users/bj873/OneDrive/Documents/case%20study/Expense_Tracking_JAVA_AntigravtiyStyle/src/main/resources/db.properties)
```properties
db.url=jdbc:mysql://localhost:3306/expense_tracker_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&createDatabaseIfNotExist=true
db.user=root
db.password=your_password_here
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
1. Ensure MySQL is started in XAMPP.
2. Double-click [`run-server.bat`](file:///c:/Users/bj873/OneDrive/Documents/case%20study/Expense_Tracking_JAVA_AntigravtiyStyle/run-server.bat).
3. Open your browser and go to:
   👉 **`http://localhost:8080/expense-tracker/`**

> **Default Seed Login Credentials:**
> - **Email**: `demo@expensetracker.com`
> - **Password**: `password123`

---

## 🏛️ System Architecture Diagram (MVC + DAO Pattern)

```
Browser (HTTP Request)
  │
  ▼
[ 04_Controllers_HTTP_Servlets ]  ◄── (Protected by AuthFilter & SessionUtil)
  │   (LoginServlet, ExpenseListServlet, DashboardServlet, etc.)
  ▼
[ 03_Business_Logic_Services ]
  │   (AuthService, ExpenseService, BudgetService, ReportService)
  ▼
[ 02_Database_Access_MySQL_DAO ]  ◄── (HikariCP Connection Pool in 05_Utilities)
  │   (UserDAO, ExpenseDAO, BudgetDAO)
  ▼
[ MySQL Relational Database ]
      ├── users (user_id PK, name, email UNIQUE, password_hash, created_at)
      ├── expenses (id PK, user_id FK, amount, description, category, expense_date, notes)
      └── budgets (id PK, user_id FK, month, year, amount, updated_at)
  │
  ▼  (Data encapsulated in 01_Data_Models_Entities)
[ 06_Web_Views_JSP_Frontend ]
      (dashboard.jsp, expenses.jsp, budget.jsp, reports.jsp)
```

---

## 👥 4-Person Team Responsibilities Breakdown

| Member | Role | Assigned Functional Folders & Tasks |
| :--- | :--- | :--- |
| **Member 1** | **Authentication & Security Lead** | `05_Utilities_Security_Database` (PasswordUtil, SessionUtil), `04_Controllers_HTTP_Servlets` (AuthFilter, LoginServlet, RegisterServlet, LogoutServlet), and `UserDAO`. |
| **Member 2** | **Expense Management Lead** | `01_Data_Models_Entities` (Expense.java, Category.java), `02_Database_Access_MySQL_DAO` (ExpenseDAO), `03_Business_Logic_Services` (ExpenseService), and expense CRUD Servlets. |
| **Member 3** | **Budgeting & Analytics Lead** | `Budget.java`, `BudgetDAO`, `BudgetService`, `ReportService`, `BudgetServlet`, `ReportServlet`, and spending calculations. |
| **Member 4** | **UI/UX & Frontend Lead** | `06_Web_Views_JSP_Frontend` (JSPs & Includes), `07_Static_Assets_CSS_JS` (CSS styling & Chart.js integration), and `08_Instant_Browser_App_NoServer`. |

---

## 🎓 College Viva & Interview Guide

### 1. What is MVC architecture and how is it implemented in this project?
* **Model** (`01_Data_Models_Entities`): Contains pure Java POJO data structures (`User`, `Expense`, `Budget`, `Category`).
* **View** (`06_Web_Views_JSP_Frontend`): JSP pages rendering dynamic HTML using JSTL and Expression Language (`${...}`).
* **Controller** (`04_Controllers_HTTP_Servlets`): Java Servlets that handle HTTP GET/POST, validate input, call Services, and forward to Views.

### 2. What is the DAO Design Pattern and why is it beneficial?
* The **DAO (Data Access Object)** pattern isolates database operations from business logic.
* In our project, switching from Firebase to MySQL only required rewriting `UserDAO`, `ExpenseDAO`, and `BudgetDAO`. No Servlets, Services, or JSP views had to be modified.

### 3. How is SQL Injection prevented?
* Every query uses **Parameterized Queries** through `java.sql.PreparedStatement` (`?` placeholders).
* The SQL command is precompiled by the MySQL engine before parameters are bound, preventing attackers from injecting arbitrary SQL commands through form inputs.

### 4. Why use Connection Pooling (HikariCP) instead of `DriverManager`?
* Opening a new database TCP connection for every incoming web request causes severe latency (network handshake, authentication, socket allocation).
* **HikariCP** maintains a pool of pre-opened, active connections ready to be reused, drastically increasing throughput and lowering response times.

### 5. What Normalization forms does the MySQL schema satisfy?
* **1NF (First Normal Form)**: Each table has a primary key and all columns store atomic, indivisible values.
* **2NF (Second Normal Form)**: All non-key attributes are fully dependent on the primary key (no partial dependencies).
* **3NF (Third Normal Form)**: No transitive dependencies; attributes depend only on the primary key (e.g. user details are kept in `users` and referenced by `user_id` foreign key in `expenses`, eliminating redundancy).

### 6. How is User Authentication and Data Privacy guaranteed?
* **Session Management**: After login, `userId` is saved in the server's `HttpSession`.
* **Security Filter**: `AuthFilter` intercepts every protected servlet route and redirects unauthenticated users to `/login`.
* **Password Hashing**: Passwords are never stored in plain text; they are encrypted using **BCrypt** with automatic salt generation (`PasswordUtil`).
* **Strict User Isolation**: Every database query (`SELECT`, `UPDATE`, `DELETE`) enforces `WHERE user_id = ?`, making cross-account data leakage impossible.
