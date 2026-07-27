package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.ScoreHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ScoreHistoryMapper {

    // カテゴリ別上位5件を取得
    @Select("""
            SELECT id, user_id, category_id, difficulty_id, score, played_at
            FROM score_histories
            WHERE user_id = #{userId}
            AND category_id = #{categoryId}
            AND difficulty_id = #{difficultyId}
            ORDER BY score DESC
            LIMIT 5
            """)
    List<ScoreHistory> findTop5(
            @Param("userId") int userId,
            @Param("categoryId") int categoryId,
            @Param("difficultyId") int difficultyId);
}