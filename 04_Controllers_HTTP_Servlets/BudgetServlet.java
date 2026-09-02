package com.expensetracker.servlet;

import com.expensetracker.model.Budget;
import com.expensetracker.service.BudgetService;
import com.expensetracker.service.ReportService;
import com.expensetracker.util.SessionUtil;
import com.expensetracker.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Servlet for managing monthly budgets and monitoring spending progress.
 */
@WebServlet(name = "BudgetServlet", urlPatterns = {"/budget"})
public class BudgetServlet extends HttpServlet {
    private BudgetService budgetService;
    private ReportService reportService;

    @Override
    public void init() {
        budgetService = new BudgetService();
        reportService = new ReportService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = SessionUtil.getLoggedInUserId(request);
        LocalDate today = LocalDate.now();
        int selectedMonth = today.getMonthValue();
        int selectedYear = today.getYear();

        String mParam = request.getParameter("month");
        String yParam = request.getParameter("year");
        if (mParam != null && !mParam.trim().isEmpty()) {
            try { selectedMonth = Integer.parseInt(mParam.trim()); } catch (NumberFormatException ignored) {}
        }
        if (yParam != null && !yParam.trim().isEmpty()) {
            try { selectedYear = Integer.parseInt(yParam.trim()); } catch (NumberFormatException ignored) {}
        }

        Budget currentBudget = budgetService.getBudget(userId, selectedMonth, selectedYear);
        ReportService.MonthlyAnalytics analytics = reportService.getMonthlyAnalytics(userId, selectedMonth, selectedYear);
        BudgetService.BudgetStatus budgetStatus = budgetService.computeBudgetStatus(userId, selectedMonth, selectedYear, analytics.getTotalSpent());
        List<Budget> allBudgets = budgetService.getAllBudgets(userId);

        request.setAttribute("selectedMonth", selectedMonth);
        request.setAttribute("selectedYear", selectedYear);
        request.setAttribute("currentBudget", currentBudget);
        request.setAttribute("budgetStatus", budgetStatus);
        request.setAttribute("totalSpent", analytics.getTotalSpent());
        request.setAttribute("allBudgets", allBudgets);
        request.setAttribute("activePage", "budget");

        request.getRequestDispatcher("/budget.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = SessionUtil.getLoggedInUserId(request);
        String monthStr = request.getParameter("month");
        String yearStr = request.getParameter("year");
        String amountStr = request.getParameter("amount");

        try {
            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(yearStr);
            double amount = ValidationUtil.parseAmount(amountStr);

            if (amount < 0) {
                throw new IllegalArgumentException("Budget amount cannot be negative.");
            }

            budgetService.setBudget(userId, month, year, amount);
            SessionUtil.setFlashMessage(request, "success", "Monthly budget updated successfully!");
            response.sendRedirect(request.getContextPath() + "/budget?month=" + month + "&year=" + year);
        } catch (IllegalArgumentException e) {
            SessionUtil.setFlashMessage(request, "error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/budget?month=" + monthStr + "&year=" + yearStr);
        } catch (Exception e) {
            SessionUtil.setFlashMessage(request, "error", "Failed to update budget: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/budget");
        }
    }
}
