package com.example.shikakurush.controller.user;

import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.service.user.PasswordResetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // パスワードリセットメールアドレス入力画面
    @GetMapping("/user/password-reset-email")
    public String passwordResetEmail(Model model) {
        model.addAttribute("sent", false);
        return "user/password-reset-email";
    }

    // パスワード再設定画面（トークン検証）
    @GetMapping("/user/password-reset")
    public String passwordReset(@RequestParam(required = false) String token,
                                Model model) {
        // トークンがない場合
        if (token == null || token.isBlank()) {
            model.addAttribute("tokenError", true);
            return "user/password-reset";
        }

        // トークンの有効性チェック
        try {
            passwordResetService.validateToken(token);
            model.addAttribute("token", token);
            model.addAttribute("tokenError", false);
        } catch (AuthException e) {
            model.addAttribute("tokenError", true);
        }
        return "user/password-reset";
    }

    // パスワード再設定処理
    @PostMapping("/user/password-reset")
    public String resetPassword(@RequestParam String token,
                                @RequestParam String password,
                                @RequestParam String passwordConfirm,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        // パスワード一致チェック
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("token", token);
            model.addAttribute("tokenError", false);
            model.addAttribute("passwordConfirmError", "パスワードが一致しません");
            return "user/password-reset";
        }

        // パスワード文字数チェック
        if (password.length() < 8) {
            model.addAttribute("token", token);
            model.addAttribute("tokenError", false);
            model.addAttribute("passwordError", "パスワードは8文字以上で入力してください");
            return "user/password-reset";
        }

        try {
            passwordResetService.resetPassword(token, password, passwordConfirm);
            redirectAttributes.addFlashAttribute("message", "パスワードを変更しました。ログインしてください。");
            return "redirect:/user/password-reset-complete";
        } catch (AuthException e) {
            if ("TOKEN_EXPIRED".equals(e.getCode()) || "TOKEN_INVALID".equals(e.getCode())) {
                model.addAttribute("tokenError", true);
            } else {
                model.addAttribute("token", token);
                model.addAttribute("tokenError", false);
                model.addAttribute("error", e.getMessage());
            }
            return "user/password-reset";
        }
    }

    @GetMapping("/user/password-reset-complete")
    public String passwordResetComplete() {
        return "user/password-reset-complete";
    }

    // パスワードリセットメール送信処理
    @PostMapping("/user/password-reset-email")
    public String sendPasswordResetEmail(@RequestParam String email,
                                         Model model) {
        if (email == null || email.isBlank()) {
            model.addAttribute("emailError", "メールアドレスを入力してください");
            model.addAttribute("sent", false);
            return "user/password-reset-email";
        }

        try {
            passwordResetService.sendPasswordResetEmail(email);
            model.addAttribute("message",
                    "パスワードリセット用のメールを送信しました。メール内のリンクからパスワードを再設定してください。");
            model.addAttribute("sent", true);
        } catch (AuthException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            model.addAttribute("sent", false);
        }
        return "user/password-reset-email";
    }
}