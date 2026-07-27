package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Category;
import com.example.shikakurush.mapper.user.CategoryMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository {

    private final CategoryMapper categoryMapper;

    public CategoryRepository(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    public List<Category> findByGenreId(int genreId) {
        return categoryMapper.findByGenreId(genreId);
    }
}