package org.alwyn.shortlink.admin.remote;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.alwyn.shortlink.admin.common.result.Result;
import org.alwyn.shortlink.admin.remote.dto.req.LinkPageQueryReqDTO;
import org.alwyn.shortlink.admin.remote.dto.resp.LinkPageQueryRespDTO;

import java.util.HashMap;
import java.util.Map;

public interface LinkRemoteService {
    default Result<IPage<LinkPageQueryRespDTO>> queryShortLinkPage(LinkPageQueryReqDTO requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultPageString = HttpUtil.get("http://127.0.0.1:8080/api/short-link/project/link/query", requestMap);
        return JSON.parseObject(resultPageString, new TypeReference<>() {
        });
    }
}
