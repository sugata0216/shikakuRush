package com.example.shikakurush.controller.user;

import com.example.shikakurush.entity.Ranking;
import com.example.shikakurush.service.user.DifficultyService;
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

    public RankingController(RankingService rankingService,
                             DifficultyService difficultyService) {
        this.rankingService = rankingService;
        this.difficultyService = difficultyService;
    }

    // ランキング画面
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

        return "user/ranking";
    }
}