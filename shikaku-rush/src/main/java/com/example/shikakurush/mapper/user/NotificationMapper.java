package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
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

    // 管理画面用：削除されていない全件を取得
    @Select("""
            SELECT id, title, body, posted_at, updated_at
            FROM notifications
            WHERE deleted_at IS NULL
            ORDER BY posted_at DESC
            """)
    List<Notification> findAll();

    // 管理画面用：編集フォーム表示用に1件取得
    @Select("""
            SELECT id, title, body, posted_at, updated_at
            FROM notifications
            WHERE id = #{id}
            AND deleted_at IS NULL
            """)
    Notification findById(int id);

    // 新規登録
    @Insert("""
            INSERT INTO notifications (title, body, posted_at, updated_at)
            VALUES (#{title}, #{body}, #{postedAt}, #{updatedAt})
            """)
    void insert(Notification notification);

    // 更新
    @Update("""
            UPDATE notifications
            SET title = #{title}, body = #{body}, updated_at = #{updatedAt}
            WHERE id = #{id}
            """)
    void update(Notification notification);

    // 論理削除
    @Update("""
            UPDATE notifications
            SET deleted_at = #{deletedAt}
            WHERE id = #{id}
            """)
    void softDelete(@Param("id") int id, @Param("deletedAt") LocalDateTime deletedAt);
}