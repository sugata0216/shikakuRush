package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Question;
import com.example.shikakurush.entity.ScoreHistory;
import com.example.shikakurush.entity.ScoreHistoryQuestion;
import com.example.shikakurush.repository.user.RankingRepository;
import com.example.shikakurush.repository.user.ScoreHistoryRepository;
import com.example.shikakurush.repository.user.ScoreHistoryQuestionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResultService {

    private final ScoreHistoryRepository scoreHistoryRepository;
    private final ScoreHistoryQuestionRepository scoreHistoryQuestionRepository;
    private final RankingRepository rankingRepository;

    public ResultService(ScoreHistoryRepository scoreHistoryRepository,
                         ScoreHistoryQuestionRepository scoreHistoryQuestionRepository,
                         RankingRepository rankingRepository) {
        this.scoreHistoryRepository = scoreHistoryRepository;
        this.scoreHistoryQuestionRepository = scoreHistoryQuestionRepository;
        this.rankingRepository = rankingRepository;
    }

    private static final List<Integer> ALL_CATEGORY_IDS = List.of(1, 8, 15);

    @Transactional
    public void saveScore(Integer userId,
                          Integer categoryId,
                          Integer difficultyId,
                          Integer score,
                          List<Question> questions) { // questionsを追加
        // スコア履歴を保存
        if (categoryId != null) {
            ScoreHistory scoreHistory = new ScoreHistory();
            scoreHistory.setUserId(userId);
            scoreHistory.setCategoryId(categoryId);
            scoreHistory.setDifficultyId(difficultyId);
            scoreHistory.setScore(score);
            scoreHistoryRepository.save(scoreHistory);

            // 中間テーブルにINSERT（10問分）
            if (questions != null) {
                List<Question> playedQuestions = questions.subList(0, Math.min(10, questions.size()));
                for (Question question : playedQuestions) {
                    ScoreHistoryQuestion shq = new ScoreHistoryQuestion();
                    shq.setScoreHistoryId(scoreHistory.getId());
                    shq.setQuestionId(question.getId());
                    shq.setQuestionUpdatedAt(question.getUpdatedAt());
                    scoreHistoryQuestionRepository.save(shq);
                }
            }
        }

        // ALLカテゴリの場合のみランキングに登録・更新
        if (categoryId == null || ALL_CATEGORY_IDS.contains(categoryId)) {
            Integer genreId = resolveGenreId(categoryId);
            if (genreId != null) {
                saveOrUpdateRanking(userId, genreId, difficultyId, score);
            }
        }
    }

    private Integer resolveGenreId(Integer categoryId) {
        if (categoryId == null) return null;
        if (categoryId == 1)  return 1;
        if (categoryId == 8)  return 2;
        if (categoryId == 15) return 3;
        return null;
    }

    private void saveOrUpdateRanking(int userId, int genreId, int difficultyId, int score) {
        rankingRepository.upsertRanking(userId, genreId, difficultyId, score);
    }
}