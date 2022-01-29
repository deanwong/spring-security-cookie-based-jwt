package com.asksrc.comments.config.error;

/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description 4xx exception
 */
public class BizException extends AppException {

    public BizException(ExceptionCodeEnum code, Object... msgArgs) {
        super(code, msgArgs);
    }

    public BizException(ExceptionCodeEnum code, Throwable cause, Object... msgArgs) {
        super(code, cause, msgArgs);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(String message) {
        super(message);
    }
}