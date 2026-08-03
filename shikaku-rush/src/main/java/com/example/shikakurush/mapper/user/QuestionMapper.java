package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {

    // カテゴリ指定で11問ランダム抽出
    @Select("""
            SELECT id, category_id, question_text,
                   choice_1, choice_2, choice_3, choice_4,
                   correct_answer, explanation, source_name, updated_at
            FROM questions
            WHERE category_id = #{categoryId}
            AND delete_flag = FALSE
            ORDER BY RANDOM()
            LIMIT 11
            """)
    List<Question> findRandomByCategory(int categoryId);

    // ジャンル指定で11問ランダム抽出（ALL選択時）
    @Select("""
            SELECT q.id, q.category_id, q.question_text,
                   q.choice_1, q.choice_2, q.choice_3, q.choice_4,
                   q.correct_answer, q.explanation, q.source_name, updated_at
            FROM questions q
            INNER JOIN categories c ON c.id = q.category_id
            WHERE c.genre_id = #{genreId}
            AND q.delete_flag = FALSE
            ORDER BY RANDOM()
            LIMIT 11
            """)
    List<Question> findRandomByGenre(int genreId);
}