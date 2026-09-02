package com.expensetracker.servlet;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for displaying and filtering user expenses in a tabular view.
 */
@WebServlet(name = "ExpenseListServlet", urlPatterns = {"/expenses"})
public class ExpenseListServlet extends HttpServlet {
    private ExpenseService expenseService;

    @Override
    public void init() {
        expenseService = new ExpenseService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = SessionUtil.getLoggedInUserId(request);

        String query = request.getParameter("query");
        String category = request.getParameter("category");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String sortBy = request.getParameter("sortBy");

        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "date_desc";
        }

        List<Expense> expenses = expenseService.getFilteredExpenses(userId, query, category, startDate, endDate, sortBy);
        double totalFilteredAmount = expenses.stream().mapToDouble(Expense::getAmount).sum();

        request.setAttribute("expenses", expenses);
        request.setAttribute("totalCount", expenses.size());
        request.setAttribute("totalFilteredAmount", totalFilteredAmount);
        request.setAttribute("categories", Category.ALL_CATEGORIES);

        // Retain filter state in UI
        request.setAttribute("paramQuery", query);
        request.setAttribute("paramCategory", category);
        request.setAttribute("paramStartDate", startDate);
        request.setAttribute("paramEndDate", endDate);
        request.setAttribute("paramSortBy", sortBy);
        request.setAttribute("activePage", "expenses");

        request.getRequestDispatcher("/expenses.jsp").forward(request, response);
    }
}
