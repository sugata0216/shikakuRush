package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.PasswordResetToken;
import org.apache.ibatis.annotations.*;

@Mapper
public interface PasswordResetTokenMapper {

    @Select("SELECT * FROM password_reset_tokens WHERE token = #{token} LIMIT 1")
    PasswordResetToken findByToken(String token);

    @Insert("INSERT INTO password_reset_tokens (email, token, expires_at, created_at) " +
            "VALUES (#{email}, #{token}, #{expiresAt}, NOW())")
    void insert(PasswordResetToken token);

    @Insert("INSERT INTO password_reset_tokens (user_id, token, expires_at, created_at) " +
            "VALUES (#{userId}, #{token}, #{expiresAt}, NOW())")
    void insertForPasswordReset(PasswordResetToken token);

    @Update("UPDATE password_reset_tokens SET used_at = NOW() WHERE token = #{token}")
    void markAsUsed(String token);
}