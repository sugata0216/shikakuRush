package com.example.shikakurush.controller.admin;

import com.example.shikakurush.entity.Notification;
import com.example.shikakurush.service.user.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/notification")
public class NotificationAdminController {

    private final NotificationService notificationService;

    public NotificationAdminController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // お知らせ一覧
    @GetMapping
    public String list(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        List<Notification> notifications = notificationService.findAll();
        model.addAttribute("notifications", notifications);

        return "admin/notice-list";
    }

    // お知らせ管理メニュー（作成／一覧への入り口）
    @GetMapping("/menu")
    public String menu(HttpSession session) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        return "admin/notification-management";
    }

    // 新規作成フォーム表示
    @GetMapping("/new")
    public String newForm(HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("notification", new Notification());

        return "admin/create-notice";
    }

    // 編集フォーム表示（作成フォームを流用）
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, HttpSession session, Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        Notification notification = notificationService.findById(id);
        model.addAttribute("notification", notification);

        return "admin/create-notice";
    }

    // 新規作成の登録
    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam String content,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        notificationService.create(title, content);
        redirectAttributes.addFlashAttribute("message", "お知らせを投稿しました");

        return "redirect:/admin/notification";
    }

    // 編集の更新
    @PostMapping("/{id}")
    public String update(@PathVariable int id,
                         @RequestParam String title,
                         @RequestParam String content,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        notificationService.update(id, title, content);
        redirectAttributes.addFlashAttribute("message", "お知らせを更新しました");

        return "redirect:/admin/notification";
    }

    // 削除（論理削除）
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable int id,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        notificationService.delete(id);
        redirectAttributes.addFlashAttribute("message", "お知らせを削除しました");

        return "redirect:/admin/notification";
    }
}