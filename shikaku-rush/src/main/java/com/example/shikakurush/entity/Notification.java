package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Notification {
    private int id;
    private String title;
    private String body;
    private LocalDateTime postedAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}