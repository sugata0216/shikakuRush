package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Genre;
import com.example.shikakurush.mapper.user.GenreMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GenreRepository {

    private final GenreMapper genreMapper;

    public GenreRepository(GenreMapper genreMapper) {
        this.genreMapper = genreMapper;
    }

    public List<Genre> findAll() {
        return genreMapper.findAll();
    }
}