package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.User;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.service.user.AuthService;
import com.example.shikakurush.service.user.NgWordService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final NgWordService ngWordService;

    public AuthController(AuthService authService, NgWordService ngWordService) {
        this.authService = authService;
        this.ngWordService = ngWordService;
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

    // 登録用メールアドレス入力画面
    @GetMapping("/user/registered-email")
    public String registeredEmail() {
        return "user/registered-email";
    }

    // 登録メール送信処理
    @PostMapping("/user/registered-email")
    public String sendRegistrationEmail(@RequestParam String email,
                                        Model model) {
        if (email == null || email.isBlank()) {
            model.addAttribute("emailError", "メールアドレスを入力してください");
            model.addAttribute("sent", false);
            return "user/registered-email";
        }

        try {
            authService.sendRegistrationEmail(email);
            model.addAttribute("message",
                    "確認メールを送信しました。メール内のリンクから登録を続けてください。");
            model.addAttribute("sent", true);
        } catch (AuthException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("sent", false);
        }
        return "user/registered-email";
    }

    // 登録画面の表示（メール内URLからアクセス）
    @GetMapping("/user/registration")
    public String showRegisterForm(@RequestParam(required = false) String token,
                                   Model model) {
        if (token == null || token.isBlank()) {
            model.addAttribute("tokenError", true);
            return "user/registration";
        }

        try {
            authService.validateRegistrationToken(token);
            model.addAttribute("token", token);
            model.addAttribute("tokenError", false);
        } catch (AuthException e) {
            model.addAttribute("tokenError", true);
        }
        return "user/registration";
    }

    // 登録処理
    @PostMapping("/user/registration")
    public String register(@RequestParam String token,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String passwordConfirm,
                           Model model,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        // バリデーション
        boolean valid = true;

        if (username == null || username.isBlank()) {
            model.addAttribute("usernameError", "ユーザーネームを入力してください");
            valid = false;
        } else if (!username.matches("^[a-zA-Z0-9]{1,15}$")) {
            model.addAttribute("usernameError", "英数字のみ・15文字以内で入力してください");
            valid = false;
        } else if (ngWordService.containsNgWord(username)) {
            model.addAttribute("usernameError", "使用できない文字列が含まれています");
            valid = false;
        }

        if (password == null || password.isBlank()) {
            model.addAttribute("passwordError", "パスワードを入力してください");
            valid = false;
        } else if (password.length() < 8) {
            model.addAttribute("passwordError", "パスワードは8文字以上で入力してください");
            valid = false;
        }

        if (passwordConfirm == null || passwordConfirm.isBlank()) {
            model.addAttribute("passwordConfirmError", "パスワードを再入力してください");
            valid = false;
        } else if (!password.equals(passwordConfirm)) {
            model.addAttribute("passwordConfirmError", "パスワードが一致しません");
            valid = false;
        }

        if (!valid) {
            model.addAttribute("token", token);
            model.addAttribute("tokenError", false);
            model.addAttribute("username", username);
            return "user/registration";
        }

        try {
            User user = authService.registerComplete(token, username, password);
            // 登録完了後に自動ログイン
            session.setAttribute("userId", user.getId());
            session.setAttribute("email", user.getEmail());
            redirectAttributes.addFlashAttribute("message", "登録が完了しました。");
            return "redirect:/user/start";
        } catch (AuthException e) {
            if ("USERNAME_TAKEN".equals(e.getCode())) {
                model.addAttribute("usernameError", "このユーザーネームはすでに使われています");
            } else if ("TOKEN_EXPIRED".equals(e.getCode()) || "TOKEN_INVALID".equals(e.getCode())) {
                model.addAttribute("tokenError", true);
                return "user/registration";
            } else {
                model.addAttribute("error", e.getMessage());
            }
            model.addAttribute("token", token);
            model.addAttribute("tokenError", false);
            model.addAttribute("username", username);
            return "user/registration";
        }
    }
}