package com.example.shikakurush.controller.user;

import com.example.shikakurush.service.user.DifficultyService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DifficultyController {

    private final DifficultyService difficultyService;

    public DifficultyController(DifficultyService difficultyService) {
        this.difficultyService = difficultyService;
    }

    // 難易度選択ページ
    @GetMapping("/user/difficulty")
    public String difficulty(@RequestParam int genreId,
                             @RequestParam(required = false) Integer categoryId,
                             HttpSession session,
                             Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("genreId", genreId);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("difficulties", difficultyService.findAll());
        return "user/difficulty-selection";
    }
}