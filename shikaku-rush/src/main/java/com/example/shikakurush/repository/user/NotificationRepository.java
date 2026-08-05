package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Notification;
import com.example.shikakurush.mapper.user.NotificationMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    public List<Notification> findAll() {
        return notificationMapper.findAll();
    }

    public Notification findById(int id) {
        return notificationMapper.findById(id);
    }

    public void insert(Notification notification) {
        notificationMapper.insert(notification);
    }

    public void update(Notification notification) {
        notificationMapper.update(notification);
    }

    public void softDelete(int id, LocalDateTime deletedAt) {
        notificationMapper.softDelete(id, deletedAt);
    }
}