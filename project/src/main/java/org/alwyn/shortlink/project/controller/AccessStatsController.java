package org.alwyn.shortlink.project.controller;

import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.dto.req.AccessStatsReqDTO;
import org.alwyn.shortlink.project.service.AccessStatsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AccessStatsController {
    private final AccessStatsService accessStatsService;

    @GetMapping("api/short-link/project/stats/link")
    public void getLinkAccessStats(AccessStatsReqDTO requestParam) {
        accessStatsService.getLinkAccessStats(requestParam);
    }
}
