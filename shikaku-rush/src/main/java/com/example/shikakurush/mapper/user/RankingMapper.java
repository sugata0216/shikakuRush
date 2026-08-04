package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Ranking;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface RankingMapper {

    // ジャンル・難易度別ランキング上位5件を取得
    @Select("SELECT r.user_id, u.username, r.score, u.banned " +
            "FROM rankings r " +
            "JOIN users u ON r.user_id = u.id " +
            "WHERE r.genre_id = #{genreId} " +
            "AND r.difficulty_id = #{difficultyId} " +
            "ORDER BY r.score DESC " +
            "LIMIT 5")
    List<Ranking> findTop5(@Param("genreId") int genreId,
                           @Param("difficultyId") int difficultyId);

    // ── 既存ランキングを取得 ──────────────────────────────
    @Select("""
        SELECT id, user_id, genre_id, difficulty_id, score
        FROM rankings
        WHERE user_id = #{userId}
        AND genre_id = #{genreId}
        AND difficulty_id = #{difficultyId}
        """)
    Ranking findByUserAndGenreAndDifficulty(
            @Param("userId") int userId,
            @Param("genreId") int genreId,
            @Param("difficultyId") int difficultyId);

    // ── ランキング新規登録 ────────────────────────────────
    @Insert("""
        INSERT INTO rankings (user_id, genre_id, difficulty_id, score, updated_at)
        VALUES (#{userId}, #{genreId}, #{difficultyId}, #{score}, CURRENT_TIMESTAMP)
        """)
    void insert(Ranking ranking);

    // ── ランキングスコア更新 ──────────────────────────────
    @Update("SELECT upsert_ranking(#{userId}, #{genreId}, #{difficultyId}, #{score})")
    void upsertRanking(
            @Param("userId") int userId,
            @Param("genreId") int genreId,
            @Param("difficultyId") int difficultyId,
            @Param("score") int score);
}