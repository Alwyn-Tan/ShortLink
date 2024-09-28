package org.alwyn.shortlink.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.alwyn.shortlink.admin.common.result.Result;
import org.alwyn.shortlink.admin.common.result.Results;
import org.alwyn.shortlink.admin.dto.req.UserLoginReqDTO;
import org.alwyn.shortlink.admin.dto.req.UserRegistrationReqDTO;
import org.alwyn.shortlink.admin.dto.req.UserUpdateReqDTO;
import org.alwyn.shortlink.admin.dto.resp.UnmaskedUserRespDTO;
import org.alwyn.shortlink.admin.dto.resp.UserLoginRespDTO;
import org.alwyn.shortlink.admin.dto.resp.UserRespDTO;
import org.alwyn.shortlink.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/api/short-link/admin/users/masked_user/{username}")
    public Result<UserRespDTO> getUserByUserName(@PathVariable("username") String username) {
        return Results.success(userService.getUserByUsername(username));
    }

    @GetMapping("/api/short-link/admin/users/unmasked_user/{username}")
    public Result<UnmaskedUserRespDTO> getUnmaskedUserByUsername(@PathVariable("username") String username) {
        return Results.success(BeanUtil.toBean(userService.getUserByUsername(username), UnmaskedUserRespDTO.class));
    }

    /**
     * The request for a non-existing record in Redis will be redirected to the database, and too many requests for such
     * record will press the DB, such situation is called Cache Penetration, to avoid this, we use Bloom Filter to judge
     * whether the record exists or not ahead of querying Redis.
     *
     * @param username* @return
     */
    @GetMapping("/api/short-link/admin/users/existence")
    public Result<Boolean> checkUsernameExists(@RequestParam("username") String username) {
        return Results.success(userService.checkUsernameExists(username));
    }

    @PostMapping("/api/short-link/admin/users/registration")
    public Result<Void> registerUser(@RequestBody UserRegistrationReqDTO reqDTO) {
        userService.registerUser(reqDTO);
        return Results.success();
    }

    @PutMapping("/api/short-link/admin/users/update")
    public Result<Void> updateUser(@RequestBody UserUpdateReqDTO reqDTO) {
        userService.updateUserByUsername(reqDTO);
        return Results.success();
    }

    @PostMapping("/api/short-link/admin/users/login")
    public Result<UserLoginRespDTO> loginUserByUsernameAndPassword(@RequestBody UserLoginReqDTO reqDTO) {
        return Results.success(userService.loginUserByUsernameAndPassword(reqDTO));
    }

    @GetMapping("/api/short-link/admin/users/check-login")
    public Result<Boolean> checkLoginByUsernameAndLoginToken(@RequestParam("username") String username, @RequestParam("loginToken") String loginToken) {
        return Results.success(userService.checkLoginByUsernameAndLoginToken(username, loginToken));
    }

    @DeleteMapping("/api/short-link/admin/users/logout")
    public Result<Void> logoutUserByUsernameAndLoginToken(@RequestParam("username") String username, @RequestParam("loginToken") String loginToken) {
        userService.logoutUserByUsernameAndLoginToken(username, loginToken);
        return Results.success();
    }


}
