package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.Difficulty;
import com.example.shikakurush.mapper.user.DifficultyMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DifficultyRepository {

    private final DifficultyMapper difficultyMapper;

    public DifficultyRepository(DifficultyMapper difficultyMapper) {
        this.difficultyMapper = difficultyMapper;
    }

    public List<Difficulty> findAll() {
        return difficultyMapper.findAll();
    }
}