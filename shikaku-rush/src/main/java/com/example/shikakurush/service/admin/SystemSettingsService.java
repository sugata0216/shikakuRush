package com.example.shikakurush.service.admin;

import com.example.shikakurush.entity.SystemSettings;
import com.example.shikakurush.repository.admin.SystemSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SystemSettingsService {

    private final SystemSettingsRepository systemSettingsRepository;

    // メンテナンスモードの取得
    public boolean isMaintenanceMode() {
        SystemSettings settings = systemSettingsRepository.find();
        return settings != null && Boolean.TRUE.equals(settings.getMaintenanceMode());
    }

    // メンテナンスモードの切り替え
    @Transactional
    public boolean toggleMaintenanceMode() {
        boolean current = isMaintenanceMode();
        systemSettingsRepository.updateMaintenanceMode(!current);
        return !current;
    }
}