package com.asksrc.comments.dto.payload;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public class LoginRequest {
    @NotBlank(message = "Username or Email cannot be empty")
    private String loginId;

    @NotBlank(message = "Password cannot be empty")
    private String password;

    private boolean rememberMe;
}