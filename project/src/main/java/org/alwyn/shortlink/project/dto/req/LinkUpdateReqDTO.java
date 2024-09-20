package org.alwyn.shortlink.project.dto.req;

import lombok.Data;

@Data
public class LinkUpdateReqDTO {
    private String originLink;

    private String fullShortLink;

    private String gid;


}
