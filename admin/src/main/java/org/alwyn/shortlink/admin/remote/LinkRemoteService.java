package org.alwyn.shortlink.admin.remote;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.alwyn.shortlink.admin.common.result.Result;
import org.alwyn.shortlink.admin.remote.dto.req.LinkPageQueryReqDTO;
import org.alwyn.shortlink.admin.remote.dto.req.RecycleBinPageQueryReqDTO;
import org.alwyn.shortlink.admin.remote.dto.resp.LinkCountQueryRespDTO;
import org.alwyn.shortlink.admin.remote.dto.resp.LinkPageQueryRespDTO;
import org.alwyn.shortlink.admin.dto.resp.LinkReqDTO;

import java.util.HashMap;
import java.util.List;
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

    default Result<List<LinkCountQueryRespDTO>> listLinkCount(List<String> requestParam) {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requestParam", requestParam);
        String resultPageString = HttpUtil.get("http://127.0.0.1:8080/api/short-link/project/link/count", requestMap);
        return JSON.parseObject(resultPageString, new TypeReference<>() {
        });
    }

    /// *** Recycle Bin ***//

    default void addLinkIntoRecycleBin(LinkReqDTO requestParam) {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/project/recycle-bin/add", JSON.toJSONString(requestParam));
    }

    default Result<IPage<LinkPageQueryRespDTO>> queryRecycleBinPage(RecycleBinPageQueryReqDTO requestParam){
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gidList", requestParam.getGidList());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String result = HttpUtil.get("http://127.0.0.1:8001/api/short-link/project/recycle-bin/query", requestMap);
        return JSON.parseObject(result, new TypeReference<>() {});
    }

    default void recoverLinkFromRecycleBin(LinkReqDTO requestParam){
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/recycle-bin/recover", JSON.toJSONString(requestParam));
    }
}
