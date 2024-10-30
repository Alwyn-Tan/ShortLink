package org.alwyn.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.alwyn.shortlink.project.dao.entity.AccessStatsDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface AccessStatsMapper extends BaseMapper<AccessStatsDO> {

    @Insert("INSERT INTO access_stats_0 (full_short_link, gid, date, pv, uv, uip, time_of_the_hour, day_of_the_week, create_time, update_time, del_flag)" +
            "VALUES(#{accessStats.fullShortLink}, #{accessStats.gid}, #{accessStats.date}, #{accessStats.pv}, #{accessStats.uv}, #{accessStats.uip}, #{accessStats.timeOfTheHour}, #{accessStats.dayOfTheWeek}, NOW(), NOW(), 0)" +
            " ON DUPLICATE KEY UPDATE pv = pv + #{accessStats.pv}, uv = uv + #{accessStats.uv}, uip = uip + #{accessStats.uip};")
    void accessStatsInsert(@Param("accessStats") AccessStatsDO accessStatsDO);
}
