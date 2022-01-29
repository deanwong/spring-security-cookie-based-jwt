package com.asksrc.comments.mapper;

import com.asksrc.comments.dto.CommentDTO;
import com.asksrc.comments.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public interface CommentMapper {

    Comment toEntity(CommentDTO commentDTO);

    CommentDTO toDTO(Comment comment);

    List<CommentDTO> commentsToCommentsDTOs(List<Comment> comments);
}
