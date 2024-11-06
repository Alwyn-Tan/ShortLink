package org.alwyn.shortlink.project.dto.req;

import lombok.Data;

@Data
public class AccessStatsReqDTO {
    private String fullShortLink;
    private String gid;
    private String startDate;
    private String endDate;
}
