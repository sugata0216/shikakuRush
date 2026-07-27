package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CategoryMapper {

    // ジャンルIDに紐づくカテゴリ一覧を取得
    @Select("SELECT id, genre_id, name FROM categories WHERE genre_id = #{genreId} ORDER BY id")
    List<Category> findByGenreId(int genreId);
}