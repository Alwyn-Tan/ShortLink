package org.alwyn.shortlink.project.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.project.dao.entity.LinkDO;
import org.alwyn.shortlink.project.dao.mapper.LinkMapper;
import org.alwyn.shortlink.project.dto.req.LinkReqDTO;
import org.alwyn.shortlink.project.dto.req.RecycleBinPageQueryReqDTO;
import org.alwyn.shortlink.project.dto.resp.LinkPageQueryRespDTO;
import org.alwyn.shortlink.project.service.RecycleBinService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import static org.alwyn.shortlink.project.common.constant.RedisConstant.LINK_ROUTE_KEY;
import static org.alwyn.shortlink.project.common.constant.RedisConstant.NULL_LINK_KEY;

@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl extends ServiceImpl<LinkMapper, LinkDO> implements RecycleBinService {

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void addLinkIntoRecycleBin(LinkReqDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortLink, requestParam.getFullShortLink())
                .eq(LinkDO::getEnableStatus, 1)
                .eq(LinkDO::getDelFlag, 0);
        LinkDO selectedLinkDO = LinkDO.builder().enableStatus(0).build();
        baseMapper.update(selectedLinkDO, updateWrapper);
        stringRedisTemplate.delete(String.format(LINK_ROUTE_KEY, requestParam.getFullShortLink()));
    }

    @Override
    public IPage<LinkPageQueryRespDTO> queryRecycleBinPage(RecycleBinPageQueryReqDTO requestParam) {
        if(requestParam.getGidList() == null || requestParam.getGidList().size() == 0){
            System.out.println("NULL");
        }
        LambdaQueryWrapper<LinkDO> queryWrapper = Wrappers.lambdaQuery(LinkDO.class)
                .in(LinkDO::getGid, requestParam.getGidList())
                .eq(LinkDO::getEnableStatus, 0)
                .eq(LinkDO::getDelFlag, 0)
                .orderByDesc(LinkDO::getUpdateTime);
        IPage<LinkDO> resultPage = baseMapper.selectPage(requestParam, queryWrapper);
        return resultPage.convert(each -> {
            return BeanUtil.toBean(each, LinkPageQueryRespDTO.class);
        });
    }

    @Override
    public void recoverLinkFromRecycleBin(LinkReqDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortLink, requestParam.getFullShortLink())
                .eq(LinkDO::getEnableStatus, 0)
                .eq(LinkDO::getDelFlag, 0);
        LinkDO selectedLinkDO = LinkDO.builder().enableStatus(1).build();
        baseMapper.update(selectedLinkDO, updateWrapper);
        stringRedisTemplate.delete(String.format(NULL_LINK_KEY, requestParam.getFullShortLink()));
    }

    @Override
    public void deleteLinkInRecycleBin(LinkReqDTO requestParam) {
        LambdaUpdateWrapper<LinkDO> updateWrapper = Wrappers.lambdaUpdate(LinkDO.class)
                .eq(LinkDO::getGid, requestParam.getGid())
                .eq(LinkDO::getFullShortLink, requestParam.getFullShortLink())
                .eq(LinkDO::getEnableStatus, 0)
                .eq(LinkDO::getDelFlag, 0);
        baseMapper.delete(updateWrapper);
    }
}
