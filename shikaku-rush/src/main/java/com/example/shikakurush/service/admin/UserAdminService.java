package com.example.shikakurush.service.admin;

import com.example.shikakurush.entity.User;
import com.example.shikakurush.repository.admin.UserAdminRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAdminService {

    private final UserAdminRepository userAdminRepository;

    public UserAdminService(UserAdminRepository userAdminRepository) {
        this.userAdminRepository = userAdminRepository;
    }

    public List<User> findAll() {
        return userAdminRepository.findAll();
    }

    public List<User> search(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return userAdminRepository.findAll();
        }
        return userAdminRepository.findByUsernameContaining(keyword.trim());
    }

    @Transactional
    public void banUser(Integer id) {
        userAdminRepository.banUser(id);
    }

    @Transactional
    public void unbanUser(Integer id) {
        userAdminRepository.unbanUser(id);
    }
}