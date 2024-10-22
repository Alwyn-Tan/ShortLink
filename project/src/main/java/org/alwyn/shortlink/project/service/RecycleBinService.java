package org.alwyn.shortlink.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.alwyn.shortlink.project.dao.entity.LinkDO;
import org.alwyn.shortlink.project.dto.req.LinkPageQueryReqDTO;
import org.alwyn.shortlink.project.dto.req.LinkReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkPageQueryRespDTO;
import org.springframework.stereotype.Service;

@Service
public interface RecycleBinService extends IService<LinkDO> {

    void addLinkIntoRecycleBin(LinkReqDTO requestParam);

    IPage<LinkPageQueryRespDTO> queryRecycleBinPage(LinkPageQueryReqDTO requestParam);

    void recoverLinkFromRecycleBin(LinkReqDTO requestParam);

    void deleteLinkInRecycleBin(LinkReqDTO requestParam);
}
