package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Inquiry;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InquiryMapper {

    // お問い合わせ登録
    @Insert("""
            INSERT INTO inquiries (user_id, title, body, created_at)
            VALUES (#{userId}, #{title}, #{body}, CURRENT_TIMESTAMP)
            """)
    void insert(Inquiry inquiry);
}