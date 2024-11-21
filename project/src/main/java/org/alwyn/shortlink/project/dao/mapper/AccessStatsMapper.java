package org.alwyn.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.alwyn.shortlink.project.dao.entity.AccessStatsDO;
import org.alwyn.shortlink.project.dto.req.AccessStatsReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.DailyAccessStatsRespDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.DayOfTheWeekAccessStatsRespDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.TimeOfTheDayAccessStatsRespDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface AccessStatsMapper extends BaseMapper<AccessStatsDO> {

    @Insert("INSERT INTO access_stats_0 (full_short_link, gid, date, pv, uv, uip, time_of_the_day, day_of_the_week, create_time, update_time, del_flag)" +
            "VALUES(#{accessStats.fullShortLink}, #{accessStats.gid}, #{accessStats.date}, #{accessStats.pv}, #{accessStats.uv}, #{accessStats.uip}, #{accessStats.timeOfTheDay}, #{accessStats.dayOfTheWeek}, NOW(), NOW(), 0)" +
            " ON DUPLICATE KEY UPDATE pv = pv + #{accessStats.pv}, uv = uv + #{accessStats.uv}, uip = uip + #{accessStats.uip};")
    void accessStatsInsert(@Param("accessStats") AccessStatsDO accessStatsDO);

    @Select({
            "SELECT date, SUM(pv) as pv, SUM(uv) as uv, SUM(uip) as uip",
            "FROM access_stats_0",
            "WHERE full_short_link = #{requestParam.fullShortLink}",
            "AND gid = #{requestParam.gid}",
            "AND date BETWEEN #{requestParam.startDate} AND #{requestParam.endDate}",
            "GROUP BY full_short_link, gid, date"
    })
    List<AccessStatsDO> listAccessStatsDO(@Param("requestParam") AccessStatsReqDTO requestParam);

    @Select({
            "SELECT date, SUM(pv) as pv, SUM(uv) as uv, SUM(uip) as uip",
            "FROM access_stats_0",
            "WHERE full_short_link = #{requestParam.fullShortLink}",
            "AND gid = #{requestParam.gid}",
            "AND date BETWEEN #{requestParam.startDate} AND #{requestParam.endDate}",
            "GROUP BY full_short_link, gid, date"
    })
    List<DailyAccessStatsRespDTO> listAccessStatsPerDay(@Param("requestParam") AccessStatsReqDTO requestParam);

    @Select({
            "SELECT time_of_the_day, SUM(pv) AS pv",
            "FROM access_stats_0",
            "WHERE full_short_link = #{requestParam.fullShortLink}",
            "AND date BETWEEN #{requestParam.startDate} AND #{requestParam.endDate}",
            "GROUP BY full_short_link, gid, time_of_the_day",
    })
    List<TimeOfTheDayAccessStatsRespDTO> listTimeOfTheDayAccessStats(@Param("requestParam") AccessStatsReqDTO requestParam);

    @Select({
            "SELECT day_of_the_week, SUM(pv) AS pv",
            "FROM access_stats_0",
            "WHERE full_short_link = #{requestParam.fullShortLink}",
            "AND date BETWEEN #{requestParam.startDate} AND #{requestParam.endDate}",
            "GROUP BY full_short_link, gid, day_of_the_week",
    })
    List<DayOfTheWeekAccessStatsRespDTO> listDayOfTheWeekAccessStats(@Param("requestParam") AccessStatsReqDTO requestParam);

}

