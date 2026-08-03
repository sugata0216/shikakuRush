package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Report;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReportMapper {

    @Insert("INSERT INTO reports (user_id, question_id, detail, created_at) " +
            "VALUES (#{userId}, #{questionId}, #{detail}, NOW())")
    void insert(Report report);
}