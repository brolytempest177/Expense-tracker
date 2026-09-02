<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Dashboard — ExpenseTracker" />
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/sidebar.jsp" %>

<main class="app-main">
    <div class="page-header">
        <div>
            <h1 class="page-title">Financial Dashboard</h1>
            <p class="page-subtitle">Welcome back, <strong><c:out value="${sessionScope.LOGGED_IN_USER.name}" /></strong>! Here is your spending summary.</p>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/add-expense" class="btn btn-primary">
                <span class="btn-icon">➕</span>
                <span>Add Expense</span>
            </a>
        </div>
    </div>

    <!-- Flash Notifications -->
    <c:if test="${not empty sessionScope.FLASH_SUCCESS}">
        <div class="alert alert-success">
            <span class="alert-icon">✅</span>
            <span><c:out value="${sessionScope.FLASH_SUCCESS}" /></span>
        </div>
        <c:remove var="FLASH_SUCCESS" scope="session" />
    </c:if>

    <c:if test="${not empty sessionScope.FLASH_ERROR}">
        <div class="alert alert-danger">
            <span class="alert-icon">⚠️</span>
            <span><c:out value="${sessionScope.FLASH_ERROR}" /></span>
        </div>
        <c:remove var="FLASH_ERROR" scope="session" />
    </c:if>

    <!-- Top KPI Cards Grid -->
    <div class="kpi-grid">
        <!-- Card 1: Total Spent -->
        <div class="kpi-card card-primary">
            <div class="kpi-icon-wrapper">
                <span class="kpi-icon">💸</span>
            </div>
            <div class="kpi-content">
                <span class="kpi-label">Total Spent This Month</span>
                <h3 class="kpi-value">₹<fmt:formatNumber value="${analytics.totalSpent}" minFractionDigits="2" maxFractionDigits="2" /></h3>
                <span class="kpi-meta">For Month: <strong>${currentMonth}/${currentYear}</strong></span>
            </div>
        </div>

        <!-- Card 2: Transaction Count -->
        <div class="kpi-card card-info">
            <div class="kpi-icon-wrapper">
                <span class="kpi-icon">🧾</span>
            </div>
            <div class="kpi-content">
                <span class="kpi-label">Total Transactions</span>
                <h3 class="kpi-value">${analytics.transactionCount}</h3>
                <span class="kpi-meta">Avg: ₹<fmt:formatNumber value="${analytics.averageTransaction}" minFractionDigits="2" maxFractionDigits="2" /> / txn</span>
            </div>
        </div>

        <!-- Card 3: Budget Status -->
        <div class="kpi-card ${budgetStatus.statusLevel eq 'EXCEEDED' ? 'card-danger' : (budgetStatus.statusLevel eq 'WARNING' ? 'card-warning' : 'card-success')}">
            <div class="kpi-icon-wrapper">
                <span class="kpi-icon">🎯</span>
            </div>
            <div class="kpi-content">
                <span class="kpi-label">Remaining Budget</span>
                <h3 class="kpi-value">
                    <c:choose>
                        <c:when test="${budgetStatus.budgetAmount > 0}">
                            ₹<fmt:formatNumber value="${budgetStatus.remainingAmount}" minFractionDigits="2" maxFractionDigits="2" />
                        </c:when>
                        <c:otherwise>
                            <span class="text-subtle">No budget set</span>
                        </c:otherwise>
                    </c:choose>
                </h3>
                <span class="kpi-meta">
                    <c:choose>
                        <c:when test="${budgetStatus.budgetAmount > 0}">
                            Budget: ₹<fmt:formatNumber value="${budgetStatus.budgetAmount}" minFractionDigits="2" maxFractionDigits="2" />
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/budget" class="text-link">Set a budget →</a>
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
        </div>

        <!-- Card 4: Highest Expense -->
        <div class="kpi-card card-purple">
            <div class="kpi-icon-wrapper">
                <span class="kpi-icon">⚡</span>
            </div>
            <div class="kpi-content">
                <span class="kpi-label">Highest Expense</span>
                <h3 class="kpi-value">
                    <c:choose>
                        <c:when test="${analytics.highestExpense != null}">
                            ₹<fmt:formatNumber value="${analytics.highestExpense.amount}" minFractionDigits="2" maxFractionDigits="2" />
                        </c:when>
                        <c:otherwise>
                            ₹0.00
                        </c:otherwise>
                    </c:choose>
                </h3>
                <span class="kpi-meta text-truncate">
                    <c:choose>
                        <c:when test="${analytics.highestExpense != null}">
                            <c:out value="${analytics.highestExpense.description}" />
                        </c:when>
                        <c:otherwise>
                            No records
                        </c:otherwise>
                    </c:choose>
                </span>
            </div>
        </div>
    </div>

    <!-- Budget Progress Bar (if budget is set) -->
    <c:if test="${budgetStatus.budgetAmount > 0}">
        <div class="card budget-progress-card">
            <div class="budget-progress-header">
                <div class="budget-info">
                    <h4>Monthly Budget Progress</h4>
                    <p class="text-muted">You have spent <strong>${budgetStatus.formattedPercentage}%</strong> of your ₹<fmt:formatNumber value="${budgetStatus.budgetAmount}" minFractionDigits="2" maxFractionDigits="2" /> budget.</p>
                </div>
                <div class="budget-badge-wrap">
                    <c:choose>
                        <c:when test="${budgetStatus.statusLevel eq 'EXCEEDED'}">
                            <span class="status-badge status-danger">Over Budget</span>
                        </c:when>
                        <c:when test="${budgetStatus.statusLevel eq 'WARNING'}">
                            <span class="status-badge status-warning">Near Limit (80%+)</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge status-success">On Track</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="progress-bar-container">
                <div class="progress-bar-fill ${budgetStatus.statusLevel eq 'EXCEEDED' ? 'bg-danger' : (budgetStatus.statusLevel eq 'WARNING' ? 'bg-warning' : 'bg-success')}" 
                     style="width: ${budgetStatus.percentageUsed}%;">
                </div>
            </div>
        </div>
    </c:if>

    <!-- Visual Charts Grid -->
    <div class="charts-grid">
        <!-- Category Doughnut Chart -->
        <div class="card chart-card">
            <div class="chart-header">
                <h3>Category Spending Breakdown</h3>
                <span class="text-muted">Month: ${currentMonth}/${currentYear}</span>
            </div>
            <div class="chart-wrapper">
                <c:choose>
                    <c:when test="${analytics.totalSpent > 0}">
                        <canvas id="categoryChart" height="260"></canvas>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-chart">
                            <span>📊</span>
                            <p>No expense data recorded for this month.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <!-- Daily Spending Trend Bar Chart -->
        <div class="card chart-card">
            <div class="chart-header">
                <h3>Daily Spending Trend</h3>
                <span class="text-muted">Daily breakdown</span>
            </div>
            <div class="chart-wrapper">
                <c:choose>
                    <c:when test="${analytics.totalSpent > 0}">
                        <canvas id="trendChart" height="260"></canvas>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-chart">
                            <span>📈</span>
                            <p>No spending trend data available yet.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Recent Expenses Table -->
    <div class="card table-card mt-4">
        <div class="table-card-header">
            <h3>Recent Transactions</h3>
            <a href="${pageContext.request.contextPath}/expenses" class="btn btn-ghost btn-sm">View All Expenses →</a>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Description</th>
                        <th>Category</th>
                        <th class="text-right">Amount</th>
                        <th class="text-center">Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty recentExpenses}">
                            <c:forEach var="exp" items="${recentExpenses}">
                                <tr>
                                    <td class="cell-date"><span class="date-badge"><c:out value="${exp.date}" /></span></td>
                                    <td class="cell-desc"><strong><c:out value="${exp.description}" /></strong></td>
                                    <td>
                                        <span class="category-badge cat-${exp.category.toLowerCase()}">
                                            <c:out value="${exp.category}" />
                                        </span>
                                    </td>
                                    <td class="text-right cell-amount">
                                        ₹<fmt:formatNumber value="${exp.amount}" minFractionDigits="2" maxFractionDigits="2" />
                                    </td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/edit-expense?id=${exp.id}" class="btn-icon-action" title="Edit">✏️</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="table-empty">
                                    <div class="empty-state">
                                        <p>No transactions yet. Start by adding your first expense!</p>
                                        <a href="${pageContext.request.contextPath}/add-expense" class="btn btn-primary btn-sm">Add Expense</a>
                                    </div>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>

<!-- Embed Chart JSON Data -->
<script>
    window.chartData = {
        categoryLabels: ${not empty analytics.categoryLabelsJson ? analytics.categoryLabelsJson : '[]'},
        categoryData: ${not empty analytics.categoryDataJson ? analytics.categoryDataJson : '[]'},
        categoryColors: ${not empty analytics.categoryColorsJson ? analytics.categoryColorsJson : '[]'},
        trendLabels: ${not empty analytics.trendLabelsJson ? analytics.trendLabelsJson : '[]'},
        trendData: ${not empty analytics.trendDataJson ? analytics.trendDataJson : '[]'}
    };
</script>

<%@ include file="/includes/footer.jsp" %>
