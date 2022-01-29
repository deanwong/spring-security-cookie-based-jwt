package com.asksrc.comments.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public class BaseEntity {
    private Long id;
    /**
     * Optimistic lock version
     */
    private Integer version;
    private Instant dateCreated;
    private Instant lastUpdated;
}
