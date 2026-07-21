package com.example.shikakurush.controller.user;

import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.service.user.PasswordResetService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @GetMapping("/user/password-reset-email")
    public String passwordResetEmail() {
        return "user/password-reset-email";
    }

    @GetMapping("/user/password-reset")
    public String passwordReset() {
        return "user/password-reset";
    }

    @GetMapping("/user/password-reset-complete")
    public String passwordResetComplete() {
        return "user/password-reset-complete";
    }

    @PostMapping("/api/auth/password-reset/send-email")
    @ResponseBody
    public ResponseEntity<Void> sendEmail(@RequestBody Map<String, String> req) {
        passwordResetService.sendPasswordResetEmail(req.get("email"));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/auth/password-reset/complete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> req) {
        passwordResetService.resetPassword(
                req.get("token"),
                req.get("password"),
                req.get("passwordConfirm")
        );
        return ResponseEntity.ok(Map.of("message", "パスワードをリセットしました"));
    }

    // AuthExceptionのハンドリング
    @ExceptionHandler(AuthException.class)
    @ResponseBody
    public ResponseEntity<Map<String, String>> handleAuthException(AuthException ex) {
        int status = switch (ex.getCode()) {
            case "TOKEN_EXPIRED",
                 "TOKEN_INVALID"  -> 400;
            case "EMAIL_NOT_FOUND" -> 404;
            case "PASSWORD_MISMATCH" -> 400;
            default               -> 400;
        };
        return ResponseEntity.status(status)
                .body(Map.of("code", ex.getCode(), "message", ex.getMessage()));
    }
}