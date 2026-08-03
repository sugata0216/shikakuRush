package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Ranking;
import com.example.shikakurush.repository.user.RankingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    private final RankingRepository rankingRepository;

    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    public List<Ranking> findTop5(int genreId, int difficultyId) {
        return rankingRepository.findTop5(genreId, difficultyId);
    }

    // ── ランキング登録・更新 ──────────────────────────────
    public void saveOrUpdate(int userId, int genreId, int difficultyId, int score) {
        Ranking existing = rankingRepository.findByUserAndGenreAndDifficulty(
                userId, genreId, difficultyId);

        if (existing == null) {
            Ranking ranking = new Ranking();
            ranking.setUserId(userId);
            ranking.setGenreId(genreId);
            ranking.setDifficultyId(difficultyId);
            ranking.setScore(score);
            rankingRepository.insert(ranking);
        } else if (score > existing.getScore()) {
            rankingRepository.upsertRanking(userId, genreId, difficultyId, score);
        }
    }
}