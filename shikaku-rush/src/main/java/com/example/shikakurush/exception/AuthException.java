package com.example.shikakurush.exception;

public class AuthException extends RuntimeException {

    private final String code;

    public AuthException(String code, String message) {
        super(message);
        this.code = code;
    }

    public String getCode() { return code; }

    public static AuthException emailAlreadyExists() {
        return new AuthException("EMAIL_TAKEN", "このメールアドレスはすでに登録されています");
    }

    public static AuthException usernameAlreadyExists() {
        return new AuthException("USERNAME_TAKEN", "このユーザーネームはすでに使われています");
    }

    public static AuthException tokenExpired() {
        return new AuthException("TOKEN_EXPIRED", "このリンクは期限切れです。再度メールアドレスを入力してください");
    }

    public static AuthException tokenInvalid() {
        return new AuthException("TOKEN_INVALID", "このリンクは無効です");
    }

    public static AuthException loginFailed() {
        return new AuthException("LOGIN_FAILED", "メールアドレスまたはパスワードが正しくありません");
    }

    public static AuthException emailNotFound() {
        return new AuthException("EMAIL_NOT_FOUND", "このメールアドレスは登録されていません");
    }

    public static AuthException passwordMismatch() {
        return new AuthException("PASSWORD_MISMATCH", "パスワードが一致しません");
    }

    public static AuthException usernameAlreadyChanged() {
        return new AuthException("USERNAME_ALREADY_CHANGED", "ユーザー名は月に1回しか変更できません");
    }

    public static AuthException forbidden() {
        return new AuthException("FORBIDDEN", "選択できない称号です");
    }
}