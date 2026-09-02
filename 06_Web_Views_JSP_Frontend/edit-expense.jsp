<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Edit Expense — ExpenseTracker" />
<%@ include file="/includes/header.jsp" %>
<%@ include file="/includes/sidebar.jsp" %>

<main class="app-main">
    <div class="page-header">
        <div>
            <h1 class="page-title">Edit Expense</h1>
            <p class="page-subtitle">Update your transaction details.</p>
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
            <form action="${pageContext.request.contextPath}/edit-expense" method="POST" id="editExpenseForm">
                <input type="hidden" name="id" value="<c:out value='${expense.id}' />">

                <div class="form-grid-2">
                    <div class="form-group">
                        <label for="amount">Amount (₹) <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">₹</span>
                            <input type="number" id="amount" name="amount" step="0.01" min="0.01" 
                                   value="<c:out value='${expense.amount}' />" required autofocus>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="date">Transaction Date <span class="required">*</span></label>
                        <div class="input-wrapper">
                            <span class="input-icon">📅</span>
                            <input type="date" id="date" name="date" 
                                   value="<c:out value='${expense.date}' />" required>
                        </div>
                    </div>
                </div>

                <div class="form-group">
                    <label for="description">Description <span class="required">*</span></label>
                    <div class="input-wrapper">
                        <span class="input-icon">📝</span>
                        <input type="text" id="description" name="description" 
                               value="<c:out value='${expense.description}' />" required>
                    </div>
                </div>

                <div class="form-group">
                    <label for="category">Category <span class="required">*</span></label>
                    <div class="input-wrapper">
                        <span class="input-icon">🏷️</span>
                        <select id="category" name="category" class="form-select" required>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat}" ${expense.category eq cat ? 'selected' : ''}>${cat}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="notes">Additional Notes <span class="optional">(Optional)</span></label>
                    <textarea id="notes" name="notes" rows="3" class="form-textarea"><c:out value="${expense.notes}" /></textarea>
                </div>

                <div class="form-actions">
                    <button type="submit" class="btn btn-primary">
                        <span>Update Expense</span>
                    </button>
                    <a href="${pageContext.request.contextPath}/expenses" class="btn btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </div>
</main>

<%@ include file="/includes/footer.jsp" %>
