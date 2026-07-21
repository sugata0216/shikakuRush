package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.User;
import com.example.shikakurush.mapper.user.UserMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final UserMapper userMapper;

    public UserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    public boolean existsByEmail(String email) {
        return userMapper.countByEmail(email) > 0;
    }

    public boolean existsByUsername(String username) {
        return userMapper.countByUsername(username) > 0;
    }

    public void save(User user) {
        userMapper.insert(user);
    }

    public void updatePassword(Integer id, String password) {
        userMapper.updatePassword(id, password);
    }

    public void updateUsername(Integer id, String username) {
        userMapper.updateUsername(id, username);
    }
}