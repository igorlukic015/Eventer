package com.eventer.user.service;

import com.eventer.user.contracts.comment.CreateCommentRequest;
import com.eventer.user.contracts.comment.UpdateCommentRequest;
import com.eventer.user.security.contracts.CustomUserDetails;
import com.eventer.user.service.domain.Comment;
import com.github.igorlukic015.resulter.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Result<Page<Comment>> getComments(Pageable pageable, Long eventId);

    Result<Comment> create(CreateCommentRequest request, CustomUserDetails userDetails);

    Result<Comment> update(UpdateCommentRequest request, CustomUserDetails userDetails);

    Result delete(Long id, CustomUserDetails userDetails);
}
