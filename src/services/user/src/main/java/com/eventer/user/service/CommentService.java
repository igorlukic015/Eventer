package com.eventer.user.service;

import com.eventer.user.service.domain.Comment;
import com.github.igorlukic015.resulter.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Result<Page<Comment>> getComments(Pageable pageable, Long eventId);
}
