package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DailyAccessStatsRespDTO {
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String date;

    private Integer pv;

    private Integer uv;

    private Integer uip;

}
