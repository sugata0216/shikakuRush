package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Genre;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface GenreMapper {

    // 全ジャンル一覧を取得
    @Select("SELECT id, name FROM genres ORDER BY id")
    List<Genre> findAll();
}