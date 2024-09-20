package org.alwyn.shortlink.admin.common.exception;

import org.alwyn.shortlink.admin.common.error.ErrorResponse;

public class TPSException extends AbstractException{
    public TPSException(Throwable cause, ErrorResponse errorResponse) {
        super(cause, errorResponse);
    }
}
