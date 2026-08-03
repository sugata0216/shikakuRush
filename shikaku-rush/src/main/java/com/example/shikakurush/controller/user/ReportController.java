package com.example.shikakurush.controller.user;

import com.example.shikakurush.service.user.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 報告フォーム表示
    @GetMapping("/user/report")
    public String reportForm(@RequestParam Integer questionId,
                             @RequestParam String questionText,
                             @RequestParam(required = false) String choice1,
                             @RequestParam(required = false) String choice2,
                             @RequestParam(required = false) String choice3,
                             @RequestParam(required = false) String choice4,
                             @RequestParam(required = false) String correctAnswer,
                             Model model) {
        model.addAttribute("questionId", questionId);
        model.addAttribute("questionText", questionText);
        model.addAttribute("choice1", choice1);
        model.addAttribute("choice2", choice2);
        model.addAttribute("choice3", choice3);
        model.addAttribute("choice4", choice4);
        model.addAttribute("correctAnswer", correctAnswer);
        return "user/report";
    }

    // 報告送信処理
    @PostMapping("/user/report")
    public String report(@RequestParam int questionId,
                         @RequestParam String detail,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }
        Integer userId = (Integer) session.getAttribute("userId");
        reportService.report(userId, questionId, detail);
        redirectAttributes.addFlashAttribute("message", "報告を送信しました。");
        return "redirect:/user/report/complete";
    }

    // 報告完了画面
    @GetMapping("/user/report/complete")
    public String reportComplete() {
        return "user/report-complete";
    }
}