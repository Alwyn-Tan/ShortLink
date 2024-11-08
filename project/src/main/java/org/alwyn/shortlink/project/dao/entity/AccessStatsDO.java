package org.alwyn.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("access_stats_0")
public class AccessStatsDO extends BaseDO {
    private Long id;

    private String fullShortLink;

    private String gid;

    private Date date;

    private Integer pv;

    private Integer uv;

    private Integer uip;

    private Integer timeOfTheDay;

    private Integer dayOfTheWeek;

}
