package com.expensetracker.servlet;

import com.expensetracker.model.Category;
import com.expensetracker.model.Expense;
import com.expensetracker.service.BudgetService;
import com.expensetracker.service.ExpenseService;
import com.expensetracker.service.ReportService;
import com.expensetracker.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet for serving the main dashboard view with financial summaries, charts, and budget tracking.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    private ExpenseService expenseService;
    private BudgetService budgetService;
    private ReportService reportService;

    @Override
    public void init() {
        expenseService = new ExpenseService();
        budgetService = new BudgetService();
        reportService = new ReportService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userId = SessionUtil.getLoggedInUserId(request);
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();
        int currentYear = today.getYear();

        // Allow overriding month/year via query params if desired
        String mParam = request.getParameter("month");
        String yParam = request.getParameter("year");
        if (mParam != null && !mParam.trim().isEmpty()) {
            try { currentMonth = Integer.parseInt(mParam.trim()); } catch (NumberFormatException ignored) {}
        }
        if (yParam != null && !yParam.trim().isEmpty()) {
            try { currentYear = Integer.parseInt(yParam.trim()); } catch (NumberFormatException ignored) {}
        }

        // Monthly analytics (total spending, count, highest expense, category breakdown, daily trend)
        ReportService.MonthlyAnalytics analytics = reportService.getMonthlyAnalytics(userId, currentMonth, currentYear);

        // Budget status
        BudgetService.BudgetStatus budgetStatus = budgetService.computeBudgetStatus(userId, currentMonth, currentYear, analytics.getTotalSpent());

        // Recent 5 expenses
        List<Expense> allExpenses = expenseService.getAllExpenses(userId);
        List<Expense> recentExpenses = allExpenses.stream().limit(5).collect(Collectors.toList());

        request.setAttribute("analytics", analytics);
        request.setAttribute("budgetStatus", budgetStatus);
        request.setAttribute("recentExpenses", recentExpenses);
        request.setAttribute("currentMonth", currentMonth);
        request.setAttribute("currentYear", currentYear);
        request.setAttribute("activePage", "dashboard");

        request.getRequestDispatcher("/dashboard.jsp").forward(request, response);
    }
}
