<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="pageTitle" value="All Expenses — ExpenseTracker" />
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/sidebar.jsp" %>

<main class="app-main">
    <div class="page-header">
        <div>
            <h1 class="page-title">Expense Management</h1>
            <p class="page-subtitle">Track, filter, search, and manage all your daily transactions.</p>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/add-expense" class="btn btn-primary">
                <span class="btn-icon">➕</span>
                <span>Add Expense</span>
            </a>
        </div>
    </div>

    <!-- Alert Notifications -->
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

    <!-- Filters & Search Toolbar -->
    <div class="card toolbar-card">
        <form action="${pageContext.request.contextPath}/expenses" method="GET" class="filter-grid" id="expenseFilterForm">
            <div class="filter-group filter-search">
                <label for="query">Search</label>
                <div class="input-wrapper">
                    <span class="input-icon">🔍</span>
                    <input type="text" id="query" name="query" value="<c:out value='${paramQuery}' />" 
                           placeholder="Search description or notes...">
                </div>
            </div>

            <div class="filter-group">
                <label for="category">Category</label>
                <select id="category" name="category" class="form-select">
                    <option value="ALL">All Categories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat}" ${paramCategory eq cat ? 'selected' : ''}>${cat}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="filter-group">
                <label for="startDate">From Date</label>
                <input type="date" id="startDate" name="startDate" value="<c:out value='${paramStartDate}' />" class="form-input">
            </div>

            <div class="filter-group">
                <label for="endDate">To Date</label>
                <input type="date" id="endDate" name="endDate" value="<c:out value='${paramEndDate}' />" class="form-input">
            </div>

            <div class="filter-group">
                <label for="sortBy">Sort By</label>
                <select id="sortBy" name="sortBy" class="form-select">
                    <option value="date_desc" ${paramSortBy eq 'date_desc' ? 'selected' : ''}>Date (Newest first)</option>
                    <option value="date_asc" ${paramSortBy eq 'date_asc' ? 'selected' : ''}>Date (Oldest first)</option>
                    <option value="amount_desc" ${paramSortBy eq 'amount_desc' ? 'selected' : ''}>Amount (High to Low)</option>
                    <option value="amount_asc" ${paramSortBy eq 'amount_asc' ? 'selected' : ''}>Amount (Low to High)</option>
                    <option value="description_asc" ${paramSortBy eq 'description_asc' ? 'selected' : ''}>Description (A-Z)</option>
                </select>
            </div>

            <div class="filter-actions">
                <button type="submit" class="btn btn-secondary">Apply Filters</button>
                <a href="${pageContext.request.contextPath}/expenses" class="btn btn-ghost">Reset</a>
            </div>
        </form>
    </div>

    <!-- Summary Stats Bar -->
    <div class="stats-ribbon">
        <div class="stat-pill">
            <span class="stat-pill-label">Total Transactions:</span>
            <span class="stat-pill-value">${totalCount}</span>
        </div>
        <div class="stat-pill">
            <span class="stat-pill-label">Filtered Amount:</span>
            <span class="stat-pill-value stat-highlight">₹<fmt:formatNumber value="${totalFilteredAmount}" minFractionDigits="2" maxFractionDigits="2" /></span>
        </div>
    </div>

    <!-- Expenses Table Card -->
    <div class="card table-card">
        <div class="table-responsive">
            <table class="data-table" id="expensesTable">
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Description</th>
                        <th>Category</th>
                        <th class="text-right">Amount</th>
                        <th>Notes</th>
                        <th class="text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty expenses}">
                            <c:forEach var="exp" items="${expenses}">
                                <tr>
                                    <td class="cell-date">
                                        <span class="date-badge"><c:out value="${exp.date}" /></span>
                                    </td>
                                    <td class="cell-desc">
                                        <strong><c:out value="${exp.description}" /></strong>
                                    </td>
                                    <td>
                                        <span class="category-badge cat-${exp.category.toLowerCase()}">
                                            <c:out value="${exp.category}" />
                                        </span>
                                    </td>
                                    <td class="text-right cell-amount">
                                        ₹<fmt:formatNumber value="${exp.amount}" minFractionDigits="2" maxFractionDigits="2" />
                                    </td>
                                    <td class="cell-notes">
                                        <c:choose>
                                            <c:when test="${not empty exp.notes}">
                                                <span class="text-muted" title="<c:out value='${exp.notes}' />">
                                                    <c:out value="${exp.notes}" />
                                                </span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="text-subtle">—</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-center cell-actions">
                                        <div class="action-btn-group">
                                            <a href="${pageContext.request.contextPath}/edit-expense?id=${exp.id}" 
                                               class="btn-icon-action btn-edit" title="Edit Expense">
                                                ✏️
                                            </a>
                                            <form action="${pageContext.request.contextPath}/delete-expense" method="POST" 
                                                  class="inline-form" onsubmit="return confirmDelete('${exp.description}', '₹${exp.formattedAmount}')">
                                                <input type="hidden" name="id" value="${exp.id}">
                                                <button type="submit" class="btn-icon-action btn-delete" title="Delete Expense">
                                                    🗑️
                                                </button>
                                            </form>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="table-empty">
                                    <div class="empty-state">
                                        <span class="empty-icon">📂</span>
                                        <h3>No expenses found</h3>
                                        <p>Try adjusting your search filters or record a new expense.</p>
                                        <a href="${pageContext.request.contextPath}/add-expense" class="btn btn-primary btn-sm">Add First Expense</a>
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

<%@ include file="/includes/footer.jsp" %>
