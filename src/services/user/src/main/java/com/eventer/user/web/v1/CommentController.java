package com.eventer.user.web.v1;

import com.eventer.user.mapper.CommentMapper;
import com.eventer.user.service.CommentService;
import com.eventer.user.service.domain.Comment;
import com.github.igorlukic015.resulter.Result;
import com.github.igorlukic015.resulter.ResultUnwrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/comment")
public class CommentController implements ResultUnwrapper {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<?> getComments(final Pageable pageable, @PathVariable("eventId") Long eventId) {
        Result<Page<Comment>> result = this.commentService.getComments(pageable, eventId);

        return this.okOrError(result, CommentMapper::toDTOPage);
    }
}
