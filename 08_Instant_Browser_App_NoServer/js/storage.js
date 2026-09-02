/**
 * AppStorage — Client-side persistent data management using LocalStorage.
 * Starts completely clean with empty data so you can add your own real expenses.
 */

const AppStorage = {
    USER_KEY: 'ET_USER',
    EXPENSES_KEY: 'ET_EXPENSES',
    BUDGETS_KEY: 'ET_BUDGETS',

    CATEGORIES: ['Food', 'Transport', 'Education', 'Shopping', 'Entertainment', 'Bills', 'Health', 'Other'],

    CATEGORY_COLORS: {
        'Food': '#f59e0b',
        'Transport': '#3b82f6',
        'Education': '#8b5cf6',
        'Shopping': '#ec4899',
        'Entertainment': '#10b981',
        'Bills': '#ef4444',
        'Health': '#06b6d4',
        'Other': '#64748b'
    },

    init() {
        // Initialize empty expenses list if not yet set
        if (!localStorage.getItem(this.EXPENSES_KEY)) {
            localStorage.setItem(this.EXPENSES_KEY, JSON.stringify([]));
        }

        // Initialize empty budgets if not yet set
        if (!localStorage.getItem(this.BUDGETS_KEY)) {
            localStorage.setItem(this.BUDGETS_KEY, JSON.stringify({}));
        }
    },

    clearAllData() {
        localStorage.removeItem(this.EXPENSES_KEY);
        localStorage.removeItem(this.BUDGETS_KEY);
        localStorage.removeItem(this.USER_KEY);
        sessionStorage.removeItem('ET_LOGGED_IN');
        this.init();
    },

    getCurrentUser() {
        this.init();
        const user = localStorage.getItem(this.USER_KEY);
        return user ? JSON.parse(user) : null;
    },

    login(email, password) {
        this.init();
        const user = this.getCurrentUser();
        if (user && user.email.toLowerCase() === email.toLowerCase() && user.password === password) {
            sessionStorage.setItem('ET_LOGGED_IN', 'true');
            return { success: true, user };
        }
        // Auto-create account for testing if valid
        if (email && password && password.length >= 6) {
            const name = email.split('@')[0];
            const formattedName = name.charAt(0).toUpperCase() + name.slice(1);
            const newUser = { id: 'usr_' + Date.now(), name: formattedName, email, password };
            localStorage.setItem(this.USER_KEY, JSON.stringify(newUser));
            sessionStorage.setItem('ET_LOGGED_IN', 'true');
            return { success: true, user: newUser };
        }
        return { success: false, message: 'Invalid email or password (min 6 characters required).' };
    },

    register(name, email, password) {
        this.init();
        if (!name || !email || !password || password.length < 6) {
            return { success: false, message: 'Please enter valid details (password min 6 chars).' };
        }
        const newUser = { id: 'usr_' + Date.now(), name, email, password };
        localStorage.setItem(this.USER_KEY, JSON.stringify(newUser));
        sessionStorage.setItem('ET_LOGGED_IN', 'true');
        return { success: true, user: newUser };
    },

    logout() {
        sessionStorage.removeItem('ET_LOGGED_IN');
        window.location.href = 'login.html';
    },

    getExpenses() {
        this.init();
        const raw = localStorage.getItem(this.EXPENSES_KEY);
        return raw ? JSON.parse(raw) : [];
    },

    addExpense(expense) {
        const expenses = this.getExpenses();
        expense.id = 'exp_' + Date.now();
        expense.amount = parseFloat(expense.amount);
        expenses.unshift(expense);
        localStorage.setItem(this.EXPENSES_KEY, JSON.stringify(expenses));
        return expense;
    },

    getExpenseById(id) {
        const expenses = this.getExpenses();
        return expenses.find(e => e.id === id) || null;
    },

    updateExpense(id, updatedData) {
        const expenses = this.getExpenses();
        const index = expenses.findIndex(e => e.id === id);
        if (index !== -1) {
            updatedData.amount = parseFloat(updatedData.amount);
            expenses[index] = { ...expenses[index], ...updatedData };
            localStorage.setItem(this.EXPENSES_KEY, JSON.stringify(expenses));
            return true;
        }
        return false;
    },

    deleteExpense(id) {
        let expenses = this.getExpenses();
        expenses = expenses.filter(e => e.id !== id);
        localStorage.setItem(this.EXPENSES_KEY, JSON.stringify(expenses));
        return true;
    },

    getBudget(month, year) {
        this.init();
        const budgets = JSON.parse(localStorage.getItem(this.BUDGETS_KEY) || '{}');
        return budgets[`${year}-${month}`] || 0;
    },

    setBudget(month, year, amount) {
        this.init();
        const budgets = JSON.parse(localStorage.getItem(this.BUDGETS_KEY) || '{}');
        budgets[`${year}-${month}`] = parseFloat(amount);
        localStorage.setItem(this.BUDGETS_KEY, JSON.stringify(budgets));
        return true;
    },

    getAllBudgets() {
        this.init();
        const budgets = JSON.parse(localStorage.getItem(this.BUDGETS_KEY) || '{}');
        const list = [];
        for (const [key, val] of Object.entries(budgets)) {
            const [y, m] = key.split('-');
            list.push({ year: parseInt(y), month: parseInt(m), amount: val });
        }
        return list;
    },

    getMonthlyAnalytics(month, year) {
        const expenses = this.getExpenses();
        const filtered = expenses.filter(e => {
            if (!e.date) return false;
            const [ey, em] = e.date.split('-');
            return parseInt(ey) === year && parseInt(em) === month;
        });

        let totalSpent = 0;
        let highest = null;
        let maxAmt = -1;
        const categoryMap = {};
        this.CATEGORIES.forEach(c => categoryMap[c] = 0);
        const dailyMap = {};

        filtered.forEach(e => {
            const amt = parseFloat(e.amount) || 0;
            totalSpent += amt;
            if (amt > maxAmt) {
                maxAmt = amt;
                highest = e;
            }
            if (categoryMap[e.category] !== undefined) {
                categoryMap[e.category] += amt;
            } else {
                categoryMap[e.category] = amt;
            }
            dailyMap[e.date] = (dailyMap[e.date] || 0) + amt;
        });

        const count = filtered.length;
        const avg = count > 0 ? (totalSpent / count) : 0;
        const budgetAmt = this.getBudget(month, year);
        const remaining = budgetAmt > 0 ? (budgetAmt - totalSpent) : 0;
        const pctUsed = budgetAmt > 0 ? Math.min((totalSpent / budgetAmt) * 100, 100) : 0;

        let statusLevel = 'SAFE';
        if (budgetAmt > 0) {
            if (totalSpent > budgetAmt) statusLevel = 'EXCEEDED';
            else if (totalSpent >= 0.8 * budgetAmt) statusLevel = 'WARNING';
        } else {
            statusLevel = 'NO_BUDGET';
        }

        return {
            month,
            year,
            totalSpent,
            transactionCount: count,
            averageTransaction: avg,
            highestExpense: highest,
            categoryTotals: categoryMap,
            dailyTotals: dailyMap,
            budgetAmount: budgetAmt,
            remainingBudget: remaining,
            percentageUsed: pctUsed,
            statusLevel,
            expenses: filtered
        };
    }
};

// Initialize
AppStorage.init();
