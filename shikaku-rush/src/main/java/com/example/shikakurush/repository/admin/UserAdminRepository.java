package com.example.shikakurush.repository.admin;

import com.example.shikakurush.entity.User;
import com.example.shikakurush.mapper.user.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserAdminRepository {

    private final UserMapper userMapper;

    public UserAdminRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public List<User> findByUsernameContaining(String keyword) {
        return userMapper.findByUsernameContaining("%" + keyword + "%");
    }

    public void banUser(Integer id) {
        userMapper.banUser(id);
    }

    public void unbanUser(Integer id) {
        userMapper.unbanUser(id);
    }
}