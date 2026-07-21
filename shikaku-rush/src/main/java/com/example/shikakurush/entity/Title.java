package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Title {
    private Integer id;
    private String name;
    private Integer genreId;
    private Integer difficultyId;
    private Integer rank;
    private LocalDateTime createdAt;
}