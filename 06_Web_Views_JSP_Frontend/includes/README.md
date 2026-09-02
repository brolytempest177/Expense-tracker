# 🧩 Layout Includes (`includes/`)

## 🎯 Purpose of This Subfolder
Contains modular, reusable JSP fragments included across pages to maintain UI consistency and eliminate duplicate HTML headers, sidebars, and footers.

---

## 📂 Files in This Folder

### 1. `header.jsp`
* **Purpose**: HTML `<head>` section containing meta tags, Google Fonts (`Plus Jakarta Sans`), stylesheet link (`css/style.css`), Chart.js script tag, and the layout wrapper.

### 2. `sidebar.jsp`
* **Purpose**: App navigation sidebar with links to Dashboard, Expenses, Add Expense, Budget, Reports, active page highlight indicator, user profile pill (name & email), and Logout button. Includes mobile navbar toggle.

### 3. `footer.jsp`
* **Purpose**: Closes layout container `</div>` and includes `js/main.js`.
