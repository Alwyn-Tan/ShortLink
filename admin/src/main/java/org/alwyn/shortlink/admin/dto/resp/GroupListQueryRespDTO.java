package org.alwyn.shortlink.admin.dto.resp;

import lombok.Data;

@Data
public class GroupListQueryRespDTO {

    private String gid;

    private String groupName;

    private String username;

    private Integer linkCount;

    private Integer sortOrder;

}
