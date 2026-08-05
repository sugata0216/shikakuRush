package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Report;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ReportMapper {

    @Insert("INSERT INTO reports (user_id, question_id, detail, created_at) " +
            "VALUES (#{userId}, #{questionId}, #{detail}, NOW())")
    void insert(Report report);

    // ✅ 問題報告一覧を取得
    @Select("""
        SELECT r.id, r.user_id, r.question_id, r.detail, r.created_at,
               q.question_text, q.choice_1, q.choice_2, q.choice_3, q.choice_4
        FROM reports r
        INNER JOIN questions q ON q.id = r.question_id
        ORDER BY r.created_at DESC
        """)
    List<Report> findAll();
}