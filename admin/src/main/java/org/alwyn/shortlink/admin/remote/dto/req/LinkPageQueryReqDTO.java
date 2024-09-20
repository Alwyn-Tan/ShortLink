package org.alwyn.shortlink.admin.remote.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

@Data
public class LinkPageQueryReqDTO extends Page {
    private String gid;
    private String orderTag;
}
