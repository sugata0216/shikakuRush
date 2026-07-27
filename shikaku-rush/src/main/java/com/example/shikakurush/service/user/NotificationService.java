package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Notification;
import com.example.shikakurush.repository.user.NotificationRepository;
import org.springframework.stereotype.Service;

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
}