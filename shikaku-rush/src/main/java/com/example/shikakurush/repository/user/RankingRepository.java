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

    public List<Ranking> findTop10(int genreId, int difficultyId) {
        return rankingMapper.findTop10(genreId, difficultyId);
    }
}