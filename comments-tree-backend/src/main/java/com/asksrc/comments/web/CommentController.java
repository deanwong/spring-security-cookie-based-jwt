package com.asksrc.comments.web;

import com.asksrc.comments.dto.CommentDTO;
import com.asksrc.comments.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Slf4j
/**
 * @Author wangding
 * @Date 2022/1/25
 */
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping()
    public ResponseEntity<?> list() {
        log.info(">>> request comment list");
        List<CommentDTO> dtos = commentService.findAll();
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/{pid}")
    public ResponseEntity<?> add(@Valid @RequestBody CommentDTO commentDTO, @PathVariable("pid") Long pid) {
        log.info(">>> request add comment under [{}]", pid);
        CommentDTO newComment = commentService.add(commentDTO);
        return ResponseEntity.ok(newComment);
    }

}
