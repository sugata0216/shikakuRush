package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.User;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ── ユーザー名変更 ────────────────────────────────────
    @Transactional
    public void updateUsername(Integer userId, String username) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw AuthException.loginFailed();
        }

        // 月1回チェック
        if (user.isUsernameChangedThisMonth()) {
            throw AuthException.usernameAlreadyChanged();
        }

        // 重複チェック
        if (userRepository.existsByUsername(username)) {
            throw AuthException.usernameAlreadyExists();
        }

        userRepository.updateUsername(userId, username);
    }

    public User findById(Integer userId) {
        User user = userRepository.findById(userId);
        if (user == null) throw AuthException.loginFailed();
        return user;
    }
}