package org.alwyn.shortlink.admin.common.exception;

import org.alwyn.shortlink.admin.common.error.ErrorResponse;

public class ClientException extends AbstractException{
    public ClientException(ErrorResponse errorResponse) {
        super(errorResponse);
    }
    public ClientException(Throwable cause, ErrorResponse errorResponse) {
        super(cause, errorResponse);
    }
}
