package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.Category;
import com.example.shikakurush.entity.Question;
import com.example.shikakurush.entity.Ranking;
import com.example.shikakurush.service.user.CategoryService;
import com.example.shikakurush.service.user.RankingService;
import com.example.shikakurush.service.user.ResultService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class ResultController {

    private final ResultService resultService;
    private final CategoryService categoryService;
    private final RankingService rankingService; // ✅ 追加
    public ResultController(ResultService resultService, CategoryService categoryService, RankingService rankingService) {
        this.resultService   = resultService;
        this.categoryService = categoryService;
        this.rankingService = rankingService;
    }

    @GetMapping("/user/result")
    public String result(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }

        Integer userId       = (Integer) session.getAttribute("userId");
        Integer score        = getSessionAttr(session, "score", 0);
        Integer correctCount = getSessionAttr(session, "correctCount", 0);
        Integer difficultyId = getSessionAttr(session, "difficultyId", 1);
        Integer genreId      = getSessionAttr(session, "genreId", 1);
        Integer categoryId   = (Integer) session.getAttribute("categoryId");
        Boolean gameOver     = getSessionAttr(session, "gameOver", false);
        Integer answeredCount = getSessionAttr(session, "currentIndex", 0);
        List<Question> questions = (List<Question>) session.getAttribute("questions");

        // カテゴリ名のマップを作成（categoryId → categoryName）
        Map<Integer, String> categoryNameMap = categoryService.findByGenreId(genreId)
                .stream()
                .collect(Collectors.toMap(Category::getId, Category::getName));

        List<Boolean> correctList = (List<Boolean>) session.getAttribute("correctList");

        // 各問題にカテゴリ名を付与
        if (questions != null) {
            questions.forEach(q -> q.setCategoryName(
                    categoryNameMap.getOrDefault(q.getCategoryId(), "不明")));
        }

        // ✅ ①スコアを先に保存
        // 変更後
        if (!gameOver) {
            resultService.saveScore(userId, categoryId, difficultyId, score, questions);
        }

        // ✅ ②保存後にランキングを取得
        List<Ranking> rankings = null;
        if (!gameOver && (categoryId == null || isAllCategory(categoryId))) {
            rankings = rankingService.findTop5(genreId, difficultyId);
        }

        model.addAttribute("score", score);
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("difficultyId", difficultyId);
        model.addAttribute("genreId", genreId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("questions", questions);
        model.addAttribute("gameOver", gameOver);
        model.addAttribute("correctList", correctList);
        model.addAttribute("rankings", rankings);
        model.addAttribute("answeredCount", answeredCount);

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
        session.removeAttribute("gameOver");
        session.removeAttribute("correctList");
        session.removeAttribute("lives");

        return "user/result";
    }

    private <T> T getSessionAttr(HttpSession session, String key, T defaultValue) {
        Object val = session.getAttribute(key);
        return val != null ? (T) val : defaultValue;
    }

    private boolean isAllCategory(int categoryId) {
        return List.of(1, 8, 15).contains(categoryId);
    }
}