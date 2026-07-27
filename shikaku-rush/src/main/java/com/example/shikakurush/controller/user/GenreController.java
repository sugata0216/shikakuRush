package com.example.shikakurush.controller.user;

import com.example.shikakurush.service.user.GenreService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GenreController {

    public GenreController() {}

    // ジャンル選択ページ
    @GetMapping("/user/genre")
    public String genre(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }
        return "user/genre-selection";
    }
}