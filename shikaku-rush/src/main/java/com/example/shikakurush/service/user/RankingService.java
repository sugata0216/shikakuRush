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

    public List<Ranking> findTop10(int genreId, int difficultyId) {
        return rankingRepository.findTop10(genreId, difficultyId);
    }
}