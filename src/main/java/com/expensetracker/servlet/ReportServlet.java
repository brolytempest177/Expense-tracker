package com.expensetracker.servlet;

import com.expensetracker.model.Category;
import com.expensetracker.service.ReportService;
import com.expensetracker.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Servlet for generating monthly financial reports and statistical charts.
 */
@WebServlet(name = "ReportServlet", urlPatterns = {"/reports"})
public class ReportServlet extends HttpServlet {
    private ReportService reportService;

    @Override
    public void init() {
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

        ReportService.MonthlyAnalytics analytics = reportService.getMonthlyAnalytics(userId, selectedMonth, selectedYear);

        // Compute percentage for each category
        Map<String, Double> categoryPercentages = new LinkedHashMap<>();
        double total = analytics.getTotalSpent();
        for (Map.Entry<String, Double> entry : analytics.getCategoryTotals().entrySet()) {
            double percent = total > 0 ? (entry.getValue() / total) * 100.0 : 0.0;
            categoryPercentages.put(entry.getKey(), percent);
        }

        request.setAttribute("analytics", analytics);
        request.setAttribute("categoryPercentages", categoryPercentages);
        request.setAttribute("selectedMonth", selectedMonth);
        request.setAttribute("selectedYear", selectedYear);
        request.setAttribute("categories", Category.ALL_CATEGORIES);
        request.setAttribute("activePage", "reports");

        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }
}
