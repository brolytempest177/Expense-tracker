<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Add Expense — ExpenseTracker" />
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/sidebar.jsp" %>

<main class="app-main">
    <div class="page-header">
        <div>
            <h1 class="page-title">Add New Expense</h1>
            <p class="page-subtitle">Record a new transaction to keep your spending organized.</p>
        </div>
        <div class="header-actions">
            <a href="${pageContext.request.contextPath}/expenses" class="btn btn-ghost">
                <span>← Back to Expenses</span>
            </a>
        </div>
    </div>

    <!-- Error Alert -->
    <c:if test="${not empty errorMessage}">
        <div class="alert alert-danger">
            <span class="alert-icon">⚠️</span>
            <span><c:out value="${errorMessage}" /></span>
        </div>
    </c:if>

    <div class="form-container-card">
        <div class="card form-card">
            <form action="${pageContext.request.contextPath}/add-expense" method="POST" id="expenseForm">
                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="amount">Amount (₹) <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">₹</span>
                            <input type="number" id="amount" name="amount" step="0.01" min="0.01" 
                                   value="<c:out value='${amount}' />" placeholder="0.00" required autofocus>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="date">Transaction Date <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">📅</span>
                            <input type="date" id="date" name="date" 
                                   value="<c:out value='${not empty date ? date : todayDate}' />" required>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="description">Description <span class="required">*</span></label>
                    <div class="input-wrapper">
                        <span class="input-icon">📝</span>
                        <input type="text" id="description" name="description" 
                               value="<c:out value='${description}' />" 
                               placeholder="e.g. Grocery shopping, Metro card recharge" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="category">Category <span class="required">*</span></label>
                    <div class="input-wrapper">
                        <span class="input-icon">🏷️</span>
                        <select id="category" name="category" class="form-select" required>
                            <option value="" disabled ${empty selectedCategory ? 'selected' : ''}>Select Category</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat}" ${selectedCategory eq cat ? 'selected' : ''}>${cat}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="notes">Additional Notes <span class="optional">(Optional)</span></label>
                    <textarea id="notes" name="notes" rows="3" class="form-textarea" 
                              placeholder="Add any extra details, receipt notes, payment method..."><c:out value="${notes}" /></textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <span>Save Expense</span>
                    </button>
                    <a href="${pageContext.request.contextPath}/expenses" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</main>

<%@ include file="/includes/footer.jsp" %>
