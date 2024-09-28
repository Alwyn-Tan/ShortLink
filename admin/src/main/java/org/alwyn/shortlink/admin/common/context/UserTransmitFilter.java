package org.alwyn.shortlink.admin.common.context;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.alwyn.shortlink.admin.common.error.ErrorResponse;
import org.alwyn.shortlink.admin.common.exception.ClientException;
import org.alwyn.shortlink.admin.common.result.Results;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {
    private final StringRedisTemplate stringRedisTemplate;

    private static final List<String> ignoreURLList = Lists.newArrayList(
            "/api/short-link/admin/users/login",
            "/api/short-link/admin/users/existence"
    );

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        if (!ignoreURLList.contains(requestURI)) {
            String method = httpServletRequest.getMethod();
            if (!(Objects.equals(requestURI, "/api/short-link/admin/users/registration") && Objects.equals(method, "POST"))) {
                String username = httpServletRequest.getHeader("username");
                String token = httpServletRequest.getHeader("loginToken");

                // username or token is empty
                if (!StrUtil.isAllNotBlank(username, token)) {
                    returnJson((HttpServletResponse) servletResponse, JSON.toJSONString(Results.failure(new ClientException(ErrorResponse.USER_TOKEN_INVALID_ERROR))));
                    return;
                }

                Object userInfoJsonString;
                try {
                    userInfoJsonString = stringRedisTemplate.opsForHash().get("login_" + username, token);
                    if (userInfoJsonString == null) {
                        throw new ClientException(ErrorResponse.USER_TOKEN_INVALID_ERROR);
                    }
                } catch (Exception e) {
                    returnJson((HttpServletResponse) servletResponse, JSON.toJSONString(Results.failure(new ClientException(ErrorResponse.USER_TOKEN_INVALID_ERROR))));
                    return;
                }
                UserContextDTO userContextDTO = JSON.parseObject(userInfoJsonString.toString(), UserContextDTO.class);
                UserContext.setUserContext(userContextDTO);
            }
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUserContext();
        }

    }


    private void returnJson(HttpServletResponse response, String json) throws Exception {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try {
            writer = response.getWriter();
            writer.print(json);

        } catch (IOException e) {
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}