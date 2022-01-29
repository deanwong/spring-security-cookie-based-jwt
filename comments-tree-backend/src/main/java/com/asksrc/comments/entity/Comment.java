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
public class Comment extends BaseEntity {

    private Long pid;
    private Long aid;
    private String content;
    private Integer depth;
    private Integer status;

    /**
     * from account
     */
    private String author;
}
