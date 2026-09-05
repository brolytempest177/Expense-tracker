<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account — ExpenseTracker</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Manrope:wght@400;500;600;700;800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth-page">
    <div class="auth-container">
        <div class="auth-card">
            <div class="auth-header">
                <div class="brand-logo">
                    <span class="logo-icon">💸</span>
                    <h2>ExpenseTracker</h2>
                </div>
                <p class="auth-subtitle">Join us today to manage and budget your expenses effortlessly.</p>
            </div>

            <!-- Error Alert -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">
                    <span class="alert-icon">⚠️</span>
                    <span><c:out value="${errorMessage}" /></span>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="POST" class="auth-form" id="registerForm">
                <div class="form-group">
                    <label for="name">Full Name</label>
                    <div class="input-wrapper">
                        <span class="input-icon">👤</span>
                        <input type="text" id="name" name="name" value="<c:out value='${name}' />" 
                               placeholder="Alex Johnson" required autocomplete="name" autofocus>
                    </div>
                </div>

                <div class="form-group">
                    <label for="email">Email Address</label>
                    <div class="input-wrapper">
                        <span class="input-icon">✉️</span>
                        <input type="email" id="email" name="email" value="<c:out value='${email}' />" 
                               placeholder="alex@example.com" required autocomplete="email">
                    </div>
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <div class="input-wrapper">
                        <span class="input-icon">🔒</span>
                        <input type="password" id="password" name="password" 
                               placeholder="Minimum 6 characters" required minlength="6" autocomplete="new-password">
                    </div>
                </div>

                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <div class="input-wrapper">
                        <span class="input-icon">🔒</span>
                        <input type="password" id="confirmPassword" name="confirmPassword" 
                               placeholder="Re-enter password" required minlength="6" autocomplete="new-password">
                    </div>
                </div>

                <button type="submit" class="btn btn-primary btn-block">
                    <span>Create Account</span>
                    <span class="btn-arrow">→</span>
                </button>
            </form>

            <div class="auth-footer">
                <p>Already have an account? <a href="${pageContext.request.contextPath}/login" class="auth-link">Sign In</a></p>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
