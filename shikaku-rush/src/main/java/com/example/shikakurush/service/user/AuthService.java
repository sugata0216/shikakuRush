package com.example.shikakurush.service.user;

import com.example.shikakurush.entity.PasswordResetToken;
import com.example.shikakurush.entity.User;
import com.example.shikakurush.exception.AuthException;
import com.example.shikakurush.repository.user.PasswordResetTokenRepository;
import com.example.shikakurush.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${MAIL_USERNAME}")
    private String mailFrom;

    public AuthService(UserRepository userRepository,
                       PasswordResetTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder,
                       JavaMailSender mailSender) {
        this.userRepository  = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender      = mailSender;
    }

    // ログイン
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null || user.isBanned()) {
            throw AuthException.loginFailed();
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw AuthException.loginFailed();
        }

        return user;
    }

    // 登録メール送信
    @Transactional
    public void sendRegistrationEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw AuthException.emailAlreadyExists();
        }

        PasswordResetToken prt = new PasswordResetToken();
        prt.setEmail(email);
        prt.setToken(UUID.randomUUID().toString());
        prt.setExpiresAt(LocalDateTime.now().plusHours(24));
        tokenRepository.save(prt);

        String link = baseUrl + "/user/registration?token=" + prt.getToken();
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(mailFrom);
        mail.setTo(email);
        mail.setSubject("【シカクラッシュ!!!!】ユーザー登録のご案内");
        mail.setText(
                "以下のリンクから登録を完了してください。\n\n" + link +
                        "\n\n※このリンクは24時間有効です。\n" +
                        "心当たりがない場合はこのメールを無視してください。"
        );
        mailSender.send(mail);
    }

    // 登録完了
    @Transactional
    public User registerComplete(String token, String username, String password) {
        PasswordResetToken prt = tokenRepository.findByToken(token);

        if (prt == null)     throw AuthException.tokenInvalid();
        if (prt.isUsed())    throw AuthException.tokenInvalid();
        if (prt.isExpired()) throw AuthException.tokenExpired();

        if (userRepository.existsByEmail(prt.getEmail())) {
            throw AuthException.emailAlreadyExists();
        }
        if (userRepository.existsByUsername(username)) {
            throw AuthException.usernameAlreadyExists();
        }

        User user = new User();
        user.setEmail(prt.getEmail());
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        tokenRepository.markAsUsed(token);

        return userRepository.findByEmail(prt.getEmail());
    }
    // 登録トークン検証
    public PasswordResetToken validateRegistrationToken(String token) {

        PasswordResetToken prt = tokenRepository.findByToken(token);

        if (prt == null || prt.isUsed()) throw AuthException.tokenInvalid();
        if (prt.isExpired())             throw AuthException.tokenExpired();

        return prt;
    }
}