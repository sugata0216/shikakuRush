package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.*;
import com.example.shikakurush.repository.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final QuestionRepository questionRepository;
    private final ScoreHistoryRepository scoreHistoryRepository;
    private final ScoreHistoryQuestionRepository scoreHistoryQuestionRepository;

    // 問題を抽出してシャッフル
    public List<Question> prepareQuestions(Integer categoryId, int genreId) {
        List<Question> questions;

        if (categoryId == null || isAllCategory(categoryId)) {
            questions = questionRepository.findRandomByGenre(genreId);
        } else {
            questions = questionRepository.findRandomByCategory(categoryId);
        }

        questions.forEach(this::shuffleChoices);
        return questions;
    }

    // スコア保存処理
    @Transactional
    public void saveResult(Integer userId,
                           Integer categoryId,
                           Integer difficultyId,
                           int score,
                           int correctCount,
                           int comboMax,
                           int timeBonus,
                           List<Question> questions) {

        // 1. score_historiesにINSERT
        ScoreHistory history = new ScoreHistory();
        history.setUserId(userId);
        history.setCategoryId(categoryId);
        history.setDifficultyId(difficultyId);
        history.setScore(score);
        scoreHistoryRepository.save(history);

        // 2. score_history_questionsにINSERT（10問分）
        for (Question question : questions) {
            ScoreHistoryQuestion shq = new ScoreHistoryQuestion();
            shq.setScoreHistoryId(history.getId());
            shq.setQuestionId(question.getId());
            shq.setQuestionUpdatedAt(question.getUpdatedAt());
            scoreHistoryQuestionRepository.save(shq);
        }
    }

    // ALLカテゴリのIDを判定
    private boolean isAllCategory(int categoryId) {
        List<Integer> allCategoryIds = List.of(1, 8, 15);
        return allCategoryIds.contains(categoryId);
    }

    // 各問題の選択肢をシャッフル
    private void shuffleChoices(Question question) {
        List<String> choices = new java.util.ArrayList<>(List.of(
                question.getChoice1(),
                question.getChoice2(),
                question.getChoice3(),
                question.getChoice4()
        ));

        int correctIndex = Integer.parseInt(question.getCorrectAnswer()) - 1;
        String correctText = choices.get(correctIndex);

        Collections.shuffle(choices);

        int newCorrectIndex = choices.indexOf(correctText);
        question.setCorrectAnswer(String.valueOf(newCorrectIndex + 1));

        question.setChoice1(choices.get(0));
        question.setChoice2(choices.get(1));
        question.setChoice3(choices.get(2));
        question.setChoice4(choices.get(3));
    }
}