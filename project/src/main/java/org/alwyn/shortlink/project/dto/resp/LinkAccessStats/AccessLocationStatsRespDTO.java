package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessLocationStatsRespDTO {
    private Integer count;
    private String province;
    private Double ratio;
}
