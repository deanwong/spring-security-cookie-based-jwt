package com.asksrc.comments.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public class AccountDTO extends BaseDTO {

    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    private Integer status;
}
