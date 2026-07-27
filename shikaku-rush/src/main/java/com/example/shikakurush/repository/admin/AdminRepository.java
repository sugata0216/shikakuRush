package com.example.shikakurush.repository.admin;

import com.example.shikakurush.entity.Admin;
import com.example.shikakurush.mapper.admin.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AdminRepository {

    private final AdminMapper adminMapper;

    public Admin find() {
        return adminMapper.find();
    }
}