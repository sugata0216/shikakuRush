package com.example.shikakurush.service.admin;

import com.example.shikakurush.entity.Admin;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.repository.admin.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAuthService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Admin login(String password) {
        Admin admin = adminRepository.find();

        if (admin == null) {
            throw AuthException.loginFailed();
        }
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            throw AuthException.loginFailed();
        }
        return admin;
    }
}