package org.alwyn.shortlink.project.controller;

import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.common.result.Result;
import org.alwyn.shortlink.project.common.result.Results;
import org.alwyn.shortlink.project.dto.req.AccessStatsReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.AccessStatsRespDTO;
import org.alwyn.shortlink.project.service.AccessStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AccessStatsController {
    private final AccessStatsService accessStatsService;

    @GetMapping("/api/short-link/project/stats/link")
    public Result<AccessStatsRespDTO> getLinkAccessStats(AccessStatsReqDTO requestParam) {
        return Results.success(accessStatsService.getLinkAccessStats(requestParam));
    }
}
