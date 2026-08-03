package com.example.shikakurush.service.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class NgWordService {

    @Value("${supabase.storage.url}")
    private String storageUrl;

    @Value("${supabase.storage.service-role-key}")
    private String serviceRoleKey;

    private Set<String> ngWords = Collections.emptySet();

    private final RestTemplate restTemplate = new RestTemplate();

    // アプリ起動時にCSVを読み込む
    @PostConstruct
    public void loadNgWords() {
        ngWords = fetchNgWordsFromStorage();
        System.out.println("禁止ワード読み込み完了: " + ngWords);
    }

    // Supabase StorageからCSVを取得してメモリに展開
    public Set<String> fetchNgWordsFromStorage() {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + serviceRoleKey);
            headers.set("apikey", serviceRoleKey);

            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(
                    storageUrl, HttpMethod.GET, entity, String.class);

            String csv = response.getBody();
            if (csv == null || csv.isBlank()) {
                return Collections.emptySet();
            }

            // 改行・カンマ両方で分割して禁止ワードのセットを生成
            return Arrays.stream(csv.split("[,\n\r]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toSet());

        } catch (Exception e) {
            System.err.println("禁止ワードの読み込みに失敗しました: " + e.getMessage());
            return Collections.emptySet();
        }
    }

    // 管理者がCSVをアップロードした際にメモリ上のリストを即時更新
    public void reload() {
        ngWords = fetchNgWordsFromStorage();
    }

    // ユーザー名に禁止ワードが含まれているか判定
    public boolean containsNgWord(String username) {
        if (username == null || username.isBlank()) return false;
        String lower = username.toLowerCase();
        return ngWords.stream()
                .anyMatch(ng -> lower.contains(ng.toLowerCase()));
    }

    // 現在の禁止ワード一覧を取得（管理者画面確認用）
    public Set<String> getNgWords() {
        return Collections.unmodifiableSet(ngWords);
    }
}