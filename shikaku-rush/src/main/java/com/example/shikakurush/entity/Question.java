package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Question {
    private int id;
    private int categoryId;
    private String questionText;
    private String choice1;
    private String choice2;
    private String choice3;
    private String choice4;
    private String correctAnswer;
    private String explanation;
    private String sourceName;
    private boolean deleteFlag;
    private LocalDateTime updatedAt; // ← 追加
    private String categoryName;
}