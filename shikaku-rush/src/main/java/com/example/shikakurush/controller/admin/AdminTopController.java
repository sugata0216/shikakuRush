package com.example.shikakurush.controller.admin;

import com.example.shikakurush.service.admin.SystemSettingsService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminTopController {

    private final SystemSettingsService systemSettingsService;

    // 管理者TOP画面の表示
    @GetMapping("/admin/top")
    public String adminTop(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        model.addAttribute("maintenanceMode", systemSettingsService.isMaintenanceMode());
        return "admin/administrator-top";
    }

    // メンテナンスモード切り替え
    @PostMapping("/admin/maintenance/toggle")
    public String toggleMaintenance(HttpSession session,
                                    RedirectAttributes redirectAttributes) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }
        boolean newMode = systemSettingsService.toggleMaintenanceMode();
        redirectAttributes.addFlashAttribute("maintenanceMessage",
                newMode ? "メンテナンスモードをONにしました" : "メンテナンスモードをOFFにしました");
        return "redirect:/admin/top";
    }
}