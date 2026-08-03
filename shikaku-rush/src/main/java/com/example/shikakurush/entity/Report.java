package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Report {
    private Integer id;
    private Integer userId;
    private Integer questionId;
    private String detail;
    private LocalDateTime createdAt;
}