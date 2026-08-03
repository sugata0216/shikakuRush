package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.Category;
import com.example.shikakurush.entity.ScoreHistory;
import com.example.shikakurush.entity.Title;
import com.example.shikakurush.entity.User;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.service.user.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class MyPageController {

    private final UserService userService;
    private final UserDeleteService userDeleteService;
    private final TitleService titleService;
    private final ScoreHistoryService scoreHistoryService;
    private final DifficultyService difficultyService;
    private final NgWordService ngWordService;

    public MyPageController(UserService userService,
                            UserDeleteService userDeleteService,
                            TitleService titleService, ScoreHistoryService scoreHistoryService, DifficultyService difficultyService, NgWordService ngWordService) {
        this.userService = userService;
        this.userDeleteService = userDeleteService;
        this.titleService = titleService;
        this.scoreHistoryService = scoreHistoryService;
        this.difficultyService = difficultyService;
        this.ngWordService = ngWordService;
    }

    // ページルーティング（スコア取得を追加）
    @GetMapping("/user/my-page")
    public String myPage(@RequestParam(defaultValue = "1") int genreId,
                         @RequestParam(defaultValue = "1") int difficultyId,
                         @RequestParam(defaultValue = "1") int tab,
                         HttpSession session,
                         Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }

        Integer userId = (Integer) session.getAttribute("userId");
        User user = userService.findById(userId);
        List<Title> allTitles = titleService.findAll();
        List<Integer> ownedTitleIds = titleService.findOwnedTitleIds(userId);
        Integer selectedTitleId = titleService.findSelectedTitleId(userId);

        List<Title> ownedTitles = allTitles.stream()
                .filter(t -> ownedTitleIds.contains(t.getId()))
                .collect(java.util.stream.Collectors.toList());

        // スコア取得
        Map<Category, List<ScoreHistory>> scoreMap = scoreHistoryService
                .findScoresByGenreAndDifficulty(userId, genreId, difficultyId);

        model.addAttribute("username", user.getUsername());
        model.addAttribute("usernameChangedThisMonth", user.isUsernameChangedThisMonth());
        model.addAttribute("ownedTitles", ownedTitles);
        model.addAttribute("selectedTitleId", selectedTitleId);
        model.addAttribute("scoreMap", scoreMap);                          // ✅ 追加
        model.addAttribute("selectedGenreId", genreId);                   // ✅ 追加
        model.addAttribute("selectedDifficultyId", difficultyId);         // ✅ 追加
        model.addAttribute("difficulties", difficultyService.findAll());  // ✅ 追加
        model.addAttribute("activeTab", tab);
        return "user/my-page";
    }

    @PostMapping("/user/my-page/delete")
    public String deleteUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        userDeleteService.deleteUser(userId);
        session.invalidate();
        return "redirect:/user/login";
    }

    // セッションからユーザーIDを取得
    private Integer getUserIdFromSession(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            throw AuthException.loginFailed();
        }
        return userId;
    }

    // AuthExceptionのハンドリング
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleAuthException(AuthException ex) {
        int status = switch (ex.getCode()) {
            case "LOGIN_FAILED"             -> 401;
            case "USERNAME_TAKEN"           -> 409;
            case "USERNAME_ALREADY_CHANGED" -> 403;
            case "FORBIDDEN"                -> 403;
            default                         -> 400;
        };
        return ResponseEntity.status(status)
                .body(Map.of("code", ex.getCode(), "message", ex.getMessage()));
    }

    // ユーザー名変更
    @PostMapping("/user/my-page/username")
    public String updateUsername(@RequestParam String username,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (username == null || username.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("usernameError", "ユーザー名を入力してください");
            return "redirect:/user/my-page";
        }
        if (!username.matches("^[a-zA-Z0-9]{1,15}$")) {
            redirectAttributes.addFlashAttribute("usernameError", "英数字のみ・15文字以内で入力してください");
            return "redirect:/user/my-page";
        }

        try {
            userService.updateUsername(userId, username);
            redirectAttributes.addFlashAttribute("usernameSuccess", "ユーザー名を変更しました");
        } catch (AuthException e) {
            redirectAttributes.addFlashAttribute("usernameError", e.getMessage());
        }
        return "redirect:/user/my-page";
    }

    // 称号変更
    @PostMapping("/user/my-page/title")
    public String updateTitle(@RequestParam Integer titleId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Integer userId = (Integer) session.getAttribute("userId");

        try {
            titleService.updateSelectedTitle(userId, titleId);
            redirectAttributes.addFlashAttribute("titleSuccess", "称号を変更しました");
        } catch (AuthException e) {
            redirectAttributes.addFlashAttribute("titleError", e.getMessage());
        }
        return "redirect:/user/my-page";
    }
}