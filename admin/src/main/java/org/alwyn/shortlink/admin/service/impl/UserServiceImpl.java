package org.alwyn.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.admin.common.exception.ClientException;
import org.alwyn.shortlink.admin.common.exception.ServiceException;
import org.alwyn.shortlink.admin.dao.entity.UserDO;
import org.alwyn.shortlink.admin.dao.mapper.UserMapper;
import org.alwyn.shortlink.admin.dto.req.UserLoginReqDTO;
import org.alwyn.shortlink.admin.dto.req.UserRegistrationReqDTO;
import org.alwyn.shortlink.admin.dto.req.UserUpdateReqDTO;
import org.alwyn.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.alwyn.shortlink.admin.dto.resp.UserRespDTO;
import org.alwyn.shortlink.admin.service.GroupService;
import org.alwyn.shortlink.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.alwyn.shortlink.admin.common.constant.RedisConstant.LOCK_USER_REGISTRATION_KEY;
import static org.alwyn.shortlink.admin.common.constant.RedisConstant.USER_LOGIN_KEY;
import static org.alwyn.shortlink.admin.common.error.ErrorResponse.*;

@SuppressWarnings("ALL")
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {
    private final RBloomFilter<String> usernameBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final GroupService groupService;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ServiceException(SERVICE_ERROR);
        }
        UserRespDTO result = new UserRespDTO();
        BeanUtils.copyProperties(userDO, result);
        return result;
    }

    @Override
    public Boolean checkUsernameExists(String username) {
        return usernameBloomFilter.contains(username);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override

    public void registerUser(UserRegistrationReqDTO reqDTO) {
        if (Boolean.TRUE.equals(checkUsernameExists(reqDTO.getUsername()))) {
            throw new ClientException(USER_NAME_EXIST_ERROR);
        }
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTRATION_KEY + reqDTO.getUsername());
        if (!lock.tryLock()) {
            throw new ClientException(CLIENT_ERROR);
        }
        try {
            int inserted = baseMapper.insert(BeanUtil.toBean(reqDTO, UserDO.class));
            if (inserted == 0) {
                throw new ClientException(CLIENT_ERROR);
            }
            usernameBloomFilter.add(reqDTO.getUsername());
            groupService.createGroupByGroupName("default", reqDTO.getUsername());
        } catch (DuplicateKeyException e) {
            throw new ClientException(USER_NAME_EXIST_ERROR);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void updateUserByUsername(UserUpdateReqDTO reqDTO) {
        LambdaUpdateWrapper<UserDO> updateWrapper = Wrappers.lambdaUpdate(UserDO.class)
                .eq(UserDO::getUsername, reqDTO.getUsername());
        baseMapper.update(BeanUtil.toBean(reqDTO, UserDO.class), updateWrapper);
    }

    @Override
    public UserLoginRespDTO loginUserByUsernameAndPassword(UserLoginReqDTO reqDTO) {
        if (!usernameBloomFilter.contains(reqDTO.getUsername())) {
            throw new ClientException(USER_NAME_NOT_EXIST_ERROR);
        }
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, reqDTO.getUsername())
                .eq(UserDO::getPassword, reqDTO.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        if (userDO == null) {
            throw new ClientException(USER_PASSWORD_ERROR);
        }

        Boolean hasLogin = stringRedisTemplate.hasKey("login_" + reqDTO.getUsername());
        if (hasLogin != null && hasLogin) {
            throw new ClientException(USER_HAS_LOGGED_IN_ERROR);
        }

        //query whether the user has login
        Map<Object, Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + reqDTO.getUsername());
        if (CollUtil.isNotEmpty(hasLoginMap)) {
            stringRedisTemplate.expire(USER_LOGIN_KEY + reqDTO.getUsername(), 300L, TimeUnit.DAYS);
            String token = hasLoginMap.keySet().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException(CLIENT_ERROR));
            return new UserLoginRespDTO(token);
        }
        String uuid = UUID.randomUUID().toString();
        stringRedisTemplate.opsForHash().put(USER_LOGIN_KEY + reqDTO.getUsername(), uuid, JSON.toJSONString(userDO));
        //stringRedisTemplate.expire(USER_LOGIN_KEY + reqDTO.getUsername(), 300L, java.util.concurrent.TimeUnit.DAYS);
        return new UserLoginRespDTO(uuid);

    }

    @Override
    public Boolean checkLoginByUsernameAndLoginToken(String username, String token) {
        return stringRedisTemplate.opsForHash().get(USER_LOGIN_KEY + username, token) != null;
    }

    @Override
    public void logoutUserByUsernameAndLoginToken(String username, String token) {
        if (checkLoginByUsernameAndLoginToken(username, token)) {
            stringRedisTemplate.delete(USER_LOGIN_KEY + username);
        } else {
            throw new ClientException(USER_TOKEN_INVALID_ERROR);
        }
    }
}
