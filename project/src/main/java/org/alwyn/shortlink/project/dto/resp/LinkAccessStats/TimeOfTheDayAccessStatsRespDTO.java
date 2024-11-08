package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import lombok.Data;

@Data
public class TimeOfTheDayAccessStatsRespDTO {
    private Integer timeOfTheDay;
    private Integer pv;
}
