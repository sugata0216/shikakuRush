package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Title;
import com.example.shikakurush.mapper.user.TitleMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TitleRepository {

    private final TitleMapper titleMapper;

    public TitleRepository(TitleMapper titleMapper) {
        this.titleMapper = titleMapper;
    }

    // 全称号一覧を取得
    public List<Title> findAll() {
        return titleMapper.findAll();
    }

    // ユーザーの所持称号IDリストを取得
    public List<Integer> findOwnedTitleIdsByUserId(int userId) {
        return titleMapper.findOwnedTitleIdsByUserId(userId);
    }

    // 現在選択中の称号IDを取得
    public Integer findSelectedTitleIdByUserId(int userId) {
        return titleMapper.findSelectedTitleIdByUserId(userId);
    }

    // 選択中の称号を更新
    public void updateSelectedTitle(int userId, int titleId) {
        titleMapper.updateSelectedTitle(userId, titleId);
    }
}