package com.example.shikakurush.mapper.admin;

import com.example.shikakurush.entity.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {

    @Select("SELECT * FROM admin WHERE id = 1")
    Admin find();
}