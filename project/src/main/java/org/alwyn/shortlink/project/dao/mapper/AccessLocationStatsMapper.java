package org.alwyn.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.alwyn.shortlink.project.dao.entity.AccessLocationStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface AccessLocationStatsMapper extends BaseMapper<AccessLocationStatsDO> {

    @Insert("INSERT INTO access_location_stats_0 (full_short_link, gid, date, access_count, province, city, ip_address, create_time, update_time, del_flag)" +
            "VALUES(#{accessLocationStats.fullShortLink}, #{accessLocationStats.gid}, #{accessLocationStats.date}, #{accessLocationStats.accessCount}, #{accessLocationStats.province}, #{accessLocationStats.city}, #{accessLocationStats.ipAddress}, NOW(), NOW(), 0)" +
            "ON DUPLICATE KEY UPDATE access_count = access_count + #{accessLocationStats.accessCount};")
    void insertAccessLocationStats(@Param("accessLocationStats") AccessLocationStatsDO accessLocationStatsDO);

}
