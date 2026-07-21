package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.PasswordResetToken;
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
    public String registeredEmail() {
        return "user/registered-email";
    }

    @GetMapping("/registration")
    public String registration(@RequestParam String token,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            authService.validateRegistrationToken(token);
            model.addAttribute("token", token);
            return "user/registration";
        } catch (AuthException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/login";
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

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }

    @PostMapping("/send-registration-email")
    public String sendRegistrationEmail(@RequestParam String email,
                                        RedirectAttributes redirectAttributes) {
        try {
            authService.sendRegistrationEmail(email);
            return "redirect:/user/registered-email";
        } catch (AuthException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/login";
        }
    }

    @PostMapping("/registration")
    public String registerComplete(@RequestParam String token,
                                   @RequestParam String username,
                                   @RequestParam String password,
                                   RedirectAttributes redirectAttributes) {
        try {
            authService.registerComplete(token, username, password);
            return "redirect:/user/login";
        } catch (AuthException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/user/registration?token=" + token;
        }
    }
}