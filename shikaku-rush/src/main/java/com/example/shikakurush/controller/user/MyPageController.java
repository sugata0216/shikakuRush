package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.Title;
import com.example.shikakurush.entity.User;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.service.user.TitleService;
import com.example.shikakurush.service.user.UserDeleteService;
import com.example.shikakurush.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class MyPageController {

    private final UserService userService;
    private final UserDeleteService userDeleteService;
    private final TitleService titleService;

    public MyPageController(UserService userService,
                            UserDeleteService userDeleteService,
                            TitleService titleService) {
        this.userService = userService;
        this.userDeleteService = userDeleteService;
        this.titleService = titleService;
    }

    @GetMapping("/user/my-page")
    public String myPage(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }
        return "user/my-page";
    }

    // ユーザー情報取得API
    @GetMapping("/api/user/me")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getMe(HttpSession session) {
        Integer userId = getUserIdFromSession(session);
        User user = userService.findById(userId);
        return ResponseEntity.ok(Map.of(
                "username", user.getUsername(),
                "usernameChangedThisMonth", user.isUsernameChangedThisMonth()
        ));
    }

    // ユーザー名変更API
    @PostMapping("/api/user/username")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateUsername(
            @RequestBody Map<String, String> req,
            HttpSession session) {

        Integer userId = getUserIdFromSession(session);
        String username = req.get("username");

        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("code", "INVALID_USERNAME", "message", "ユーザー名を入力してください"));
        }
        if (!username.matches("^[a-zA-Z0-9]{1,15}$")) {
            return ResponseEntity.badRequest()
                    .body(Map.of("code", "INVALID_USERNAME", "message", "英数字のみ・15文字以内で入力してください"));
        }

        userService.updateUsername(userId, username);
        return ResponseEntity.ok(Map.of("message", "ユーザー名を変更しました"));
    }

    // 称号一覧取得API
    @GetMapping("/api/user/titles")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTitles(HttpSession session) {
        Integer userId = getUserIdFromSession(session);
        List<Title> allTitles = titleService.findAll();
        List<Integer> ownedTitleIds = titleService.findOwnedTitleIds(userId);
        Integer selectedTitleId = titleService.findSelectedTitleId(userId);
        return ResponseEntity.ok(Map.of(
                "titles", allTitles,
                "ownedTitleIds", ownedTitleIds,
                "selectedTitleId", selectedTitleId != null ? selectedTitleId : ""
        ));
    }

    // 称号変更API
    @PostMapping("/api/user/title")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateSelectedTitle(
            @RequestBody Map<String, Integer> req,
            HttpSession session) {

        Integer userId = getUserIdFromSession(session);
        Integer titleId = req.get("titleId");

        if (titleId == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("code", "INVALID_TITLE", "message", "称号を選択してください"));
        }

        titleService.updateSelectedTitle(userId, titleId);
        return ResponseEntity.ok(Map.of("message", "称号を変更しました"));
    }

    // 退会API
    @DeleteMapping("/api/user/delete")
    @ResponseBody
    public ResponseEntity<Void> deleteUser(HttpSession session) {
        Integer userId = getUserIdFromSession(session);
        userDeleteService.deleteUser(userId);
        session.invalidate();
        return ResponseEntity.ok().build();
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
}