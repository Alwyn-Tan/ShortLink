package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import lombok.Data;

@Data
public class TopAccessIpStatsRespDTO {
    private String ipAddress;
    private Integer ipAccessCount;
}
