package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Inquiry {
    private int id;
    private Integer userId;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private String username; // JOIN用
}