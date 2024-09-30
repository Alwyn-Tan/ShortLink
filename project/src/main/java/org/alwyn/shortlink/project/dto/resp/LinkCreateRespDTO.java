package org.alwyn.shortlink.project.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LinkCreateRespDTO {

    private String gid;

    private String originLink;

    private String suffix;

    private String fullShortLink;
}
