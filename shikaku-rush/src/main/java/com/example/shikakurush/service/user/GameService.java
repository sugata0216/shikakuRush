package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Question;
import com.example.shikakurush.repository.user.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    private final QuestionRepository questionRepository;

    public GameService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // 問題を抽出してシャッフル
    public List<Question> prepareQuestions(Integer categoryId, int genreId) {
        List<Question> questions;

        // categoryIdがnullまたはALLカテゴリ（ID=1）の場合はジャンル全体から抽出
        if (categoryId == null || isAllCategory(categoryId)) {
            questions = questionRepository.findRandomByGenre(genreId);
        } else {
            questions = questionRepository.findRandomByCategory(categoryId);
        }

        questions.forEach(this::shuffleChoices);
        return questions;
    }

    // ALLカテゴリのIDを判定
    private boolean isAllCategory(int categoryId) {
        List<Integer> allCategoryIds = List.of(1, 8, 15);
        return allCategoryIds.contains(categoryId);
    }

    // 各問題の選択肢をシャッフル
    private void shuffleChoices(Question question) {
        // 選択肢と正解をリストに変換
        List<String> choices = new java.util.ArrayList<>(List.of(
                question.getChoice1(),
                question.getChoice2(),
                question.getChoice3(),
                question.getChoice4()
        ));

        // シャッフル前の正解テキストを保持
        int correctIndex = Integer.parseInt(question.getCorrectAnswer()) - 1;
        String correctText = choices.get(correctIndex);

        // 選択肢をシャッフル
        Collections.shuffle(choices);

        // シャッフル後の正解インデックスを更新
        int newCorrectIndex = choices.indexOf(correctText);
        question.setCorrectAnswer(String.valueOf(newCorrectIndex + 1));

        // シャッフル後の選択肢をセット
        question.setChoice1(choices.get(0));
        question.setChoice2(choices.get(1));
        question.setChoice3(choices.get(2));
        question.setChoice4(choices.get(3));
    }
}