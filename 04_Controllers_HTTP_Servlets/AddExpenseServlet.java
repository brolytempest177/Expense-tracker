package com.expensetracker.servlet;

import com.expensetracker.model.Category;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.util.SessionUtil;
import com.expensetracker.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Servlet for adding a new expense.
 */
@WebServlet(name = "AddExpenseServlet", urlPatterns = {"/add-expense"})
public class AddExpenseServlet extends HttpServlet {
    private ExpenseService expenseService;

    @Override
    public void init() {
        expenseService = new ExpenseService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categories", Category.ALL_CATEGORIES);
        request.setAttribute("todayDate", LocalDate.now().toString());
        request.setAttribute("activePage", "add-expense");
        request.getRequestDispatcher("/add-expense.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = SessionUtil.getLoggedInUserId(request);
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

            expenseService.addExpense(userId, amount, description, category, date, notes);
            SessionUtil.setFlashMessage(request, "success", "Expense added successfully!");
            response.sendRedirect(request.getContextPath() + "/expenses");
        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
            request.setAttribute("amount", amountStr);
            request.setAttribute("description", description);
            request.setAttribute("selectedCategory", category);
            request.setAttribute("date", date);
            request.setAttribute("notes", notes);
            request.setAttribute("categories", Category.ALL_CATEGORIES);
            request.setAttribute("todayDate", LocalDate.now().toString());
            request.setAttribute("activePage", "add-expense");
            request.getRequestDispatcher("/add-expense.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Failed to add expense: " + e.getMessage());
            request.setAttribute("categories", Category.ALL_CATEGORIES);
            request.setAttribute("todayDate", LocalDate.now().toString());
            request.setAttribute("activePage", "add-expense");
            request.getRequestDispatcher("/add-expense.jsp").forward(request, response);
        }
    }
}
