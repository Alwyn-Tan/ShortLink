package org.alwyn.shortlink.project.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.common.result.Result;
import org.alwyn.shortlink.project.common.result.Results;
import org.alwyn.shortlink.project.dto.resp.LinkPageQueryRespDTO;
import org.alwyn.shortlink.project.service.RecycleBinService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecycleBinController {

    private final RecycleBinService recycleBinService;

    @PostMapping("api/short-link/project/recycle-bin/add")
    public Result<Void> addLinkIntoRecycleBin() {
        recycleBinService.addLinkIntoRecycleBin();
        return Results.success();
    }

    @GetMapping("api/short-link/project/recycle-bin/query")
    public Result<IPage<LinkPageQueryRespDTO>> queryRecycleBinPage() {
        return Results.success(recycleBinService.queryRecycleBinPage());
    }

    @PostMapping("api/short-link/project/recycle-bin/delete")
    public Result<Void> deleteLinkInRecycleBin() {
        recycleBinService.deleteLinkInRecycleBin();
        return Results.success();
    }

    @PostMapping("api/short-link/project/recycle-bin/recover")
    public Result<Void> recoverLinkFromRecycleBin() {
        recycleBinService.recoverLinkFromRecycleBin();
        return Results.success();
    }
}
