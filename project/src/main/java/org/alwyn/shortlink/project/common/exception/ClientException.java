package org.alwyn.shortlink.project.common.exception;

import org.alwyn.shortlink.project.common.error.ErrorResponse;

public class ClientException extends AbstractException {
    public ClientException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
    public ClientException(Throwable cause, ErrorResponse errorResponse) {
        super(cause, errorResponse);
    }
}
