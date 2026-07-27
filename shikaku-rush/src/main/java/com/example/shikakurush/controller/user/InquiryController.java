package com.example.shikakurush.controller.user;

import com.example.shikakurush.service.user.InquiryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // お問い合わせ画面
    @GetMapping("/user/inquiry")
    public String inquiry(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }
        return "user/inquiry";
    }

    // お問い合わせ送信
    @PostMapping("/user/inquiry")
    public String send(@RequestParam String title,
                       @RequestParam String body,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }

        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "タイトルを入力してください");
            return "redirect:/user/inquiry";
        }
        if (body == null || body.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "本文を入力してください");
            return "redirect:/user/inquiry";
        }

        Integer userId = (Integer) session.getAttribute("userId");
        inquiryService.save(userId, title, body);

        redirectAttributes.addFlashAttribute("successMessage", "お問い合わせを送信しました");
        return "redirect:/user/inquiry";
    }
}