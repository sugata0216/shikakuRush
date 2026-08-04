package com.example.shikakurush.controller.admin;

import com.example.shikakurush.entity.User;
import com.example.shikakurush.service.admin.UserAdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserAdminController {

    private final UserAdminService userAdminService;

    public UserAdminController(UserAdminService userAdminService) {
        this.userAdminService = userAdminService;
    }

    // ユーザー一覧
    @GetMapping("/admin/user")
    public String userList(@RequestParam(required = false) String keyword,
                           HttpSession session,
                           Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        List<User> users = userAdminService.search(keyword);
        model.addAttribute("users", users);
        model.addAttribute("keyword", keyword);
        return "admin/user-list";
    }

    // BAN処理
    @PostMapping("/admin/user/ban")
    public String banUser(@RequestParam Integer id,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        userAdminService.banUser(id);
        redirectAttributes.addFlashAttribute("message", "ユーザーをBANしました");
        return "redirect:/admin/user";
    }

    // BAN解除処理
    @PostMapping("/admin/user/unban")
    public String unbanUser(@RequestParam Integer id,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        userAdminService.unbanUser(id);
        redirectAttributes.addFlashAttribute("message", "BANを解除しました");
        return "redirect:/admin/user";
    }

    // ユーザー管理メニュー
    @GetMapping("/admin/user/menu")
    public String userMenu(HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        return "admin/user-management-menu";
    }
}