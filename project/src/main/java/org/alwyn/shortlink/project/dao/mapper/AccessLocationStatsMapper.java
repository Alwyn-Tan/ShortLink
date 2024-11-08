package org.alwyn.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.alwyn.shortlink.project.dao.entity.AccessLocationStatsDO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.TopAccessIpStatsRespDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//city看来是不需要了，只保留省份就可以
//sql查询返回一个
public interface AccessLocationStatsMapper extends BaseMapper<AccessLocationStatsDO> {

    @Insert("INSERT INTO access_location_stats_0 (full_short_link, gid, date, access_count, province, ip_address, create_time, update_time, del_flag)" +
            "VALUES(#{accessLocationStats.fullShortLink}, #{accessLocationStats.gid}, #{accessLocationStats.date}, #{accessLocationStats.accessCount}, #{accessLocationStats.province}, #{accessLocationStats.ipAddress}, NOW(), NOW(), 0)" +
            "ON DUPLICATE KEY UPDATE access_count = access_count + #{accessLocationStats.accessCount};")
    void insertAccessLocationStats(@Param("accessLocationStats") AccessLocationStatsDO accessLocationStatsDO);

    @Select({
            "SELECT province, SUM(access_count) AS access_count",
            "FROM access_location_stats_0",
            "WHERE full_short_link = #{fullShortLink}",
    })
    List<AccessLocationStatsDO> listAccessLocationStats(@Param("fullShortLink") String fullShortLink);

    @Select({
            "SELECT ip_address, SUM(access_count) AS access_count",
            "FROM access_location_stats_0",
            "WHERE full_short_link = #{fullShortLink}",
    })
    List<TopAccessIpStatsRespDTO> listTopAccessIpStats(@Param("fullShortLink") String fullShortLink);
}
