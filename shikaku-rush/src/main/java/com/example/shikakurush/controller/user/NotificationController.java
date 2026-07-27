package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.Notification;
import com.example.shikakurush.service.user.NotificationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // お知らせ一覧ページ
    @GetMapping("/user/notification")
    public String notification(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }

        List<Notification> notifications = notificationService.findCurrentMonth();
        model.addAttribute("notifications", notifications);

        return "user/notice";
    }
}