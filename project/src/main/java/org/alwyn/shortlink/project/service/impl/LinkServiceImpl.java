package org.alwyn.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import groovy.util.logging.Slf4j;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.alwyn.shortlink.project.common.enums.ValidDateTypeEnum;
import org.alwyn.shortlink.project.common.exception.ServiceException;
import org.alwyn.shortlink.project.common.util.HashUtil;
import org.alwyn.shortlink.project.dao.entity.LinkDO;
import org.alwyn.shortlink.project.dao.entity.LinkRouteDO;
import org.alwyn.shortlink.project.dao.mapper.LinkMapper;
import org.alwyn.shortlink.project.dao.mapper.LinkRouteMapper;
import org.alwyn.shortlink.project.dto.req.LinkCreateReqDTO;
import org.alwyn.shortlink.project.dto.req.LinkPageQueryReqDTO;
import org.alwyn.shortlink.project.dto.req.LinkUpdateReqDTO;
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
import java.util.Objects;
import java.util.UUID;

import static org.alwyn.shortlink.project.common.error.ErrorResponse.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LinkServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements LinkService {

    private final RBloomFilter<String> shortLinkBloomFilter;
    private final LinkRouteMapper linkRouteMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public LinkCreateRespDTO createShortLink(LinkCreateReqDTO requestParam) {
        String suffix = generateLinkSuffix(requestParam);
        String fullShortLink = "https://" + requestParam.getDomain() + "/" + suffix;

        LinkDO linkDO = LinkDO.builder()
                .domain(requestParam.getDomain())
                .suffix(suffix)
                .originLink(requestParam.getOriginLink())
                .fullShortLink(fullShortLink)
                .gid(requestParam.getGid())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .enableStatus(1)
                .build();

        LinkRouteDO linkRouteDO = LinkRouteDO.builder()
                .fullShortLink(fullShortLink)
                .gid(requestParam.getGid())
                .build();
        try {
            baseMapper.insert(linkDO);
            linkRouteMapper.insert(linkRouteDO);
        } catch (DuplicateKeyException ex) {
            throw new ServiceException(LINK_EXISTS_ERROR);
        }
        shortLinkBloomFilter.add(fullShortLink);
        return LinkCreateRespDTO.builder()
                .fullShortLink(fullShortLink)
                .gid(linkDO.getGid())
                .originLink(linkDO.getOriginLink())
                .suffix(suffix)
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


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateLink(LinkUpdateReqDTO requestParam) {
        LambdaQueryWrapper<LinkDO> queryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortLink, requestParam.getFullShortLink())
                .eq(LinkDO::getEnableStatus, 1)
                .eq(LinkDO::getDelFlag, 0);
        LinkDO selectedLinkDO = baseMapper.selectOne(queryWrapper);

        if (selectedLinkDO == null) {
            throw new ServiceException(LINK_NOT_EXIST_ERROR);
        }

        LinkDO linkDOToBeUpdated = LinkDO.builder()
                .domain(selectedLinkDO.getDomain())
                .suffix(selectedLinkDO.getSuffix())
                .originLink(requestParam.getOriginLink())
                .gid(selectedLinkDO.getGid())
                .validDate(selectedLinkDO.getValidDate())
                .validDateType(selectedLinkDO.getValidDateType())
                .build();

        LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortLink, selectedLinkDO.getFullShortLink())
                .eq(LinkDO::getEnableStatus, 1)
                .eq(LinkDO::getDelFlag, 0)
                .set(Objects.equals(requestParam.getValidDateType(), ValidDateTypeEnum.PERMANENT.getType()), LinkDO::getValidDate, null);

        baseMapper.update(linkDOToBeUpdated, updateWrapper);
    }

    @SneakyThrows
    @Override
    public void redirectLink(String suffix, ServletRequest request, ServletResponse response) {
        String serverName = request.getServerName();
        String fullShortLink = "https://" + serverName + "/" + suffix;
        LambdaQueryWrapper<LinkRouteDO> linkRouteDOLambdaQueryWrapper = Wrappers.lambdaQuery(LinkRouteDO.class)
                .eq(LinkRouteDO::getFullShortLink, fullShortLink);
        LinkRouteDO linkRouteDO = linkRouteMapper.selectOne(linkRouteDOLambdaQueryWrapper);
        if (linkRouteDO == null) {
            return;
        }

        LambdaQueryWrapper<LinkDO> linkDOLambdaQueryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                .eq(LinkDO::getGid, linkRouteDO.getGid())
                .eq(LinkDO::getFullShortLink, fullShortLink)
                .eq(LinkDO::getEnableStatus, 1)
                .eq(LinkDO::getDelFlag, 0);
        LinkDO linkDO = baseMapper.selectOne(linkDOLambdaQueryWrapper);
        if (linkDO != null) {
            ((HttpServletResponse) response).sendRedirect(linkDO.getOriginLink());
        }
    }

    private String generateLinkSuffix(LinkCreateReqDTO requestParam) {
        int maximumTryLimit = 10;
        String linkSuffix;
        while (maximumTryLimit > 0) {
            String originLink = requestParam.getOriginLink();
            originLink += UUID.randomUUID().toString();
            linkSuffix = HashUtil.hashToBase62(originLink);
            String fullShortLink = "https://" + requestParam.getDomain() + "/" + linkSuffix;
            if (!shortLinkBloomFilter.contains(fullShortLink)) {
                return linkSuffix;
            }
            maximumTryLimit--;
        }
        throw new ServiceException(SERVICE_SYSTEM_TIMEOUT);
    }
}
