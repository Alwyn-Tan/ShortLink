package org.alwyn.shortlink.project.dto.resp;

import lombok.Data;

@Data
public class LinkCountQueryRespDTO {
    private Integer gid;

    private Integer linkCount;
}
