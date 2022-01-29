package com.asksrc.comments.dto;

import lombok.Data;

import java.time.Instant;

@Data
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public class BaseDTO {

    private Long id;
    /**
     * Optimistic lock version
     */
    private Integer version;
    private Instant dateCreated;
    private Instant lastUpdated;

}
