package com.eventer.user.web.v1;

import com.eventer.user.mapper.CommentMapper;
import com.eventer.user.security.contracts.CustomUserDetails;
import com.eventer.user.service.CommentService;
import com.eventer.user.service.domain.Comment;
import com.eventer.user.web.dto.comment.CreateCommentDTO;
import com.eventer.user.web.dto.comment.UpdateCommentDTO;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/comment")
public class CommentController implements ResultUnwrapper {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreateCommentDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Result<Comment> result = this.commentService.create(CommentMapper.toRequest(dto), userDetails);

        return this.okOrError(result, CommentMapper::toDTO);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateCommentDTO dto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Result<Comment> result = this.commentService.update(CommentMapper.toRequest(dto), userDetails);

        return this.okOrError(result, CommentMapper::toDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Result result = this.commentService.delete(id, userDetails);

        return this.okOrError(result);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getComments(final Pageable pageable, @PathVariable("eventId") Long eventId) {
        Result<Page<Comment>> result = this.commentService.getComments(pageable, eventId);

        return this.okOrError(result, CommentMapper::toDTOPage);
    }
}
