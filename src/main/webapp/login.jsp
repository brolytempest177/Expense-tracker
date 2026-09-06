<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — ExpenseTracker</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Space+Grotesk:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-container">
        <div class="auth-card">
            <div class="auth-header">
                <div class="brand-logo">
                    <span class="logo-icon">⚡</span>
                    <span class="brand-name">ExpenseTracker</span>
                </div>
                <h2>Welcome back</h2>
                <p class="auth-subtitle">Sign in to your account</p>
            </div>

            <!-- Error and Flash Message Alerts -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    <span><c:out value="${errorMessage}" /></span>
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.FLASH_ERROR}">
                <div class="alert alert-danger">
                    <span><c:out value="${sessionScope.FLASH_ERROR}" /></span>
                </div>
                <c:remove var="FLASH_ERROR" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.FLASH_SUCCESS}">
                <div class="alert alert-success">
                    <span><c:out value="${sessionScope.FLASH_SUCCESS}" /></span>
                </div>
                <c:remove var="FLASH_SUCCESS" scope="session" />
            </c:if>

            <c:if test="${param.logout eq 'true'}">
                <div class="alert alert-info">
                    <span>You have been logged out successfully.</span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="POST" class="auth-form" id="loginForm">
                <div class="form-group">
                    <label for="email">Email</label>
                    <div class="input-wrapper">
                        <span class="input-icon">✉️</span>
                        <input type="email" id="email" name="email" value="<c:out value='${enteredEmail}' />" 
                               placeholder="you@example.com" required autocomplete="email" autofocus>
                    </div>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <div class="input-wrapper">
                        <span class="input-icon">🔑</span>
                        <input type="password" id="password" name="password" 
                               placeholder="••••••••" required autocomplete="current-password">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary btn-block" style="margin-top: 0.75rem">
                    <span>Sign in</span>
                </button>
            </form>

            <div style="text-align:center; margin-top:1.25rem; padding-top:1.25rem; border-top:1px solid var(--border-color);">
                <a href="${pageContext.request.contextPath}/guest-login" style="display:block; width:100%; padding:0.65rem 1rem; font-size:0.88rem; font-weight:600; font-family:inherit; border-radius:4px; border:1px solid #eab308; background:transparent; color:#eab308; cursor:pointer; text-align:center; text-decoration:none;">
                    👤 Continue as Guest
                </a>
            </div>

            <div class="auth-footer">
                <p>New here? <a href="${pageContext.request.contextPath}/register" class="auth-link">Create an account</a></p>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
