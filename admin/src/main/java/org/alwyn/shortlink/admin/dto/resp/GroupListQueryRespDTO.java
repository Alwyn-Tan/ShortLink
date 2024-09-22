package org.alwyn.shortlink.admin.dto.resp;

import lombok.Data;

@Data
public class GroupListQueryRespDTO {

    private String gid;

    private String groupname;

    private String username;

    private Integer linkCount;

    private Integer sortOrder;

}
