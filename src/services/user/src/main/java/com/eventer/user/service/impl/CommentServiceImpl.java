package com.eventer.user.service.impl;

import com.eventer.user.data.repository.CommentRepository;
import com.eventer.user.mapper.CommentMapper;
import com.eventer.user.service.CommentService;
import com.eventer.user.service.UserService;
import com.eventer.user.service.domain.Comment;
import com.github.igorlukic015.resulter.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;

    public CommentServiceImpl(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    @Override
    public Result<Page<Comment>> getComments(Pageable pageable, Long eventId) {
        Page<com.eventer.user.data.model.Comment> foundComments =
                this.commentRepository.findAllByEventId(eventId, pageable);

        Result<Page<Comment>> commentsOrError = CommentMapper.toDomainPage(foundComments);

        if (commentsOrError.isFailure()) {
            return Result.fromError(commentsOrError);
        }

        return commentsOrError;
    }
}
