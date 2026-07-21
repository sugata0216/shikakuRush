package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Title;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TitleMapper {

    // 全称号一覧を取得
    @Select("""
            SELECT id, name, genre_id, difficulty_id, rank
            FROM titles
            ORDER BY genre_id, difficulty_id, rank
            """)
    List<Title> findAll();

    // ユーザーの所持称号IDリストを取得
    @Select("""
            SELECT title_id
            FROM user_titles
            WHERE user_id = #{userId}
            """)
    List<Integer> findOwnedTitleIdsByUserId(int userId);

    // 現在選択中の称号IDを取得
    @Select("SELECT selected_title_id FROM users WHERE id = #{userId}")
    Integer findSelectedTitleIdByUserId(int userId);

    // 選択中の称号を更新
    @Update("UPDATE users SET selected_title_id = #{titleId} WHERE id = #{userId}")
    void updateSelectedTitle(@Param("userId") int userId, @Param("titleId") int titleId);
}