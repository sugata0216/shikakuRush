package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.User;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.service.user.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class LoginController {

    private final AuthService authService;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/registered-email")
    public String registeredEmail(Model model) {
        model.addAttribute("sent", false);
        return "user/registered-email";
    }

    @PostMapping("/registered-email")
    public String sendRegistrationEmail(@RequestParam String email,
                                        Model model) {
        try {
            authService.sendRegistrationEmail(email);
            model.addAttribute("sent", true);
            model.addAttribute("message", "確認メールを送信しました。メール内のリンクから登録を続けてください。");
            return "user/registered-email";
        } catch (AuthException e) {
            model.addAttribute("sent", false);
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            return "user/registered-email";
        }
    }

    @GetMapping("/registration")
    public String registration(@RequestParam String token,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            authService.validateRegistrationToken(token);
            model.addAttribute("token", token);
            model.addAttribute("tokenError", false);
            return "user/registration";
        } catch (AuthException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/login";
        }
    }

    @PostMapping("/registration")
    public String registerComplete(@RequestParam String token,
                                   @RequestParam String username,
                                   @RequestParam String password,
                                   @RequestParam String passwordConfirm,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        model.addAttribute("token", token);
        model.addAttribute("tokenError", false);
        model.addAttribute("username", username);

        // パスワード一致チェック
        if (!password.equals(passwordConfirm)) {
            model.addAttribute("passwordConfirmError", "パスワードが一致しません");
            return "user/registration";
        }

        // パスワード長チェック
        if (password.length() < 8) {
            model.addAttribute("passwordError", "パスワードは8文字以上で入力してください");
            return "user/registration";
        }

        try {
            authService.registerComplete(token, username, password);
            return "redirect:/user/login";
        } catch (AuthException e) {
            if (e.getCode().equals("TOKEN_EXPIRED") || e.getCode().equals("TOKEN_INVALID")) {
                model.addAttribute("tokenError", true);
            } else if (e.getCode().equals("INVALID_USERNAME")) {
                model.addAttribute("usernameError", e.getMessage());
            } else if (e.getCode().equals("USERNAME_TAKEN")) {
                model.addAttribute("usernameError", e.getMessage());
            } else {
                model.addAttribute("error", e.getMessage());
            }
            return "user/registration";
        }
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String email,
                            @RequestParam String password,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            User user = authService.login(email, password);
            session.setAttribute("userId", user.getId());
            return "redirect:/";
        } catch (AuthException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/login";
        }
    }
}