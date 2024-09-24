package org.alwyn.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.common.exception.ServiceException;
import org.alwyn.shortlink.project.common.util.HashUtil;
import org.alwyn.shortlink.project.dao.entity.LinkDO;
import org.alwyn.shortlink.project.dao.mapper.LinkMapper;
import org.alwyn.shortlink.project.dto.req.LinkCreateReqDTO;
import org.alwyn.shortlink.project.dto.req.LinkPageQueryReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkCountQueryRespDTO;
import org.alwyn.shortlink.project.dto.resp.LinkCreateRespDTO;
import org.alwyn.shortlink.project.dto.resp.LinkPageQueryRespDTO;
import org.alwyn.shortlink.project.service.LinkService;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.alwyn.shortlink.project.common.error.ErrorResponse.SERVICE_SYSTEM_TIMEOUT;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {

    private final RBloomFilter<String> shortLinkBloomFilter;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LinkCreateRespDTO createShortLink(LinkCreateReqDTO requestParam) {
        String linkSuffix = generateLinkSuffix(requestParam);
        String fullShortLink = requestParam.getDomain() + "/" + linkSuffix;
        LinkDO linkDO = LinkDO.builder()
                .domain(requestParam.getDomain())
                .shortLink(linkSuffix)
                .originLink(requestParam.getOriginLink())
                .fullShortLink(fullShortLink)
                .gid(requestParam.getGid())
                .enableStatus(1)
                .build();
        try {
            baseMapper.insert(linkDO);
        } catch (DuplicateKeyException ex) {
            throw new ServiceException("Shortlink Exists");
        }
        return LinkCreateRespDTO.builder()
                .fullShortLink(requestParam.getDomain() + linkSuffix)
                .gid(linkDO.getGid())
                .originLink(requestParam.getOriginLink())
                .shortLink(linkSuffix)
                .build();
    }

    @Override
    public IPage<LinkPageQueryRespDTO> queryShortLinkPage(LinkPageQueryReqDTO requestParam) {
        LambdaQueryWrapper<LinkDO> queryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getEnableStatus, 1)
                .eq(LinkDO::getDelFlag, 0);
        IPage<LinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> BeanUtil.toBean(each, LinkPageQueryRespDTO.class));
    }

    @Override
    public List<LinkCountQueryRespDTO> listLinkCount(List<String> requestParam) {
        QueryWrapper<LinkDO> queryWrapper = Wrappers.query(new LinkDO())
                .select("gid as gid, count(*) as linkCount")
                .in("gid", requestParam)
                .eq("enable_status", 1)
                .groupBy("gid");
        List<Map<String, Object>> LinkDOList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(LinkDOList, LinkCountQueryRespDTO.class);
    }

    private String generateLinkSuffix(LinkCreateReqDTO requestParam) {
        int maximumTryLimit = 10;
        String linkSuffix;
        while (maximumTryLimit > 0) {
            String originLink = requestParam.getOriginLink();
            originLink += UUID.randomUUID().toString();
            linkSuffix = HashUtil.hashToBase62(originLink);
            String fullShortLink = requestParam.getDomain() + "/" + linkSuffix;
            if (!shortLinkBloomFilter.contains(fullShortLink)) {
                return linkSuffix;
            }
            maximumTryLimit--;
        }
        throw new ServiceException(SERVICE_SYSTEM_TIMEOUT);
    }
}
