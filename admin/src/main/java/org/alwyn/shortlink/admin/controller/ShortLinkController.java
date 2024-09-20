package org.alwyn.shortlink.admin.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.alwyn.shortlink.admin.common.result.Result;
import org.alwyn.shortlink.admin.remote.LinkRemoteService;
import org.alwyn.shortlink.admin.remote.dto.req.LinkPageQueryReqDTO;
import org.alwyn.shortlink.admin.remote.dto.resp.LinkPageQueryRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortLinkController {
    LinkRemoteService service = new LinkRemoteService() {
    };

    @GetMapping("/api/short-link/admin/link/query")
    public Result<IPage<LinkPageQueryRespDTO>> queryShortLinkPage(LinkPageQueryReqDTO requestParam) {
        return service.queryShortLinkPage(requestParam);
    }


}