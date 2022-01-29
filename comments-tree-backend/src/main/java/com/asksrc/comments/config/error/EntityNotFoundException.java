package com.asksrc.comments.config.error;

/**
 * @Author wangding
 * @Date 2022/1/24
 * @Description 404 exception
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
