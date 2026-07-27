package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Ranking {
    private int id;
    private int userId;
    private int genreId;
    private int difficultyId;
    private int score;
    private LocalDateTime updatedAt;
    private String username;
}