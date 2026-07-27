package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.Difficulty;
import com.example.shikakurush.repository.user.DifficultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DifficultyService {

    private final DifficultyRepository difficultyRepository;

    public DifficultyService(DifficultyRepository difficultyRepository) {
        this.difficultyRepository = difficultyRepository;
    }

    public List<Difficulty> findAll() {
        return difficultyRepository.findAll();
    }
}