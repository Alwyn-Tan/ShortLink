package org.alwyn.shortlink.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.common.result.Result;
import org.alwyn.shortlink.project.common.result.Results;
import org.alwyn.shortlink.project.dto.req.LinkCreateReqDTO;
import org.alwyn.shortlink.project.dto.req.LinkPageQueryReqDTO;
import org.alwyn.shortlink.project.dto.req.LinkUpdateReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkCountQueryRespDTO;
import org.alwyn.shortlink.project.dto.resp.LinkCreateRespDTO;
import org.alwyn.shortlink.project.dto.resp.LinkPageQueryRespDTO;
import org.alwyn.shortlink.project.service.LinkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/api/short-link/project/link/count")
    public Result<List<LinkCountQueryRespDTO>> listLinkCount(@RequestParam("requestParam") List<String> requestParam) {
        return Results.success(linkService.listLinkCount(requestParam));
    }

    @PutMapping("/api/short-link/project/link/update")
    public Result<Void> updateLink(@RequestBody LinkUpdateReqDTO requestParam) {
        linkService.updateLink(requestParam);
        return Results.success();
    }

    @GetMapping("/{suffix}")
    public void redirectLink(@PathVariable("suffix") String suffix, ServletRequest request, ServletResponse response) {
        linkService.redirectLink(suffix, request, response);
    }
}
