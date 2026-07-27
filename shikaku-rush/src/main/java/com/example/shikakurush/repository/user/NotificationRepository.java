package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Notification;
import com.example.shikakurush.mapper.user.NotificationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepository {

    private final NotificationMapper notificationMapper;

    public NotificationRepository(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    public List<Notification> findCurrentMonth() {
        return notificationMapper.findCurrentMonth();
    }
}