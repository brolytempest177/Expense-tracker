package com.expensetracker.servlet;

import com.expensetracker.service.ExpenseService;
import com.expensetracker.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for deleting an expense.
 */
@WebServlet(name = "DeleteExpenseServlet", urlPatterns = {"/delete-expense"})
public class DeleteExpenseServlet extends HttpServlet {
    private ExpenseService expenseService;

    @Override
    public void init() {
        expenseService = new ExpenseService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = SessionUtil.getLoggedInUserId(request);
        String expenseId = request.getParameter("id");

        if (expenseId == null || expenseId.trim().isEmpty()) {
            SessionUtil.setFlashMessage(request, "error", "Invalid expense ID.");
            response.sendRedirect(request.getContextPath() + "/expenses");
            return;
        }

        boolean deleted = expenseService.deleteExpense(userId, expenseId.trim());
        if (deleted) {
            SessionUtil.setFlashMessage(request, "success", "Expense deleted successfully.");
        } else {
            SessionUtil.setFlashMessage(request, "error", "Failed to delete expense or access denied.");
        }

        response.sendRedirect(request.getContextPath() + "/expenses");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Redirect GET attempts on delete endpoint back to expenses list
        response.sendRedirect(request.getContextPath() + "/expenses");
    }
}
