package org.alwyn.shortlink.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.dao.entity.AccessLocationStatsDO;
import org.alwyn.shortlink.project.dao.entity.AccessStatsDO;
import org.alwyn.shortlink.project.dao.mapper.AccessLocationStatsMapper;
import org.alwyn.shortlink.project.dao.mapper.AccessStatsMapper;
import org.alwyn.shortlink.project.dto.req.AccessStatsReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkAccessStats.*;
import org.alwyn.shortlink.project.service.AccessStatsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class AccessStatsServiceImpl implements AccessStatsService {
    private final AccessStatsMapper accessStatsMapper;
    private final AccessLocationStatsMapper accessLocationStatsMapper;

    @Override
    public AccessStatsRespDTO getLinkAccessStats(AccessStatsReqDTO requestParam) {
        List<AccessStatsDO> accessStatsDOList = accessStatsMapper.listAccessStatsDO(requestParam);
        if (CollUtil.isEmpty(accessStatsDOList)) {
            return null;
        }
        int totalPv = 0, totalUv = 0, totalUip = 0;
        for (AccessStatsDO item : accessStatsDOList) {
            totalPv += item.getPv();
            totalUv += item.getUv();
            totalUip += item.getUip();
        }
        List<DailyAccessStatsRespDTO> dailyAccessStatsList = new ArrayList<>();
        List<String> dateList = DateUtil.rangeToList(DateUtil.parse(requestParam.getStartDate()), DateUtil.parse(requestParam.getEndDate()), DateField.DAY_OF_MONTH).stream()
                .map(DateUtil::formatDate)
                .toList();
        dateList.forEach(each -> accessStatsDOList.stream()
                .filter(item -> Objects.equals(each, DateUtil.formatDate(item.getDate())))
                .findFirst()
                .ifPresentOrElse(item -> {
                    DailyAccessStatsRespDTO respDTO = DailyAccessStatsRespDTO.builder()
                            .date(each)
                            .pv(item.getPv())
                            .uv(item.getUv())
                            .uip(item.getUip())
                            .build();
                    dailyAccessStatsList.add(respDTO);
                }, () -> {
                    DailyAccessStatsRespDTO respDTO = DailyAccessStatsRespDTO.builder()
                            .date(each)
                            .pv(0)
                            .uv(0)
                            .uip(0)
                            .build();
                    dailyAccessStatsList.add(respDTO);
                }));

        List<Integer> timeOfTheDayAcessStatsList = timeOfTheDayAccessStatsList(requestParam);
        List<Integer> dayOfTheWeekAccessStatsList = dayOfTheWeekAccessStatsList(requestParam);
        List<AccessLocationStatsRespDTO> accessLocationStatsList = getAccessLocationStatsList(requestParam.getFullShortLink());
        List<TopAccessIpStatsRespDTO> topAccessIpStatsList = getTopAccessIpStatsList(requestParam.getFullShortLink());

        return AccessStatsRespDTO.builder()
                .fullShortLink(requestParam.getFullShortLink())
                .gid(requestParam.getGid())
                .totalPv(totalPv)
                .totalUv(totalUv)
                .totalUip(totalUip)
                .dailyAccessStats(dailyAccessStatsList)
                .timeOfTheDayAccessStats(timeOfTheDayAcessStatsList)
                .dayOfTheWeekAccessStats(dayOfTheWeekAccessStatsList)
                .accessLocationStats(accessLocationStatsList)
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
