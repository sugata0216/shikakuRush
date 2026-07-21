package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Title;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.repository.user.TitleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TitleService {

    private final TitleRepository titleRepository;

    public TitleService(TitleRepository titleRepository) {
        this.titleRepository = titleRepository;
    }

    // ── 全称号一覧と所持情報を取得 ────────────────────────
    public List<Title> findAll() {
        return titleRepository.findAll();
    }

    // ── ユーザーの所持称号IDリストを取得 ──────────────────
    public List<Integer> findOwnedTitleIds(int userId) {
        return titleRepository.findOwnedTitleIdsByUserId(userId);
    }

    // ── 現在選択中の称号IDを取得 ──────────────────────────
    public Integer findSelectedTitleId(int userId) {
        return titleRepository.findSelectedTitleIdByUserId(userId);
    }

    // ── 選択中の称号を更新 ────────────────────────────────
    public void updateSelectedTitle(int userId, int titleId) {
        // 所持しているか確認
        List<Integer> ownedTitleIds = titleRepository.findOwnedTitleIdsByUserId(userId);
        if (!ownedTitleIds.contains(titleId)) {
            throw AuthException.forbidden(); // 所持していない称号は選択不可
        }
        titleRepository.updateSelectedTitle(userId, titleId);
    }
}