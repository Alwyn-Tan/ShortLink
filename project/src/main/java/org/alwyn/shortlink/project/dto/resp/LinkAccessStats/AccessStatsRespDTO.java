package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessStatsRespDTO {
    private String fullShortLink;
    private String gid;
    private Integer totalPv;
    private Integer totalUv;
    private Integer totalUip;
    private List<DailyAccessStatsRespDTO> dailyAccessStats;
    private List<Integer> timeOfTheDayAccessStats;
    private List<Integer> dayOfTheWeekAccessStats;
    private List<AccessLocationStatsRespDTO> accessLocationStats;
    private List<TopAccessIpStatsRespDTO> topAccessIpStats;

}
