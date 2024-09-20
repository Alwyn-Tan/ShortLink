package org.alwyn.shortlink.admin.common.exception;

import org.alwyn.shortlink.admin.common.error.ErrorResponse;
import org.springframework.util.StringUtils;

public abstract class AbstractException extends RuntimeException{
    public final String errorCode;
    public final String errorMessage;
    public AbstractException(ErrorResponse errorResponse) {
        this.errorCode = errorResponse.code();
        this.errorMessage = StringUtils.hasLength(errorResponse.message()) ? errorResponse.message() : "UNDEFINED ERROR";
    }
    public AbstractException(Throwable throwable, ErrorResponse errorResponse) {
        super(throwable);
        this.errorCode = errorResponse.code();
        this.errorMessage = StringUtils.hasLength(errorResponse.message()) ? errorResponse.message() : "UNDEFINED ERROR";
    }
}
