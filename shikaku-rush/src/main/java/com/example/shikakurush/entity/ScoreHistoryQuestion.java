package com.example.shikakurush.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScoreHistoryQuestion {
    private Integer id;
    private Long scoreHistoryId;
    private Integer questionId;
    private LocalDateTime questionUpdatedAt;
}