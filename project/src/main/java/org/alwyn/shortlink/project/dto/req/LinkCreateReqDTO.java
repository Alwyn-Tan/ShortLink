package org.alwyn.shortlink.project.dto.req;

import lombok.Data;

@Data
public class LinkCreateReqDTO {
    private String domain;

    private String originLink;

    private String gid;

}
