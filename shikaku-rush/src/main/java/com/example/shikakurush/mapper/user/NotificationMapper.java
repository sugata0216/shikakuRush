package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotificationMapper {

    // 当月分のお知らせ一覧を取得
    @Select("""
            SELECT id, title, body, posted_at, updated_at
            FROM notifications
            WHERE deleted_at IS NULL
            OR deleted_at >= DATE_TRUNC('month', CURRENT_DATE)
            ORDER BY posted_at DESC
            """)
    List<Notification> findCurrentMonth();
}