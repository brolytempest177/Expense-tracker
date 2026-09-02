<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="Monthly Budget — ExpenseTracker" />
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/sidebar.jsp" %>

<main class="app-main">
    <div class="page-header">
        <div>
            <h1 class="page-title">Budget Management</h1>
            <p class="page-subtitle">Plan and monitor your spending limits for each month.</p>
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

    <!-- Month/Year Selector Toolbar -->
    <div class="card toolbar-card">
        <form action="${pageContext.request.contextPath}/budget" method="GET" class="filter-grid">
            <div class="filter-group">
                <label for="month">Select Month</label>
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
                <label for="year">Select Year</label>
                <input type="number" id="year" name="year" value="${selectedYear}" min="2020" max="2035" class="form-input">
            </div>

            <div class="filter-actions">
                <button type="submit" class="btn btn-secondary">Switch Month</button>
            </div>
        </form>
    </div>

    <!-- Budget Status Grid -->
    <div class="grid-2-col">
        <!-- Card 1: Set / Update Budget Form -->
        <div class="card form-card">
            <div class="card-header-styled">
                <h3>🎯 Set Budget for Month ${selectedMonth}/${selectedYear}</h3>
                <p class="text-muted">Define your maximum expenditure target for this month.</p>
            </div>

            <form action="${pageContext.request.contextPath}/budget" method="POST" id="budgetForm" class="mt-3">
                <input type="hidden" name="month" value="${selectedMonth}">
                <input type="hidden" name="year" value="${selectedYear}">

                <div class="form-group">
                    <label for="amount">Budget Amount (₹) <span class="required">*</span></label>
                    <div class="input-wrapper">
                        <span class="input-icon">₹</span>
                        <input type="number" id="amount" name="amount" step="0.01" min="0.01" 
                               value="${currentBudget != null ? currentBudget.amount : ''}" 
                               placeholder="e.g. 25000.00" required autofocus>
                    </div>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <span>${currentBudget != null ? 'Update Budget' : 'Save Budget'}</span>
                    </button>
                </div>
            </form>
        </div>

        <!-- Card 2: Current Spending vs Budget Progress -->
        <div class="card status-overview-card">
            <div class="card-header-styled">
                <h3>📊 Spending vs Target</h3>
                <p class="text-muted">Live overview of your spending status.</p>
            </div>

            <div class="budget-metrics-list mt-3">
                <div class="metric-row">
                    <span class="metric-title">Allocated Budget:</span>
                    <span class="metric-val">
                        <c:choose>
                            <c:when test="${budgetStatus.budgetAmount > 0}">
                                ₹<fmt:formatNumber value="${budgetStatus.budgetAmount}" minFractionDigits="2" maxFractionDigits="2" />
                            </c:when>
                            <c:otherwise>
                                <span class="text-subtle">Not Set</span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="metric-row">
                    <span class="metric-title">Total Spent:</span>
                    <span class="metric-val stat-danger">
                        ₹<fmt:formatNumber value="${budgetStatus.spentAmount}" minFractionDigits="2" maxFractionDigits="2" />
                    </span>
                </div>

                <div class="metric-row">
                    <span class="metric-title">Remaining Balance:</span>
                    <span class="metric-val ${budgetStatus.remainingAmount < 0 ? 'stat-danger' : 'stat-success'}">
                        ₹<fmt:formatNumber value="${budgetStatus.remainingAmount}" minFractionDigits="2" maxFractionDigits="2" />
                    </span>
                </div>

                <div class="metric-row">
                    <span class="metric-title">Budget Used:</span>
                    <span class="metric-val"><strong>${budgetStatus.formattedPercentage}%</strong></span>
                </div>
            </div>

            <c:if test="${budgetStatus.budgetAmount > 0}">
                <div class="progress-bar-container mt-4">
                    <div class="progress-bar-fill ${budgetStatus.statusLevel eq 'EXCEEDED' ? 'bg-danger' : (budgetStatus.statusLevel eq 'WARNING' ? 'bg-warning' : 'bg-success')}" 
                         style="width: ${budgetStatus.percentageUsed}%;">
                    </div>
                </div>
                <div class="budget-alert-box mt-3">
                    <c:choose>
                        <c:when test="${budgetStatus.statusLevel eq 'EXCEEDED'}">
                            <div class="alert alert-danger">
                                <span>⚠️ You have exceeded your budget by ₹<fmt:formatNumber value="${-budgetStatus.remainingAmount}" minFractionDigits="2" maxFractionDigits="2" />!</span>
                            </div>
                        </c:when>
                        <c:when test="${budgetStatus.statusLevel eq 'WARNING'}">
                            <div class="alert alert-warning">
                                <span>⚠️ Caution: You have utilized over 80% of your allocated budget.</span>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-success">
                                <span>✅ Great job! Your spending is well within your budget limit.</span>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Historical Budgets Table -->
    <div class="card table-card mt-4">
        <div class="table-card-header">
            <h3>Budget History</h3>
        </div>
        <div class="table-responsive">
            <table class="data-table">
                <thead>
                    <tr>
                        <th>Month & Year</th>
                        <th>Target Budget</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty allBudgets}">
                            <c:forEach var="b" items="${allBudgets}">
                                <tr>
                                    <td><strong>${b.monthName} ${b.year}</strong></td>
                                    <td>₹<fmt:formatNumber value="${b.amount}" minFractionDigits="2" maxFractionDigits="2" /></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/budget?month=${b.month}&year=${b.year}" 
                                           class="btn btn-ghost btn-sm">View Details →</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3" class="table-empty">
                                    <p class="text-muted">No budgets recorded yet.</p>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>

<%@ include file="/includes/footer.jsp" %>
