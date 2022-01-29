package com.asksrc.comments.config.error;

/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description API error code
 */
public enum ExceptionCodeEnum {

    /**
     * violate the username policy
     */
    INVALID_USERNAME(10001, "Username is invalid"),
    /**
     * violate the password policy
     */
    INVALID_PASSWORD(10002, "Password is invalid"),
    /**
     * violate the email policy
     */
    INVALID_EMAIL(10003, "Email is invalid"),
    EXIST_USERNAME(10004, "Username already exists"),
    EXIST_EMAIL(10005, "Email already exists"),
    BAD_CREDENTIAL(10006, "Username or password is incorrect");

    private int code;
    private String messageTemplate;

    ExceptionCodeEnum(int code, String messageTemplate) {
        this.code = code;
        this.messageTemplate = messageTemplate;
    }

    public int getCode() {
        return code;
    }

    public String getMessageTemplate() {
        return messageTemplate;
    }
}
