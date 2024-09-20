package org.alwyn.shortlink.admin.common.exception;

import org.alwyn.shortlink.admin.common.error.ErrorResponse;

public class ServiceException extends AbstractException{
    public ServiceException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
    public ServiceException(Throwable cause, ErrorResponse errorResponse) {
        super(cause, errorResponse);
    }
}
