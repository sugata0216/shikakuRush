package com.example.shikakurush.mapper.admin;

import com.example.shikakurush.entity.SystemSettings;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SystemSettingsMapper {

    @Select("SELECT * FROM system_settings WHERE id = 1")
    SystemSettings find();

    @Update("UPDATE system_settings SET maintenance_mode = #{maintenanceMode} WHERE id = 1")
    void updateMaintenanceMode(Boolean maintenanceMode);
}