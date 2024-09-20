package org.alwyn.shortlink.admin.common.result;

import org.alwyn.shortlink.admin.common.error.ErrorResponse;
import org.alwyn.shortlink.admin.common.exception.AbstractException;

public class Results {
    public static <T> Result<T> success(T data){
        return new Result<T>().setCode(Result.SUCCESS_CODE).setData(data);
    }

    public static Result<Void> success() {
        return new Result<Void>()
                .setCode(Result.SUCCESS_CODE);
    }

    public static  Result<Void> failure(AbstractException abstractException){
        return new Result<Void>()
                .setCode(abstractException.errorCode)
                .setMessage(abstractException.errorMessage);
    }

    public static Result<Void> failure(){
        return new Result<Void>()
                .setCode(ErrorResponse.SERVICE_ERROR.code())
                .setMessage(ErrorResponse.SERVICE_ERROR.message());
    }
}
