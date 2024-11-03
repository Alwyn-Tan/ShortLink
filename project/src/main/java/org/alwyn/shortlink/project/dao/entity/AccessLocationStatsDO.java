package org.alwyn.shortlink.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("access_location_stats_0")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessLocationStatsDO extends BaseDO {

    private Long id;

    private String fullShortLink;

    private String gid;

    private Date date;

    private Integer accessCount;

    private String province;

    private String city;

}
