package com.example.shikakurush.controller.user;

import com.example.shikakurush.service.user.CategoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // カテゴリ選択ページ
    @GetMapping("/user/category")
    public String category(@RequestParam int genreId,
                           HttpSession session,
                           Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }
        model.addAttribute("genreId", genreId);
        model.addAttribute("categories", categoryService.findByGenreId(genreId));
        return "user/category-selection";
    }
}