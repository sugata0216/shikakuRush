package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Inquiry;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface InquiryMapper {

    // お問い合わせ登録
    @Insert("""
            INSERT INTO inquiries (user_id, title, body, created_at)
            VALUES (#{userId}, #{title}, #{body}, CURRENT_TIMESTAMP)
            """)
    void insert(Inquiry inquiry);

    // 一覧取得（管理者用）
    @Select("SELECT i.*, u.username FROM inquiries i " +
            "LEFT JOIN users u ON i.user_id = u.id " +
            "ORDER BY i.id DESC")
    List<Inquiry> findAll();

    // 詳細取得（管理者用）
    @Select("SELECT i.*, u.username FROM inquiries i " +
            "LEFT JOIN users u ON i.user_id = u.id " +
            "WHERE i.id = #{id}")
    Inquiry findById(int id);
}