package com.asksrc.comments.dto;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public class CommentDTO extends BaseDTO {

    private Long pid;
    private Long aid;

    @Size(min = 3, max = 200)
    private String content;
    private Integer depth;
    private Integer status;

    private List<CommentDTO> replies;
    private String author;
}
