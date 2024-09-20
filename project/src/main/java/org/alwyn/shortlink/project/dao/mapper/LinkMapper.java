package org.alwyn.shortlink.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.alwyn.shortlink.project.dao.entity.LinkDO;
import org.alwyn.shortlink.project.dto.req.LinkPageQueryReqDTO;

public interface LinkMapper extends BaseMapper<LinkDO> {
    IPage<LinkDO> pageLink(LinkPageQueryReqDTO requestParam);
}
