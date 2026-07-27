package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScoreHistory {
    private long id;
    private int userId;
    private int categoryId;
    private int difficultyId;
    private int score;
    private LocalDateTime playedAt;
}