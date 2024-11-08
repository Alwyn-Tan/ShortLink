package org.alwyn.shortlink.project.dto.resp.LinkAccessStats;

import lombok.Data;

@Data
public class TopAccessIpStatsRespDTO {
    private Integer ipAccessCount;
    private String ipAddress;
}
