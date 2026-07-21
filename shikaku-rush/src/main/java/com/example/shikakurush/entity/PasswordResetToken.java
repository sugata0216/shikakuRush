package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PasswordResetToken {
    private Integer id;
    private Integer userId;
    private String email;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime usedAt;
    private LocalDateTime createdAt;

    // トークンが期限切れかどうか
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    // トークンが使用済みかどうか
    public boolean isUsed() {
        return usedAt != null;
    }
}