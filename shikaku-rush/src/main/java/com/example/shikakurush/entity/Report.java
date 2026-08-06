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

    // ✅ JOINで取得する問題情報
    private String questionText;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
}