package com.asksrc.comments.service;

import com.asksrc.comments.dto.CommentDTO;

import java.util.List;

/**
 * @Author wangding
 * @Date 2022/1/25
 */
public interface CommentService {

    /**
     * fetch all comments
     *
     * @return comment list which only contains root comments
     */
    List<CommentDTO> findAll();

    /**
     * add a comment
     *
     * @param commentDTO must contains pid and content, aid would be found from security session
     * @return comment
     * @throws com.asksrc.comments.config.error.EntityNotFoundException if not login
     */
    CommentDTO add(CommentDTO commentDTO);
}
