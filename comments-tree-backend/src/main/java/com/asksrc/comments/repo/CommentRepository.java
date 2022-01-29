package com.asksrc.comments.repo;

import com.asksrc.comments.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public interface CommentRepository {

    List<Comment> findAll();

    Optional<Comment> findById(Long id);

    void create(Comment comment);
}
