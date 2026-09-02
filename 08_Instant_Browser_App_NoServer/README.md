# ⚡ 08 — Instant Browser App (Zero Server Setup)

## 🎯 Purpose of This Folder
This folder contains a **100% standalone, pure HTML5/CSS3/JavaScript** version of the entire Expense Tracker web application. 

It requires **NO Tomcat**, **NO Java Servlets**, and **NO database setup** to run. You can simply double-click `login.html` or `index.html` to test and demonstrate the complete user interface and all features right in your browser!

---

## 🚀 How to Run It (2 Seconds)
1. Double-click [`login.html`](file:///c:/Users/bj873/OneDrive/Documents/case%20study/Expense_Tracking_JAVA_AntigravtiyStyle/08_Instant_Browser_App_NoServer/login.html) in Windows File Explorer.
2. Click **Sign In** (a pre-filled demo account is already set up).
3. The live dashboard will load with pre-seeded sample transactions and charts!

---

## 📂 Files in This Folder

* **`login.html` & `register.html`**: Instant authentication screens saving user session in `localStorage`.
* **`dashboard.html`**: Real-time KPI summary cards, interactive Chart.js Doughnut and Bar charts, and budget meter.
* **`expenses.html`**: Full CRUD transaction table with live keyword search, category filtering, date filters, sorting, edit, and delete with confirmation dialogs.
* **`add-expense.html` & `edit-expense.html`**: Forms for recording new expenses and updating existing entries.
* **`budget.html`**: Monthly budget targets and live spending meter calculation.
* **`reports.html`**: Analytics breakdown and print-ready summary.
* **`js/storage.js`**: Client-side database emulator managing expenses, users, and budgets in browser `localStorage`.
* **`css/style.css`**: Full responsive design system.
