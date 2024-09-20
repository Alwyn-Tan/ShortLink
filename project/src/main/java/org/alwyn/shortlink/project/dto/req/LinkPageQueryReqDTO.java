package org.alwyn.shortlink.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.alwyn.shortlink.project.dao.entity.LinkDO;

@Data
public class LinkPageQueryReqDTO extends Page<LinkDO> {
    private String gid;
    private String orderTag;
}
