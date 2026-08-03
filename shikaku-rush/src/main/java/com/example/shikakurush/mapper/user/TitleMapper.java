package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Title;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TitleMapper {

    @Select("""
            SELECT id, name, genre_id, difficulty_id, rank
            FROM titles
            ORDER BY genre_id, difficulty_id, rank
            """)
    List<Title> findAll();

    @Select("""
            SELECT title_id
            FROM user_titles
            WHERE user_id = #{userId}
            """)
    List<Integer> findOwnedTitleIdsByUserId(int userId);

    @Select("SELECT selected_title_id FROM users WHERE id = #{userId}")
    Integer findSelectedTitleIdByUserId(int userId);

    @Update("UPDATE users SET selected_title_id = #{titleId} WHERE id = #{userId}")
    void updateSelectedTitle(@Param("userId") int userId, @Param("titleId") int titleId);

    // ── 称号新規作成（付与バッチ用） ──────────────────────
    @Insert("""
            INSERT INTO titles (name, genre_id, difficulty_id, rank, created_at)
            VALUES (#{name}, #{genreId}, #{difficultyId}, #{rank}, CURRENT_TIMESTAMP)
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(Title title);

    // ── ユーザーへの称号付与 ──────────────────────────────
    @Insert("""
            INSERT INTO user_titles (user_id, title_id, awarded_at)
            VALUES (#{userId}, #{titleId}, CURRENT_TIMESTAMP)
            """)
    void insertUserTitle(@Param("userId") int userId, @Param("titleId") int titleId);
}