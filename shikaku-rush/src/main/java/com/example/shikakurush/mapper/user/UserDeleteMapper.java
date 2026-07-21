package com.example.shikakurush.mapper.user;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserDeleteMapper {

    // 既存のメソッド（そのまま）
    @Delete("DELETE FROM score_history_questions WHERE score_history_id IN " +
            "(SELECT id FROM score_histories WHERE user_id = #{userId})")
    void deleteScoreHistoryQuestions(@Param("userId") Integer userId);

    @Delete("DELETE FROM score_histories WHERE user_id = #{userId}")
    void deleteScoreHistories(@Param("userId") Integer userId);

    @Delete("DELETE FROM rankings WHERE user_id = #{userId}")
    void deleteRankings(@Param("userId") Integer userId);

    @Delete("DELETE FROM user_titles WHERE user_id = #{userId}")
    void deleteUserTitles(@Param("userId") Integer userId);

    // 追加するメソッド
    @Update("UPDATE inquiries SET user_id = NULL WHERE user_id = #{userId}")
    void nullifyInquiryUserId(@Param("userId") Integer userId);

    @Update("UPDATE reports SET user_id = NULL WHERE user_id = #{userId}")
    void nullifyReportUserId(@Param("userId") Integer userId);

    @Delete("DELETE FROM password_reset_tokens WHERE user_id = #{userId}")
    void deletePasswordResetTokens(@Param("userId") Integer userId);

    @Update("UPDATE users SET deleted_flag = true, selected_title_id = NULL " +
            "WHERE id = #{userId}")
    void softDeleteUser(@Param("userId") Integer userId);
}