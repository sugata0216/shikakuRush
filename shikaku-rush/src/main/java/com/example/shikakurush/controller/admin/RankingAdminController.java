package com.example.shikakurush.controller.admin;

import com.example.shikakurush.entity.Ranking;
import com.example.shikakurush.service.admin.UserAdminService;
import com.example.shikakurush.service.user.DifficultyService;
import com.example.shikakurush.service.user.GenreService;
import com.example.shikakurush.service.user.RankingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class RankingAdminController {

    private final RankingService rankingService;
    private final UserAdminService userAdminService;
    private final GenreService genreService;
    private final DifficultyService difficultyService;

    public RankingAdminController(RankingService rankingService,
                                  UserAdminService userAdminService,
                                  GenreService genreService,
                                  DifficultyService difficultyService) {
        this.rankingService = rankingService;
        this.userAdminService = userAdminService;
        this.genreService = genreService;
        this.difficultyService = difficultyService;
    }

    @GetMapping("/admin/ranking")
    public String rankingList(@RequestParam(defaultValue = "1") int genreId,
                              @RequestParam(defaultValue = "1") int difficultyId,
                              HttpSession session,
                              Model model) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        List<Ranking> rankings = rankingService.findTop5(genreId, difficultyId);

        model.addAttribute("rankings", rankings);
        model.addAttribute("selectedGenreId", genreId);
        model.addAttribute("selectedDifficultyId", difficultyId);
        model.addAttribute("genres", genreService.findAll());           // 追加
        model.addAttribute("difficulties", difficultyService.findAll()); // 追加

        return "admin/ranking-management";
    }

    @PostMapping("/admin/ranking/ban")
    public String banFromRanking(@RequestParam(required = false) Integer id,
                                 @RequestParam int genreId,
                                 @RequestParam int difficultyId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        if (session.getAttribute("adminId") == null) {
            return "redirect:/admin/login";
        }

        // ラジオボタンが未選択の場合
        if (id == null) {
            redirectAttributes.addFlashAttribute("error", "BANするユーザーを選択してください");
            return "redirect:/admin/ranking?genreId=" + genreId + "&difficultyId=" + difficultyId;
        }

        userAdminService.banUser(id);
        redirectAttributes.addFlashAttribute("message", "ユーザーをBANしました");

        return "redirect:/admin/ranking?genreId=" + genreId + "&difficultyId=" + difficultyId;
    }
}