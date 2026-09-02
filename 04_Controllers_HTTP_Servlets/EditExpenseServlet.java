package com.expensetracker.servlet;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.util.SessionUtil;
import com.expensetracker.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for viewing and editing an existing expense.
 */
@WebServlet(name = "EditExpenseServlet", urlPatterns = {"/edit-expense"})
public class EditExpenseServlet extends HttpServlet {
    private ExpenseService expenseService;

    @Override
    public void init() {
        expenseService = new ExpenseService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = SessionUtil.getLoggedInUserId(request);
        String expenseId = request.getParameter("id");

        if (expenseId == null || expenseId.trim().isEmpty()) {
            SessionUtil.setFlashMessage(request, "error", "Invalid expense ID.");
            response.sendRedirect(request.getContextPath() + "/expenses");
            return;
        }

        Expense expense = expenseService.getExpense(userId, expenseId.trim());
        if (expense == null) {
            SessionUtil.setFlashMessage(request, "error", "Expense not found or access denied.");
            response.sendRedirect(request.getContextPath() + "/expenses");
            return;
        }

        request.setAttribute("expense", expense);
        request.setAttribute("categories", Category.ALL_CATEGORIES);
        request.setAttribute("activePage", "expenses");
        request.getRequestDispatcher("/edit-expense.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = SessionUtil.getLoggedInUserId(request);
        String expenseId = request.getParameter("id");
        String amountStr = request.getParameter("amount");
        String description = request.getParameter("description");
        String category = request.getParameter("category");
        String date = request.getParameter("date");
        String notes = request.getParameter("notes");

        try {
            double amount = ValidationUtil.parseAmount(amountStr);
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be a positive number.");
            }

            expenseService.updateExpense(userId, expenseId, amount, description, category, date, notes);
            SessionUtil.setFlashMessage(request, "success", "Expense updated successfully!");
            response.sendRedirect(request.getContextPath() + "/expenses");
        } catch (IllegalArgumentException e) {
            Expense expense = new Expense(expenseId, userId, ValidationUtil.parseAmount(amountStr), description, category, date, notes, null);
            request.setAttribute("expense", expense);
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("categories", Category.ALL_CATEGORIES);
            request.setAttribute("activePage", "expenses");
            request.getRequestDispatcher("/edit-expense.jsp").forward(request, response);
        } catch (Exception e) {
            SessionUtil.setFlashMessage(request, "error", "Failed to update expense: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/expenses");
        }
    }
}
