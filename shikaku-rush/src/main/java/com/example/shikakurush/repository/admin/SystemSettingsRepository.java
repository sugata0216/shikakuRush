package com.example.shikakurush.repository.admin;

import com.example.shikakurush.entity.SystemSettings;
import com.example.shikakurush.mapper.admin.SystemSettingsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class SystemSettingsRepository {

    private final SystemSettingsMapper systemSettingsMapper;

    public SystemSettings find() {
        return systemSettingsMapper.find();
    }

    public void updateMaintenanceMode(Boolean maintenanceMode) {
        systemSettingsMapper.updateMaintenanceMode(maintenanceMode);
    }
}