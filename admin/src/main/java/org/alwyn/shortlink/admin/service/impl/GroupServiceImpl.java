package org.alwyn.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.alwyn.shortlink.admin.common.context.UserContext;
import org.alwyn.shortlink.admin.common.exception.ClientException;
import org.alwyn.shortlink.admin.common.exception.ServiceException;
import org.alwyn.shortlink.admin.common.tool.RandomGenerator;
import org.alwyn.shortlink.admin.dao.entity.GroupDO;
import org.alwyn.shortlink.admin.dao.mapper.GroupMapper;
import org.alwyn.shortlink.admin.dto.req.GroupUpdateReqDTO;
import org.alwyn.shortlink.admin.dto.resp.GroupListQueryRespDTO;
import org.alwyn.shortlink.admin.remote.LinkRemoteService;
import org.alwyn.shortlink.admin.service.GroupService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.alwyn.shortlink.admin.common.constant.RedisConstant.LOCK_GROUP_ADD_KEY;
import static org.alwyn.shortlink.admin.common.error.ErrorResponse.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {

    private final RedissonClient redissonClient;
    private final RBloomFilter<String> gidBloomFilter;
    private static final Integer MAX_RETRY_LIMIT = 10;

    LinkRemoteService shortLinkRemoteService = new LinkRemoteService() {
    };

    @Override
    public void createGroupByGroupName(String groupname) {
        createGroupByGroupName(groupname, UserContext.getUserNameFromUserContext());
    }

    @Override
    public void createGroupByGroupName(String groupname, String username) {
        if (username == null) {
            throw new ServiceException(USER_LOGIN_ERROR);
        }
        if (groupname == null) {
            throw new ClientException(GROUP_NAME_NULL_ERROR);
        }
        LambdaQueryWrapper<GroupDO> lamdaQueryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, username)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGroupname, groupname);
        if (baseMapper.selectCount(lamdaQueryWrapper) > 0) {
            GroupDO groupDORecreated = new GroupDO();
            groupDORecreated.setDelFlag(0);
            baseMapper.update(groupDORecreated, lamdaQueryWrapper);
            return;
        }

        RLock lock = redissonClient.getLock(LOCK_GROUP_ADD_KEY + username);
        lock.lock();
        if (!lock.tryLock()) {
            throw new ClientException(SERVICE_ERROR);
        }
        try {
            int tryCount = 0;
            while (tryCount <= MAX_RETRY_LIMIT) {
                String gid = RandomGenerator.generateRandom();
                if (!gidBloomFilter.contains(gid)) {
                    GroupDO groupDO = GroupDO.builder().gid(gid).groupname(groupname).username(username).sortOrder(0).build();
                    baseMapper.insert(BeanUtil.toBean(groupDO, GroupDO.class));
                    gidBloomFilter.add(gid);
                    break;
                }
                tryCount++;
            }
        } catch (DuplicateKeyException ex) {
            throw new ClientException(GROUP_NAME_EXIST_ERROR);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<GroupListQueryRespDTO> listGroup() {
        if (UserContext.getUserNameFromUserContext() == null) {
            throw new ServiceException(USER_LOGIN_ERROR);
        }
        LambdaQueryWrapper<GroupDO> lambdaQueryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUserNameFromUserContext())
                .eq(GroupDO::getDelFlag, 0)
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUpdateTime);
        List<GroupDO> groupDOList = baseMapper.selectList(lambdaQueryWrapper);
        return BeanUtil.copyToList(groupDOList, GroupListQueryRespDTO.class);
    }

    @Override
    public Integer updateGroupByGroupName(GroupUpdateReqDTO requestParam) {
        LambdaUpdateWrapper<GroupDO> lambdaUpdateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUserNameFromUserContext())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, requestParam.getGid());
        GroupDO groupDOUpdated = GroupDO.builder().groupname(requestParam.getGroupname()).build();
        return baseMapper.update(groupDOUpdated, lambdaUpdateWrapper);
    }

    @Override
    public Integer deleteGroupByGid(String gid) {
        LambdaUpdateWrapper<GroupDO> lambdaUpdateWrapper = Wrappers.lambdaUpdate(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUserNameFromUserContext())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getGid, gid);
        GroupDO groupDOUpdated = new GroupDO();
        groupDOUpdated.setDelFlag(1);
        return baseMapper.update(groupDOUpdated, lambdaUpdateWrapper);
    }
}
