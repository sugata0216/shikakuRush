package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.Ranking;
import com.example.shikakurush.service.user.DifficultyService;
import com.example.shikakurush.service.user.GenreService;
import com.example.shikakurush.service.user.RankingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RankingController {

    private final RankingService rankingService;
    private final DifficultyService difficultyService;
    private final GenreService genreService; // 追加

    public RankingController(RankingService rankingService,
                             DifficultyService difficultyService,
                             GenreService genreService) { // 追加
        this.rankingService = rankingService;
        this.difficultyService = difficultyService;
        this.genreService = genreService; // 追加
    }

    @GetMapping("/user/ranking")
    public String ranking(@RequestParam(defaultValue = "1") int genreId,
                          @RequestParam(defaultValue = "1") int difficultyId,
                          HttpSession session,
                          Model model) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/user/login";
        }

        List<Ranking> rankings = rankingService.findTop5(genreId, difficultyId);

        model.addAttribute("rankings", rankings);
        model.addAttribute("selectedGenreId", genreId);
        model.addAttribute("selectedDifficultyId", difficultyId);
        model.addAttribute("difficulties", difficultyService.findAll());
        model.addAttribute("genres", genreService.findAll()); // 追加

        return "user/ranking";
    }
}