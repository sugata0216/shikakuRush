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
    @Select("SELECT * FROM inquiries ORDER BY id DESC")
    List<Inquiry> findAll();

    // 詳細取得（管理者用）
    @Select("SELECT * FROM inquiries WHERE id = #{id}")
    Inquiry findById(int id);
}