package org.alwyn.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.alwyn.shortlink.admin.dao.entity.UserDO;
import org.alwyn.shortlink.admin.dto.req.UserLoginReqDTO;
import org.alwyn.shortlink.admin.dto.req.UserRegistrationReqDTO;
import org.alwyn.shortlink.admin.dto.req.UserUpdateReqDTO;
import org.alwyn.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.alwyn.shortlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);
    Boolean checkUsernameExists(String username);
    void registerUser(UserRegistrationReqDTO reqDTO);
    void updateUserByUsername(UserUpdateReqDTO reqDTO);
    UserLoginRespDTO loginUserByUsernameAndPassword(UserLoginReqDTO reqDTO);
    Boolean checkLoginByUsernameAndLoginToken(String username, String token);
    void logoutUserByUsernameAndLoginToken(String username, String token);
}
