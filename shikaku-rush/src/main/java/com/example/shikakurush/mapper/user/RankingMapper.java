package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Ranking;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RankingMapper {

    // ジャンル・難易度別ランキング上位10件を取得
    @Select("""
            SELECT r.id, r.user_id, r.genre_id, r.difficulty_id, r.score, r.updated_at,
                   u.username
            FROM rankings r
            INNER JOIN users u ON u.id = r.user_id
            WHERE r.genre_id = #{genreId}
            AND r.difficulty_id = #{difficultyId}
            AND u.deleted_flag = FALSE
            AND u.banned = FALSE
            ORDER BY r.score DESC
            LIMIT 10
            """)
    List<Ranking> findTop10(
            @Param("genreId") int genreId,
            @Param("difficultyId") int difficultyId);
}