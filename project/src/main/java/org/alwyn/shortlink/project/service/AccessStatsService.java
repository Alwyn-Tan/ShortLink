package org.alwyn.shortlink.project.service;

import org.alwyn.shortlink.project.dto.req.AccessStatsReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.AccessStatsRespDTO;
import org.springframework.stereotype.Service;

@Service
public interface AccessStatsService {
    AccessStatsRespDTO getLinkAccessStats(AccessStatsReqDTO requestParam);

}
