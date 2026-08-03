package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Ranking;
import com.example.shikakurush.mapper.user.RankingMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingRepository {

    private final RankingMapper rankingMapper;

    public RankingRepository(RankingMapper rankingMapper) {
        this.rankingMapper = rankingMapper;
    }

    public List<Ranking> findTop5(int genreId, int difficultyId) {
        return rankingMapper.findTop5(genreId, difficultyId);
    }

    public Ranking findByUserAndGenreAndDifficulty(int userId, int genreId, int difficultyId) {
        return rankingMapper.findByUserAndGenreAndDifficulty(userId, genreId, difficultyId);
    }

    public void insert(Ranking ranking) {
        rankingMapper.insert(ranking);
    }

    public void upsertRanking(int userId, int genreId, int difficultyId, int score) {
        rankingMapper.upsertRanking(userId, genreId, difficultyId, score);
    }
}