package org.alwyn.shortlink.project.common.error;

public enum ErrorResponse {
    CLIENT_ERROR("A000001", "Client Error"),
    USER_REGISTER_ERROR("A000100", "User Registration Error"),
    USER_NAME_EXIST_ERROR("A000111", "User Name Exists Error"),
    USER_NAME_NOT_EXIST_ERROR("A000112", "User Name Not Exists Error"),
    GROUP_NAME_EXIST_ERROR("A000120", "Group Name Exists Error"),
    GROUP_NAME_NULL_ERROR("A000121", "Group Name Null Error"),
    USER_LOGIN_ERROR("A000200", "Uer Login Error"),
    USER_TOKEN_INVALID_ERROR("A000210", "User Token Invalid Error"),
    LINK_ERROR("A000300", "Link Error"),
    LINK_EXISTS_ERROR("A000301", "Link Exist Error"),
    LINK_NOT_EXIST_ERROR("A000302", "Link Not Exist Error"),
    SERVICE_ERROR("B000001", "Service Error"),
    SERVICE_SYSTEM_TIMEOUT("B000100", "System Timeout"),
    REMOTE_ERROR("C000001", "Third Party Service Error"),
    AMAP_ERROR("C000100", "AMAP API Error");
    private final String code;

    private final String message;

    ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
