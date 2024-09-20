package org.alwyn.shortlink.admin.common.context;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Optional;

public final class UserContext {
    private static final ThreadLocal<UserContextDTO> USER_THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static void setUserContext(UserContextDTO userContextDTO) {
        USER_THREAD_LOCAL.set(userContextDTO);
    }

    public static String getUserIdFromUserContext() {
        UserContextDTO userContextDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userContextDTO).map(UserContextDTO::getUserId).orElse(null);
    }

    public static String getUserNameFromUserContext() {
        UserContextDTO userContextDTO = USER_THREAD_LOCAL.get();
        return Optional.ofNullable(userContextDTO).map(UserContextDTO::getUsername).orElse(null);
    }

    public static void removeUserContext() {
        USER_THREAD_LOCAL.remove();
    }
}
