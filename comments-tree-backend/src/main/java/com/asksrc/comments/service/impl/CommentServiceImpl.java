package com.asksrc.comments.service.impl;

import com.asksrc.comments.config.security.AuthUser;
import com.asksrc.comments.dto.AccountDTO;
import com.asksrc.comments.dto.CommentDTO;
import com.asksrc.comments.entity.Comment;
import com.asksrc.comments.mapper.CommentMapper;
import com.asksrc.comments.repo.CommentRepository;
import com.asksrc.comments.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Override
    public List<CommentDTO> findAll() {
        List<Comment> entities = commentRepository.findAll();
        List<CommentDTO> dtos = commentMapper.commentsToCommentsDTOs(entities);
        toTree(dtos);
        return dtos;
    }

    private void toTree(List<CommentDTO> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return;
        }
        // convert list to map
        Map<Long, CommentDTO> commentTree = dtos.stream().collect(Collectors.toMap(CommentDTO::getId, Function.identity()));
        for (CommentDTO commentDTO : dtos) {
            if (isRoot(commentDTO.getPid())) {
                continue;
            }
            // get parent comment from map
            CommentDTO parent = commentTree.get(commentDTO.getPid());
            // Exceptional case, skip
            if (parent == null) {
                continue;
            }
            if (parent.getReplies() == null) {
                parent.setReplies(new ArrayList<>());
            }
            parent.getReplies().add(commentDTO);
        }
        // only output root
        dtos.removeIf(commentDTO -> !isRoot(commentDTO.getPid()));
    }

    @Override
    public CommentDTO add(CommentDTO commentDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        AccountDTO accountDTO = null;
        if (auth.getPrincipal() instanceof AuthUser) {
            AuthUser authUser = (AuthUser) auth.getPrincipal();
            accountDTO = authUser.getAccount();
        }
        if (accountDTO == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        log.info("found current login user [{}]", accountDTO.getUsername());
        commentDTO.setAid(accountDTO.getId());
        commentDTO.setDepth(calDepth(commentDTO.getPid()));
        Comment entity = commentMapper.toEntity(commentDTO);
        commentRepository.create(entity);
        entity = commentRepository.findById(entity.getId()).orElse(null);
        return commentMapper.toDTO(entity);
    }

    private boolean isRoot(Long pid) {
        return pid == null || pid == 0L;
    }
    /**
     * Currently, not necessary to call this method
     *
     * @param pid
     * @return depth if root then 1
     */
    private Integer calDepth(Long pid) {
        if (isRoot(pid)) {
            return 1;
        }
        Optional<Comment> parent = commentRepository.findById(pid);
        if (parent.isEmpty()) {
            return 1;
        }
        return 1 + parent.get().getDepth();
    }
}
