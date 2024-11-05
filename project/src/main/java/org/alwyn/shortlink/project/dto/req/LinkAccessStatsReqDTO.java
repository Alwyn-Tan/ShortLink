package org.alwyn.shortlink.project.dto.req;

import lombok.Data;

@Data
public class LinkAccessStatsReqDTO {
    private String fullShortLink;
    private String gid;
    private String date;
}
