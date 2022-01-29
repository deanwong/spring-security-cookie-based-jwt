package com.asksrc.comments.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public class Account extends BaseEntity {

    private String username;
    private String email;
    private String password;
    private Integer status;

}
