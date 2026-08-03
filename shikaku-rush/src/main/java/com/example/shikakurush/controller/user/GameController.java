package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.Question;
import com.example.shikakurush.service.user.CategoryService;
import com.example.shikakurush.service.user.GameService;
import com.example.shikakurush.service.user.RankingService;
import com.example.shikakurush.service.user.ScoreHistoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GameController {

    private final GameService gameService;
    private final CategoryService categoryService;
    private final ScoreHistoryService scoreHistoryService;
    private final RankingService rankingService;

    public GameController(GameService gameService, CategoryService categoryService, ScoreHistoryService scoreHistoryService, RankingService rankingService) {
        this.gameService = gameService;
        this.categoryService = categoryService;
        this.scoreHistoryService = scoreHistoryService;
        this.rankingService = rankingService;
    }

    // ローディング画面
    @GetMapping("/user/loading")
    public String loading(@RequestParam int genreId,
                          @RequestParam(required = false) Integer categoryId,
                          @RequestParam int difficultyId,
                          HttpSession session,
                          Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }

        List<Question> questions = gameService.prepareQuestions(categoryId, genreId);
        int lives = (difficultyId == 2) ? 1 : Integer.MAX_VALUE; // ノーマルは無限、ハードは1
        session.setAttribute("questions", questions);
        session.setAttribute("currentIndex", 0);
        session.setAttribute("changeUsed", false);
        session.setAttribute("difficultyId", difficultyId);
        session.setAttribute("genreId", genreId);
        session.setAttribute("categoryId", categoryId);
        session.setAttribute("lives", lives);

        // ジャンルIDによって制限時間を設定
        int timeLimit = switch (genreId) {
            case 1, 2 -> 30;
            case 3    -> 15;
            default   -> 30;
        };
        session.setAttribute("timeLimit", timeLimit);

        model.addAttribute("genreId", genreId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("difficultyId", difficultyId);

        return "user/loading";
    }

    // ゲーム画面
    @GetMapping("/user/game")
    public String game(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }

        List<Question> questions = (List<Question>) session.getAttribute("questions");
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");
        Boolean changeUsed = (Boolean) session.getAttribute("changeUsed");

        if (questions == null || currentIndex == null) {
            return "redirect:/user/genre-selection";
        }

        Question currentQuestion = questions.get(currentIndex);

        // カテゴリ名を取得してModelに渡す
        String categoryName = categoryService.findByGenreId(
                        (Integer) session.getAttribute("genreId"))
                .stream()
                .filter(c -> c.getId() == currentQuestion.getCategoryId())
                .map(c -> c.getName())
                .findFirst()
                .orElse("不明");

        model.addAttribute("question", currentQuestion);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("currentIndex", currentIndex);
        model.addAttribute("totalQuestions", 10);
        model.addAttribute("changeUsed", changeUsed);

        return "user/game";
    }

    // Changeボタン（問題入れ替え）
    @GetMapping("/user/game/change")
    public String change(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }

        Boolean changeUsed = (Boolean) session.getAttribute("changeUsed");
        List<Question> questions = (List<Question>) session.getAttribute("questions");
        Integer currentIndex = (Integer) session.getAttribute("currentIndex");

        // Changeが既に使用済みまたはセッションが不正な場合はゲーム画面に戻す
        if (Boolean.TRUE.equals(changeUsed) || questions == null || currentIndex == null) {
            return "redirect:/user/game";
        }

        // 現在の問題を11問目と入れ替え
        Question spare = questions.get(10);
        questions.set(10, questions.get(currentIndex));
        questions.set(currentIndex, spare);

        session.setAttribute("questions", questions);
        session.setAttribute("changeUsed", true);

        return "redirect:/user/game";
    }
    // 回答処理
    @PostMapping("/user/game/answer")
    public String answer(@RequestParam(required = false) String answer,
                         @RequestParam(defaultValue = "false") boolean timedOut,
                         @RequestParam(defaultValue = "60") int timeLeft,
                         HttpSession session) {

        List<Question> questions = (List<Question>) session.getAttribute("questions");
        Integer currentIndex     = getSessionAttr(session, "currentIndex", 0);
        Integer score            = getSessionAttr(session, "score", 0);
        Integer combo            = getSessionAttr(session, "combo", 0);
        Integer correctCount     = getSessionAttr(session, "correctCount", 0);
        Integer difficultyId     = getSessionAttr(session, "difficultyId", 1);
        Integer lives            = getSessionAttr(session, "lives", Integer.MAX_VALUE);

        if (questions == null) {
            return "redirect:/user/genre";
        }

        Question currentQuestion = questions.get(currentIndex);
        boolean isCorrect = !timedOut
                && answer != null
                && answer.equals(currentQuestion.getCorrectAnswer());

        if (isCorrect) {
            combo++;
            correctCount++;
            int baseScore  = (difficultyId == 1) ? 10 : 100;
            int timeBonus  = timeLeft * 10;
            int comboBonus = combo * 50;
            score += baseScore + timeBonus + comboBonus;
        } else {
            combo = 0;
            if (lives != Integer.MAX_VALUE) {
                lives--;
            }
        }

        List<Boolean> correctList = (List<Boolean>) session.getAttribute("correctList");
        if (correctList == null) {
            correctList = new java.util.ArrayList<>();
        }
        correctList.add(isCorrect);
        session.setAttribute("correctList", correctList);

        session.setAttribute("score", score);
        session.setAttribute("combo", combo);
        session.setAttribute("correctCount", correctCount);
        session.setAttribute("changeUsed", false);
        session.setAttribute("currentIndex", currentIndex + 1);
        session.setAttribute("lives", lives);

        // ハードモードで残機が尽きたらゲームオーバーとして結果画面へ
        if (lives <= 0) {
            session.setAttribute("gameOver", true);
            return "redirect:/user/result";
        }

        if (currentIndex + 1 >= 10) {
            return "redirect:/user/result";
        }

        return "redirect:/user/game";
    }

    // Give Up
    @PostMapping("/user/game/giveup")
    public String giveUp(HttpSession session) {
        // ゲーム関連のセッションをクリア
        session.removeAttribute("questions");
        session.removeAttribute("currentIndex");
        session.removeAttribute("score");
        session.removeAttribute("combo");
        session.removeAttribute("correctCount");
        session.removeAttribute("changeUsed");
        session.removeAttribute("difficultyId");
        session.removeAttribute("genreId");
        session.removeAttribute("categoryId");
        session.removeAttribute("timeLeft");

        return "redirect:/user/genre";
    }

    private <T> T getSessionAttr(HttpSession session, String key, T defaultValue) {
        Object val = session.getAttribute(key);
        return val != null ? (T) val : defaultValue;
    }
}