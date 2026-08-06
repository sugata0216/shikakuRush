package com.example.shikakurush.controller.admin;

import com.example.shikakurush.entity.Report;
import com.example.shikakurush.service.user.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminReportController {

    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ── 問題報告一覧ページ ────────────────────────────────
    @GetMapping("/admin/report")
    public String report(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        List<Report> reports = reportService.findAll();
        model.addAttribute("reports", reports);

        return "admin/question-reporting"; // ✅ 修正
    }
}