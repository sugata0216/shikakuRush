package com.example.shikakurush.mapper.user;

import com.example.shikakurush.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE email = #{email} AND deleted_flag = false LIMIT 1")
    User findByEmail(String email);

    @Select("SELECT * FROM users WHERE id = #{id} AND deleted_flag = false LIMIT 1")
    User findById(Integer id);

    @Select("SELECT COUNT(*) FROM users WHERE email = #{email}")
    int countByEmail(String email);

    @Select("SELECT COUNT(*) FROM users WHERE username = #{username}")
    int countByUsername(String username);

    @Insert("INSERT INTO users (email, password, username, created_at, updated_at) " +
            "VALUES (#{email}, #{password}, #{username}, NOW(), NOW())")
    void insert(User user);

    @Update("UPDATE users SET password = #{password}, updated_at = NOW() WHERE id = #{id}")
    void updatePassword(@Param("id") Integer id, @Param("password") String password);

    @Update("UPDATE users SET username = #{username}, username_changed_this_month = true, updated_at = NOW() WHERE id = #{id}")
    void updateUsername(@Param("id") Integer id, @Param("username") String username);

    @Update("UPDATE users SET deleted_flag = true, updated_at = NOW() WHERE id = #{id}")
    void deleteUser(@Param("id") Integer id);

    // 管理者用：全ユーザー取得（削除済み除く）
    @Select("SELECT * FROM users WHERE deleted_flag = false ORDER BY id ASC")
    List<User> findAll();

    // 管理者用：ユーザー名で検索
    @Select("SELECT * FROM users WHERE deleted_flag = false AND username LIKE #{keyword} ORDER BY id ASC")
    List<User> findByUsernameContaining(@Param("keyword") String keyword);

    // 管理者用：BANする
    @Update("UPDATE users SET banned = true, updated_at = NOW() WHERE id = #{id}")
    void banUser(@Param("id") Integer id);

    // 管理者用：BAN解除する
    @Update("UPDATE users SET banned = false, updated_at = NOW() WHERE id = #{id}")
    void unbanUser(@Param("id") Integer id);
}