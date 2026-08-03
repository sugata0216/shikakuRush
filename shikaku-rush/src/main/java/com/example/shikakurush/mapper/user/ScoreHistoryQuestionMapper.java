package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.ScoreHistoryQuestion;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ScoreHistoryQuestionMapper {

    @Insert("INSERT INTO score_history_questions (score_history_id, question_id, question_updated_at) " +
            "VALUES (#{scoreHistoryId}, #{questionId}, #{questionUpdatedAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(ScoreHistoryQuestion scoreHistoryQuestion);
}