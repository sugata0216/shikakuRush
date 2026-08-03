package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.ScoreHistory;
import com.example.shikakurush.mapper.user.ScoreHistoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ScoreHistoryRepository {

    private final ScoreHistoryMapper scoreHistoryMapper;

    public ScoreHistoryRepository(ScoreHistoryMapper scoreHistoryMapper) {
        this.scoreHistoryMapper = scoreHistoryMapper;
    }

    public List<ScoreHistory> findTop5(int userId, int categoryId, int difficultyId) {
        return scoreHistoryMapper.findTop5(userId, categoryId, difficultyId);
    }

    public void save(ScoreHistory scoreHistory) {
        scoreHistoryMapper.insert(scoreHistory);
    }
}