package org.alwyn.shortlink.project.common.exception;

import org.alwyn.shortlink.project.common.error.ErrorResponse;

public class TPSException extends AbstractException {
    public TPSException(Throwable cause, ErrorResponse errorResponse) {
        super(cause, errorResponse);
    }
}
