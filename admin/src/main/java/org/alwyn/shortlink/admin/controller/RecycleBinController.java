package org.alwyn.shortlink.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.admin.common.result.Result;
import org.alwyn.shortlink.admin.common.result.Results;
import org.alwyn.shortlink.admin.dto.resp.LinkReqDTO;
import org.alwyn.shortlink.admin.remote.LinkRemoteService;
import org.alwyn.shortlink.admin.remote.dto.req.RecycleBinPageQueryReqDTO;
import org.alwyn.shortlink.admin.remote.dto.resp.LinkPageQueryRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    //private final RecycleBinService recycleBinService;

    LinkRemoteService linkRemoteService = new LinkRemoteService() {
    };

    @PostMapping("/api/short-link/admin/recycle-bin/add")
    public Result<Void>  addLinkIntoRecycleBin(@RequestBody LinkReqDTO requestParam){
        linkRemoteService.addLinkIntoRecycleBin(requestParam);
        return Results.success();
    }

    @GetMapping("/api/short-link/admin/recycle-bin/query")
    public Result<IPage<LinkPageQueryRespDTO>> queryRecycleBinPage(RecycleBinPageQueryReqDTO requestParam) {
        return linkRemoteService.queryRecycleBinPage(requestParam);
    }
}
