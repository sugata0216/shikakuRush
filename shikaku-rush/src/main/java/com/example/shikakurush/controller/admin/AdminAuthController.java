package com.example.shikakurush.controller.admin;

import com.example.shikakurush.entity.Admin;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.service.admin.AdminAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    // 管理者ログイン画面の表示
    @GetMapping("/admin/login")
    public String loginForm(HttpSession session) {
        // すでにログイン済みの場合はTOP画面へ
        if (session.getAttribute("adminId") != null) {
            return "redirect:/admin/top";
        }
        return "admin/administrator-login";
    }

    // 管理者ログイン処理
    @PostMapping("/admin/login")
    public String login(@RequestParam String password,
                        HttpSession session,
                        Model model) {
        try {
            Admin admin = adminAuthService.login(password);
            session.setAttribute("adminId", admin.getId());
            return "redirect:/admin/top";
        } catch (AuthException e) {
            model.addAttribute("error", "パスワードが正しくありません");
            return "admin/administrator-login";
        }
    }

    // 管理者ログアウト
    @PostMapping("/admin/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("adminId");
        return "redirect:/admin/login";
    }

}