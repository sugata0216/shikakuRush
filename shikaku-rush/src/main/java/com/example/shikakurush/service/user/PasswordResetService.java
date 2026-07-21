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
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    @Value("${MAIL_USERNAME}")
    private String mailFrom;

    public PasswordResetService(UserRepository userRepository,
                                PasswordResetTokenRepository tokenRepository,
                                PasswordEncoder passwordEncoder,
                                JavaMailSender mailSender) {
        this.userRepository  = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender      = mailSender;
    }

    // ── パスワードリセットメール送信 ──────────────────────
    @Transactional
    public void sendPasswordResetEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw AuthException.emailNotFound();
        }

        PasswordResetToken prt = new PasswordResetToken();
        prt.setUserId(user.getId());
        prt.setToken(UUID.randomUUID().toString());
        prt.setExpiresAt(LocalDateTime.now().plusHours(1)); // 有効期限1時間
        tokenRepository.saveForPasswordReset(prt);

        String link = baseUrl + "/user/password-reset?token=" + prt.getToken();
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(mailFrom);
        mail.setTo(email);
        mail.setSubject("【シカクラッシュ!!!!】パスワードリセットのご案内");
        mail.setText(
                "以下のリンクからパスワードをリセットしてください。\n\n" + link +
                        "\n\n※このリンクは1時間有効です。\n" +
                        "心当たりがない場合はこのメールを無視してください。"
        );
        mailSender.send(mail);
    }

    // ── パスワードリセット完了 ────────────────────────────
    @Transactional
    public void resetPassword(String token, String password, String passwordConfirm) {
        if (!password.equals(passwordConfirm)) {
            throw AuthException.passwordMismatch();
        }

        PasswordResetToken prt = tokenRepository.findByToken(token);
        if (prt == null)     throw AuthException.tokenInvalid();
        if (prt.isUsed())    throw AuthException.tokenInvalid();
        if (prt.isExpired()) throw AuthException.tokenExpired();

        userRepository.updatePassword(prt.getUserId(), passwordEncoder.encode(password));
        tokenRepository.markAsUsed(token);
    }
}