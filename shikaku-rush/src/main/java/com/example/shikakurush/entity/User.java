package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String email;
    private String password;
    private String username;
    private Integer selectedTitleId;
    private boolean usernameChangedThisMonth;
    private boolean banned;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean deleteFlag;
}