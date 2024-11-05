package org.alwyn.shortlink.project.controller;

import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.common.result.Result;
import org.alwyn.shortlink.project.common.result.Results;
import org.alwyn.shortlink.project.dto.req.LinkAccessStatsReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.AccessStatsRespDTO;
import org.alwyn.shortlink.project.service.AccessStatsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccessStatsController {
    private final AccessStatsService accessStatsService;

    @GetMapping("api/short-link/project/stats/link")
    public Result<AccessStatsRespDTO> getLinkAccessStats(LinkAccessStatsReqDTO requestParam) {
        return Results.success(accessStatsService.getLinkAccessStats(requestParam));
    }
}
