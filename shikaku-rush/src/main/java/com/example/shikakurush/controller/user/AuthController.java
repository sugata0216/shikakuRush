package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.User;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.service.user.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // ログイン
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Map<String, String> req,
            HttpSession session) {

        User user = authService.login(req.get("email"), req.get("password"));
        session.setAttribute("userId", user.getId());
        session.setAttribute("email", user.getEmail());

        return ResponseEntity.ok(Map.of("message", "ログインしました"));
    }

    // ログアウト
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    // 登録メール送信
    @PostMapping("/register/send-email")
    public ResponseEntity<Void> sendEmail(@RequestBody Map<String, String> req) {
        authService.sendRegistrationEmail(req.get("email"));
        return ResponseEntity.ok().build();
    }

    // 登録完了
    @PostMapping("/register/complete")
    public ResponseEntity<Map<String, String>> registerComplete(
            @RequestBody Map<String, String> req,
            HttpSession session) {

        User user = authService.registerComplete(
                req.get("token"),
                req.get("username"),
                req.get("password")
        );
        // 登録完了後に自動ログイン
        session.setAttribute("userId", user.getId());
        session.setAttribute("email", user.getEmail());

        return ResponseEntity.ok(Map.of("message", "登録が完了しました"));
    }

    // AuthExceptionのハンドリング
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Map<String, String>> handleAuthException(AuthException ex) {
        int status = switch (ex.getCode()) {
            case "LOGIN_FAILED"            -> 401;
            case "TOKEN_EXPIRED",
                 "TOKEN_INVALID"           -> 400;
            case "EMAIL_TAKEN",
                 "USERNAME_TAKEN"          -> 409;
            default                        -> 400;
        };
        return ResponseEntity.status(status)
                .body(Map.of("code", ex.getCode(), "message", ex.getMessage()));
    }
}