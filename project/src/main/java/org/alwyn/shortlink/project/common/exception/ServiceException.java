package org.alwyn.shortlink.project.common.exception;

import org.alwyn.shortlink.project.common.error.ErrorResponse;

public class ServiceException extends AbstractException {
    private String message;
    public ServiceException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
    public ServiceException(String message){
        super(null, null);
        this.message = message;
    }
    public ServiceException(Throwable cause, ErrorResponse errorResponse) {
        super(cause, errorResponse);
    }
}
