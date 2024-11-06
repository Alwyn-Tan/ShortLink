package org.alwyn.shortlink.project.service;

import org.alwyn.shortlink.project.dto.req.AccessStatsReqDTO;
import org.springframework.stereotype.Service;

@Service
public interface AccessStatsService {
    void getLinkAccessStats(AccessStatsReqDTO requestParam);

}
