package com.asksrc.comments.config.error;

import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description general exception
 */
public class AppException extends RuntimeException {

    @Getter
    private String module;

    @Getter
    private Integer code;

    public AppException(ExceptionCodeEnum exceptionCode, Throwable cause, Object... msgArgs) {
        super(resolveExceptionMessage(exceptionCode.getMessageTemplate(), msgArgs), cause);
        this.code = exceptionCode.getCode();
    }

    public AppException(ExceptionCodeEnum exceptionCode, Object... msgArgs) {
        super(resolveExceptionMessage(exceptionCode.getMessageTemplate(), msgArgs));
        this.code = exceptionCode.getCode();
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message) {
        super(message);
    }

    private static String resolveExceptionMessage(String messageTemplate, Object[] msgArgs) {
        return MessageFormatter.arrayFormat(messageTemplate, msgArgs).getMessage();
    }

}
