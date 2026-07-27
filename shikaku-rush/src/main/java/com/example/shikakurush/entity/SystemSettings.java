package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SystemSettings {
    private Integer id;
    private Boolean maintenanceMode;
    private LocalDateTime updatedAt;
}