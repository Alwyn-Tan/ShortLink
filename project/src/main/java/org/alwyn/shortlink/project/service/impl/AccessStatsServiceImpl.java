package org.alwyn.shortlink.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.dao.entity.AccessLocationStatsDO;
import org.alwyn.shortlink.project.dao.entity.AccessStatsDO;
import org.alwyn.shortlink.project.dao.mapper.AccessLocationStatsMapper;
import org.alwyn.shortlink.project.dao.mapper.AccessStatsMapper;
import org.alwyn.shortlink.project.dto.req.AccessStatsReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.AccessLocationStatsRespDTO;
import org.alwyn.shortlink.project.service.AccessStatsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessStatsServiceImpl implements AccessStatsService {
    private final AccessStatsMapper accessStatsMapper;
    private final AccessLocationStatsMapper accessLocationStatsMapper;

    @Override
    public void getLinkAccessStats(AccessStatsReqDTO requestParam) {
        List<AccessStatsDO> dailyAccessStatsDOList = accessStatsMapper.listAccessStatsPerDay(requestParam);
        if (CollUtil.isEmpty(dailyAccessStatsDOList)) {
            return;
        }
        List<AccessLocationStatsRespDTO> list = getAccessLocationStatsList(requestParam.getFullShortLink());

//        return AccessStatsRespDTO.builder()
//                .fullShortLink(requestParam.getFullShortLink())
//                .gid(requestParam.getGid())
//                .pv(BasicAccessStats.getPv())
//                .uv(BasicAccessStats.getUv())
//                .uip(BasicAccessStats.getUip())
//                .dailyAccessStats(dailyAccessStatsList)
//                .hourlyAccessStats(houlyAccessStatsList)
//                .weeklyAccessStats(weeklyAccessStatsList)
//                .accessLocationStats(getAccessLocationStatsList(requestParam.getFullShortLink()))
//                .topAccessIpStats(topAccessIpStatsList)
//                .build();
    }

    private List<AccessLocationStatsRespDTO> getAccessLocationStatsList(String fullShortLink) {
        List<AccessLocationStatsRespDTO> respDTOList = new ArrayList<>();
        List<AccessLocationStatsDO> accessLocationStatsDOList = accessLocationStatsMapper.listAccessLocationStats(fullShortLink);
        double locationSum = accessLocationStatsDOList.stream().mapToInt(AccessLocationStatsDO::getAccessCount).sum();
        for (AccessLocationStatsDO accessLocationStatsDO : accessLocationStatsDOList) {
            respDTOList.add(AccessLocationStatsRespDTO.builder()
                    .province(accessLocationStatsDO.getProvince())
                    .ratio(locationSum == 0 ? 0 : accessLocationStatsDO.getAccessCount() * 100.0 / locationSum)
                    .count(accessLocationStatsDO.getAccessCount())
                    .build());
        }
        return respDTOList;
    }
}
