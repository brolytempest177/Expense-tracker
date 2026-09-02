/**
 * Java Expense Tracker — Frontend JavaScript
 */

document.addEventListener('DOMContentLoaded', () => {
    // 1. Mobile Sidebar Navigation Toggle
    const mobileToggle = document.getElementById('mobileMenuToggle');
    const sidebar = document.getElementById('appSidebar');

    if (mobileToggle && sidebar) {
        mobileToggle.addEventListener('click', () => {
            sidebar.classList.toggle('open');
        });

        // Close sidebar when clicking outside on mobile
        document.addEventListener('click', (e) => {
            if (window.innerWidth <= 992) {
                if (!sidebar.contains(e.target) && !mobileToggle.contains(e.target) && sidebar.classList.contains('open')) {
                    sidebar.classList.remove('open');
                }
            }
        });
    }

    // 2. Initialize Charts if chart data is available
    if (window.chartData) {
        initCategoryChart();
        initTrendChart();
    }

    // 3. Register Form Validation
    const registerForm = document.getElementById('registerForm');
    if (registerForm) {
        registerForm.addEventListener('submit', (e) => {
            const password = document.getElementById('password').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            if (password.length < 6) {
                alert('Password must be at least 6 characters long.');
                e.preventDefault();
                return;
            }

            if (password !== confirmPassword) {
                alert('Passwords do not match. Please re-enter your password.');
                e.preventDefault();
            }
        });
    }

    // 4. Expense Form Client-side Validation
    const expenseForm = document.getElementById('expenseForm') || document.getElementById('editExpenseForm');
    if (expenseForm) {
        expenseForm.addEventListener('submit', (e) => {
            const amountInput = document.getElementById('amount');
            const amountVal = parseFloat(amountInput.value);

            if (isNaN(amountVal) || amountVal <= 0) {
                alert('Please enter a valid amount greater than zero.');
                amountInput.focus();
                e.preventDefault();
            }
        });
    }
});

/**
 * Initializes the Category Spending Doughnut Chart
 */
function initCategoryChart() {
    const ctx = document.getElementById('categoryChart');
    if (!ctx) return;

    const labels = window.chartData.categoryLabels || [];
    const data = window.chartData.categoryData || [];
    const colors = window.chartData.categoryColors || [];

    if (labels.length === 0 || data.length === 0) return;

    new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: colors,
                borderWidth: 2,
                borderColor: '#ffffff',
                hoverOffset: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'bottom',
                    labels: {
                        boxWidth: 12,
                        font: {
                            family: "'Plus Jakarta Sans', sans-serif",
                            size: 12,
                            weight: '600'
                        },
                        padding: 14
                    }
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const val = context.parsed;
                            return ` ₹${val.toFixed(2)}`;
                        }
                    }
                }
            },
            cutout: '68%'
        }
    });
}

/**
 * Initializes the Daily Spending Trend Bar Chart
 */
function initTrendChart() {
    const ctx = document.getElementById('trendChart');
    if (!ctx) return;

    const labels = window.chartData.trendLabels || [];
    const data = window.chartData.trendData || [];

    if (labels.length === 0 || data.length === 0) return;

    new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: 'Spent (₹)',
                data: data,
                backgroundColor: 'rgba(79, 70, 229, 0.75)',
                borderColor: '#4f46e5',
                borderWidth: 1.5,
                borderRadius: 6,
                maxBarThickness: 32
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {
                        color: '#f1f5f9'
                    },
                    ticks: {
                        callback: function(value) {
                            return '₹' + value;
                        },
                        font: {
                            family: "'Plus Jakarta Sans', sans-serif",
                            size: 11
                        }
                    }
                },
                x: {
                    grid: {
                        display: false
                    },
                    ticks: {
                        font: {
                            family: "'Plus Jakarta Sans', sans-serif",
                            size: 11
                        }
                    }
                }
            },
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return ` ₹${context.parsed.y.toFixed(2)}`;
                        }
                    }
                }
            }
        }
    });
}

/**
 * Global helper for confirming expense deletion
 */
function confirmDelete(description, amount) {
    return confirm(`Are you sure you want to delete the expense: "${description}" (${amount})?\nThis action cannot be undone.`);
}
