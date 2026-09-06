package com.expensetracker.servlet;

import com.expensetracker.model.User;
import com.expensetracker.util.SessionUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Servlet handling guest login — creates a temporary session without database.
 */
@WebServlet(name = "GuestLoginServlet", urlPatterns = {"/guest-login"})
public class GuestLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Create a guest user object
        User guest = new User();
        guest.setUserId("guest_" + System.currentTimeMillis());
        guest.setName("Guest User");
        guest.setEmail("guest@demo.com");
        guest.setCreatedAt(new Date());

        // Set session
        SessionUtil.setLoggedInUser(request, guest);
        request.getSession().setAttribute("IS_GUEST", true);

        response.sendRedirect(request.getContextPath() + "/dashboard");
    }
}
