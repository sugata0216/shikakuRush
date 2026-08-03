package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.ScoreHistoryQuestion;
import com.example.shikakurush.mapper.user.ScoreHistoryQuestionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ScoreHistoryQuestionRepository {

    private final ScoreHistoryQuestionMapper scoreHistoryQuestionMapper;

    public void save(ScoreHistoryQuestion scoreHistoryQuestion) {
        scoreHistoryQuestionMapper.insert(scoreHistoryQuestion);
    }
}