package org.alwyn.shortlink.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.dao.entity.AccessLocationStatsDO;
import org.alwyn.shortlink.project.dao.mapper.AccessLocationStatsMapper;
import org.alwyn.shortlink.project.dao.mapper.AccessStatsMapper;
import org.alwyn.shortlink.project.dto.req.AccessStatsReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.*;
import org.alwyn.shortlink.project.service.AccessStatsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AccessStatsServiceImpl implements AccessStatsService {
    private final AccessStatsMapper accessStatsMapper;
    private final AccessLocationStatsMapper accessLocationStatsMapper;

    @Override
    public AccessStatsRespDTO getLinkAccessStats(AccessStatsReqDTO requestParam) {
        List<DailyAccessStatsRespDTO> dailyAccessStatsDOList = accessStatsMapper.listAccessStatsPerDay(requestParam);
        if (CollUtil.isEmpty(dailyAccessStatsDOList)) {
            return null;
        }

        List<Integer> timeOfTheDayAcessStatsList = timeOfTheDayAccessStatsList(requestParam);
        List<Integer> dayOfTheWeekAccessStatsList = dayOfTheWeekAccessStatsList(requestParam);
        List<AccessLocationStatsRespDTO> accessLocationStatsList = getAccessLocationStatsList(requestParam.getFullShortLink());
        List<TopAccessIpStatsRespDTO> topAccessIpStatsList = getTopAccessIpStatsList(requestParam.getFullShortLink());

        return AccessStatsRespDTO.builder()
                .fullShortLink(requestParam.getFullShortLink())
                .gid(requestParam.getGid())
                .dailyAccessStats(dailyAccessStatsDOList)
                .timeOfTheDayAccessStats(timeOfTheDayAcessStatsList)
                .dayOfTheWeekAccessStats(dayOfTheWeekAccessStatsList)
                .accessLocationStats(getAccessLocationStatsList(requestParam.getFullShortLink()))
                .topAccessIpStats(topAccessIpStatsList)
                .build();
    }

    public List<Integer> timeOfTheDayAccessStatsList(AccessStatsReqDTO requestParam) {
        List<Integer> timeOfTheDayAccessStatsList = new ArrayList<>();
        List<TimeOfTheDayAccessStatsRespDTO> timeOfTheDayAccessStatsRespDTOList = accessStatsMapper.listTimeOfTheDayAccessStats(requestParam);
        for (int hour = 0; hour <= 23; hour++) {
            AtomicInteger timeOfTheDay = new AtomicInteger(hour);
            int accessCount = timeOfTheDayAccessStatsRespDTOList.stream().
                    filter(item -> item.getTimeOfTheDay() == timeOfTheDay.get())
                    .findFirst()
                    .map(TimeOfTheDayAccessStatsRespDTO::getPv)
                    .orElse(0);
            timeOfTheDayAccessStatsList.add(accessCount);
        }
        return timeOfTheDayAccessStatsList;
    }

    public List<Integer> dayOfTheWeekAccessStatsList(AccessStatsReqDTO requestParam) {
        List<Integer> dayOfTheWeekAccessStatsList = new ArrayList<>();
        List<DayOfTheWeekAccessStatsRespDTO> dayOfTheWeekAccessStatsRespDTOList = accessStatsMapper.listDayOfTheWeekAccessStats(requestParam);
        for (int day = 0; day <= 6; day++) {
            AtomicInteger dayOfTheWeek = new AtomicInteger(day);
            int accessCount = dayOfTheWeekAccessStatsRespDTOList.stream().
                    filter(item -> item.getDayOfTheWeek() == dayOfTheWeek.get())
                    .findFirst()
                    .map(DayOfTheWeekAccessStatsRespDTO::getPv)
                    .orElse(0);
            dayOfTheWeekAccessStatsList.add(accessCount);
        }
        return dayOfTheWeekAccessStatsList;
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

    private List<TopAccessIpStatsRespDTO> getTopAccessIpStatsList(String fullShortLink) {
        return accessLocationStatsMapper.listTopAccessIpStats(fullShortLink);
    }
}
