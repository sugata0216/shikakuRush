package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Difficulty;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DifficultyMapper {

    // 全難易度一覧を取得
    @Select("SELECT id, name FROM difficulties ORDER BY id")
    List<Difficulty> findAll();
}