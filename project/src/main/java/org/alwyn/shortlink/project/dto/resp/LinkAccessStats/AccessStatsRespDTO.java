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
    private Integer pv;
    private Integer uv;
    private Integer uip;
    private List<DailyAccessStatsRespDTO> dailyAccessStats;
    private List<Integer> hourlyAccessStats;
    private List<Integer> weeklyAccessStats;
    private List<AccessLocationStatsRespDTO> accessLocationStats;
    private List<TopAccessIpStatsRespDTO> topAccessIpStats;

}
