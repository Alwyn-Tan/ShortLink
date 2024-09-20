package org.alwyn.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.common.result.Result;
import org.alwyn.shortlink.project.common.result.Results;
import org.alwyn.shortlink.project.dto.req.LinkCreateReqDTO;
import org.alwyn.shortlink.project.dto.req.LinkPageQueryReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkCreateRespDTO;
import org.alwyn.shortlink.project.dto.resp.LinkPageQueryRespDTO;
import org.alwyn.shortlink.project.service.LinkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ShortLinkController {
    private final LinkService linkService;

    @PostMapping("/api/short-link/project/link/creation")
    public Result<LinkCreateRespDTO> createShortLink(@RequestBody LinkCreateReqDTO requestParam) {
        return Results.success(linkService.createShortLink(requestParam));
    }

    @GetMapping("/api/short-link/project/link/query")
    public Result<IPage<LinkPageQueryRespDTO>> queryShortLinkPage(LinkPageQueryReqDTO requestParam) {
        return Results.success(linkService.queryShortLinkPage(requestParam));
    }

}
