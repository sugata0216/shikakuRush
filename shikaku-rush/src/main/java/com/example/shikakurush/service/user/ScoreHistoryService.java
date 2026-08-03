package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Category;
import com.example.shikakurush.entity.ScoreHistory;
import com.example.shikakurush.repository.user.CategoryRepository;
import com.example.shikakurush.repository.user.ScoreHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreHistoryService {

    private final ScoreHistoryRepository scoreHistoryRepository;
    private final CategoryRepository categoryRepository;

    public ScoreHistoryService(ScoreHistoryRepository scoreHistoryRepository,
                               CategoryRepository categoryRepository) {
        this.scoreHistoryRepository = scoreHistoryRepository;
        this.categoryRepository = categoryRepository;
    }

    // ジャンル・難易度別のカテゴリごと上位5件を取得
    public Map<Category, List<ScoreHistory>> findScoresByGenreAndDifficulty(
            int userId, int genreId, int difficultyId) {

        List<Category> categories = categoryRepository.findByGenreId(genreId);
        Map<Category, List<ScoreHistory>> scoreMap = new LinkedHashMap<>();

        categories.forEach(category -> {
            List<ScoreHistory> scores = scoreHistoryRepository.findTop5(
                    userId, category.getId(), difficultyId);
            scoreMap.put(category, scores);
        });

        return scoreMap;
    }

    public void save(int userId, int categoryId, int difficultyId, int score) {
        ScoreHistory scoreHistory = new ScoreHistory();
        scoreHistory.setUserId(userId);
        scoreHistory.setCategoryId(categoryId);
        scoreHistory.setDifficultyId(difficultyId);
        scoreHistory.setScore(score);
        scoreHistoryRepository.save(scoreHistory);
    }
}