package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import lombok.Data;

@Data
public class AccessLocationStatsRespDTO {
    private Integer count;
    private String province;
    private Double ratio;
}
