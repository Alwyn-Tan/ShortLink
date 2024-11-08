package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import lombok.Data;

@Data
public class DayOfTheWeekAccessStatsRespDTO {
    private Integer dayOfTheWeek;
    private Integer pv;
}
