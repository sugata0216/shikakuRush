package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Question;
import com.example.shikakurush.mapper.user.QuestionMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class QuestionRepository {

    private final QuestionMapper questionMapper;

    public QuestionRepository(QuestionMapper questionMapper) {
        this.questionMapper = questionMapper;
    }

    // カテゴリ指定で11問ランダム抽出
    public List<Question> findRandomByCategory(int categoryId) {
        return questionMapper.findRandomByCategory(categoryId);
    }

    // ジャンル指定で11問ランダム抽出（ALL選択時）
    public List<Question> findRandomByGenre(int genreId) {
        return questionMapper.findRandomByGenre(genreId);
    }
}