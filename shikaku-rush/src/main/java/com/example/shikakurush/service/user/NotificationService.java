package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Notification;
import com.example.shikakurush.repository.user.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findCurrentMonth() {
        return notificationRepository.findCurrentMonth();
    }

    // ── 管理画面用 ──────────────────────────────────────

    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

    public Notification findById(int id) {
        return notificationRepository.findById(id);
    }

    @Transactional
    public void create(String title, String body) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setBody(body);
        LocalDateTime now = LocalDateTime.now();
        notification.setPostedAt(now);
        notification.setUpdatedAt(now);
        notificationRepository.insert(notification);
    }

    @Transactional
    public void update(int id, String title, String body) {
        Notification notification = new Notification();
        notification.setId(id);
        notification.setTitle(title);
        notification.setBody(body);
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.update(notification);
    }

    @Transactional
    public void delete(int id) {
        notificationRepository.softDelete(id, LocalDateTime.now());
    }
}