package com.example.shikakurush.controller.admin;

import com.example.shikakurush.entity.Inquiry;
import com.example.shikakurush.service.admin.InquiryAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InquiryAdminController {

    private final InquiryAdminService inquiryAdminService;

    public InquiryAdminController(InquiryAdminService inquiryAdminService) {
        this.inquiryAdminService = inquiryAdminService;
    }

    // お問い合わせ一覧
    @GetMapping("/admin/inquiry")
    public String inquiryList(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        List<Inquiry> inquiries = inquiryAdminService.findAll();
        model.addAttribute("inquiries", inquiries);
        return "admin/confirm-inquiry";
    }

    // 詳細取得（ポップアップ用）
    @GetMapping("/admin/inquiry/detail")
    @org.springframework.web.bind.annotation.ResponseBody
    public Inquiry inquiryDetail(@RequestParam int id, HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return null;
        }
        return inquiryAdminService.findById(id);
    }
}