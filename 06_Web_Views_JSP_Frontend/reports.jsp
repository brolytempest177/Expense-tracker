<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Financial Reports & Analytics — ExpenseTracker" />
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/sidebar.jsp" %>

<main class="app-main">
    <div class="page-header">
        <div>
            <h1 class="page-title">Financial Analytics & Reports</h1>
            <p class="page-subtitle">Detailed breakdown and monthly spending trends analysis.</p>
        </div>
        <div class="header-actions no-print">
            <button onclick="window.print()" class="btn btn-secondary">
                <span class="btn-icon">🖨️</span>
                <span>Print Report</span>
            </button>
        </div>
    </div>

    <!-- Month / Year Selector Toolbar -->
    <div class="card toolbar-card no-print">
        <form action="${pageContext.request.contextPath}/reports" method="GET" class="filter-grid">
            <div class="filter-group">
                <label for="month">Report Month</label>
                <select id="month" name="month" class="form-select">
                    <option value="1" ${selectedMonth eq 1 ? 'selected' : ''}>January</option>
                    <option value="2" ${selectedMonth eq 2 ? 'selected' : ''}>February</option>
                    <option value="3" ${selectedMonth eq 3 ? 'selected' : ''}>March</option>
                    <option value="4" ${selectedMonth eq 4 ? 'selected' : ''}>April</option>
                    <option value="5" ${selectedMonth eq 5 ? 'selected' : ''}>May</option>
                    <option value="6" ${selectedMonth eq 6 ? 'selected' : ''}>June</option>
                    <option value="7" ${selectedMonth eq 7 ? 'selected' : ''}>July</option>
                    <option value="8" ${selectedMonth eq 8 ? 'selected' : ''}>August</option>
                    <option value="9" ${selectedMonth eq 9 ? 'selected' : ''}>September</option>
                    <option value="10" ${selectedMonth eq 10 ? 'selected' : ''}>October</option>
                    <option value="11" ${selectedMonth eq 11 ? 'selected' : ''}>November</option>
                    <option value="12" ${selectedMonth eq 12 ? 'selected' : ''}>December</option>
                </select>
            </div>

            <div class="filter-group">
                <label for="year">Report Year</label>
                <input type="number" id="year" name="year" value="${selectedYear}" min="2020" max="2035" class="form-input">
            </div>

            <div class="filter-actions">
                <button type="submit" class="btn btn-primary">Generate Report</button>
            </div>
        </form>
    </div>

    <!-- Monthly Summary KPIs -->
    <div class="kpi-grid">
        <div class="kpi-card card-primary">
            <div class="kpi-icon-wrapper"><span class="kpi-icon">💰</span></div>
            <div class="kpi-content">
                <span class="kpi-label">Total Monthly Expenses</span>
                <h3 class="kpi-value">₹<fmt:formatNumber value="${analytics.totalSpent}" minFractionDigits="2" maxFractionDigits="2" /></h3>
                <span class="kpi-meta">For ${selectedMonth}/${selectedYear}</span>
            </div>
        </div>

        <div class="kpi-card card-info">
            <div class="kpi-icon-wrapper"><span class="kpi-icon">🔢</span></div>
            <div class="kpi-content">
                <span class="kpi-label">Total Transactions</span>
                <h3 class="kpi-value">${analytics.transactionCount}</h3>
                <span class="kpi-meta">Recorded entries</span>
            </div>
        </div>

        <div class="kpi-card card-purple">
            <div class="kpi-icon-wrapper"><span class="kpi-icon">⚡</span></div>
            <div class="kpi-content">
                <span class="kpi-label">Highest Expense</span>
                <h3 class="kpi-value">
                    <c:choose>
                        <c:when test="${analytics.highestExpense != null}">
                            ₹<fmt:formatNumber value="${analytics.highestExpense.amount}" minFractionDigits="2" maxFractionDigits="2" />
                        </c:when>
                        <c:otherwise>₹0.00</c:otherwise>
                    </c:choose>
                </h3>
                <span class="kpi-meta text-truncate">
                    <c:choose>
                        <c:when test="${analytics.highestExpense != null}">
                            <c:out value="${analytics.highestExpense.description}" />
                        </c:when>
                        <c:otherwise>No transactions</c:otherwise>
                    </c:choose>
                </span>
            </div>
        </div>

        <div class="kpi-card card-success">
            <div class="kpi-icon-wrapper"><span class="kpi-icon">📊</span></div>
            <div class="kpi-content">
                <span class="kpi-label">Average Spending / Txn</span>
                <h3 class="kpi-value">₹<fmt:formatNumber value="${analytics.averageTransaction}" minFractionDigits="2" maxFractionDigits="2" /></h3>
                <span class="kpi-meta">Per transaction average</span>
            </div>
        </div>
    </div>

    <!-- Charts Section -->
    <div class="charts-grid">
        <div class="card chart-card">
            <div class="chart-header">
                <h3>Category Distribution</h3>
                <span class="text-muted">Proportional spending</span>
            </div>
            <div class="chart-wrapper">
                <c:choose>
                    <c:when test="${analytics.totalSpent > 0}">
                        <canvas id="categoryChart" height="260"></canvas>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-chart">
                            <p>No data recorded for this month.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="card chart-card">
            <div class="chart-header">
                <h3>Daily Spending Trends</h3>
                <span class="text-muted">Timeline flow</span>
            </div>
            <div class="chart-wrapper">
                <c:choose>
                    <c:when test="${analytics.totalSpent > 0}">
                        <canvas id="trendChart" height="260"></canvas>
                    </c:when>
                    <c:otherwise>
                        <div class="empty-chart">
                            <p>No transactions to chart.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <!-- Detailed Category Breakdown Table -->
    <div class="card table-card mt-4">
        <div class="table-card-header">
            <h3>Category-Wise Spending Breakdown</h3>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Category</th>
                        <th class="text-right">Total Amount</th>
                        <th class="text-right">% of Total</th>
                        <th>Spending Intensity</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="entry" items="${analytics.categoryTotals}">
                        <c:set var="pct" value="${categoryPercentages[entry.key]}" />
                        <tr>
                            <td>
                                <span class="category-badge cat-${entry.key.toLowerCase()}">
                                    <c:out value="${entry.key}" />
                                </span>
                            </td>
                            <td class="text-right font-medium">
                                ₹<fmt:formatNumber value="${entry.value}" minFractionDigits="2" maxFractionDigits="2" />
                            </td>
                            <td class="text-right font-medium">
                                <fmt:formatNumber value="${pct}" minFractionDigits="1" maxFractionDigits="1" />%
                            </td>
                            <td>
                                <div class="progress-bar-container progress-bar-sm">
                                    <div class="progress-bar-fill" style="width: ${pct}%;"></div>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
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
