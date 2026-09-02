package com.expensetracker.servlet;

import com.expensetracker.util.SessionUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Authentication Filter to protect sensitive routes from unauthenticated access.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    // List of public URI patterns that do not require authentication
    private static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/login",
            "/login.jsp",
            "/register",
            "/register.jsp",
            "/logout",
            "/css/",
            "/js/",
            "/images/",
            "/favicon.ico"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        // Check if requested URI is public
        boolean isPublic = isPublicResource(path);

        if (isPublic) {
            chain.doFilter(req, res);
            return;
        }

        // Check authentication
        if (SessionUtil.isLoggedIn(request)) {
            chain.doFilter(req, res);
        } else {
            // Store target URL or redirect to login
            SessionUtil.setFlashMessage(request, "error", "Please log in to access this page.");
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }

    private boolean isPublicResource(String path) {
        if (path.isEmpty() || "/".equals(path)) {
            return true;
        }
        for (String publicPrefix : PUBLIC_PATHS) {
            if (path.startsWith(publicPrefix)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() {
        // Cleanup if needed
    }
}
