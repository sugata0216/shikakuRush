package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Category;
import com.example.shikakurush.repository.user.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findByGenreId(int genreId) {
        return categoryRepository.findByGenreId(genreId);
    }
}