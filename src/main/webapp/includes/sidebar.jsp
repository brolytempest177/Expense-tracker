<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Mobile Header Bar -->
<div class="mobile-navbar">
    <div class="brand-logo">
        <span class="logo-icon">💸</span>
        <span class="brand-name">ExpenseTracker</span>
    </div>
    <button class="mobile-menu-btn" id="mobileMenuToggle" aria-label="Toggle Navigation Menu">
        <span>☰</span>
    </button>
</div>

<!-- Main App Sidebar -->
<aside class="app-sidebar" id="appSidebar">
    <div class="sidebar-brand">
        <div class="brand-logo">
            <span class="logo-icon">💸</span>
            <span class="brand-name">ExpenseTracker</span>
        </div>
        <span class="badge-tag badge-subtle">v1.0</span>
    </div>

    <nav class="sidebar-nav">
        <ul class="nav-list">
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/dashboard" 
                   class="nav-link ${activePage eq 'dashboard' ? 'active' : ''}">
                    <span class="nav-icon">📊</span>
                    <span class="nav-label">Dashboard</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/expenses" 
                   class="nav-link ${activePage eq 'expenses' ? 'active' : ''}">
                    <span class="nav-icon">💳</span>
                    <span class="nav-label">Expenses</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/add-expense" 
                   class="nav-link ${activePage eq 'add-expense' ? 'active' : ''}">
                    <span class="nav-icon">➕</span>
                    <span class="nav-label">Add Expense</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/budget" 
                   class="nav-link ${activePage eq 'budget' ? 'active' : ''}">
                    <span class="nav-icon">🎯</span>
                    <span class="nav-label">Monthly Budget</span>
                </a>
            </li>
            <li class="nav-item">
                <a href="${pageContext.request.contextPath}/reports" 
                   class="nav-link ${activePage eq 'reports' ? 'active' : ''}">
                    <span class="nav-icon">📈</span>
                    <span class="nav-label">Analytics & Reports</span>
                </a>
            </li>
        </ul>
    </nav>

    <div class="sidebar-footer">
        <div class="user-profile-card">
            <div class="user-avatar">
                ${sessionScope.LOGGED_IN_USER != null ? sessionScope.LOGGED_IN_USER.name.substring(0,1).toUpperCase() : 'U'}
            </div>
            <div class="user-details">
                <span class="user-name"><c:out value="${sessionScope.LOGGED_IN_USER.name}" /></span>
                <span class="user-email"><c:out value="${sessionScope.LOGGED_IN_USER.email}" /></span>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/logout" class="btn-logout" title="Sign Out">
            <span class="nav-icon">🚪</span>
            <span class="nav-label">Logout</span>
        </a>
    </div>
</aside>
