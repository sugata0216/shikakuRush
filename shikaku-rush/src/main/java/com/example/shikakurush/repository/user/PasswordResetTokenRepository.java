package com.example.shikakurush.repository.user;

import com.example.shikakurush.entity.PasswordResetToken;
import com.example.shikakurush.mapper.user.PasswordResetTokenMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PasswordResetTokenRepository {

    private final PasswordResetTokenMapper tokenMapper;

    public PasswordResetTokenRepository(PasswordResetTokenMapper tokenMapper) {
        this.tokenMapper = tokenMapper;
    }

    public PasswordResetToken findByToken(String token) {
        return tokenMapper.findByToken(token);
    }

    public void save(PasswordResetToken token) {
        tokenMapper.insert(token);
    }

    public void saveForPasswordReset(PasswordResetToken token) {
        tokenMapper.insertForPasswordReset(token);
    }

    public void markAsUsed(String token) {
        tokenMapper.markAsUsed(token);
    }
}