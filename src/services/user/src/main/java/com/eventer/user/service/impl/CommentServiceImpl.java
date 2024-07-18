package com.eventer.user.service.impl;

import com.eventer.user.contracts.comment.CreateCommentRequest;
import com.eventer.user.data.model.User;
import com.eventer.user.data.repository.CommentRepository;
import com.eventer.user.data.repository.UserRepository;
import com.eventer.user.mapper.CommentMapper;
import com.eventer.user.security.contracts.CustomUserDetails;
import com.eventer.user.service.CommentService;
import com.eventer.user.service.domain.Comment;
import com.eventer.user.utils.ResultErrorMessages;
import com.github.igorlukic015.resulter.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Optional;


@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;;
    private final UserRepository userRepository;


    public CommentServiceImpl(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
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

    @Transactional
    @Override
    public Result<Comment> create(CreateCommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        Optional<User> foundUser =
                this.userRepository.findByUsername(((CustomUserDetails) authentication.getPrincipal()).getUsername());

        if (foundUser.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        Result<Comment> partialCommentOrError = Comment.partialCreate(request.text(), request.eventId());

        if (partialCommentOrError.isFailure()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.fromError(partialCommentOrError);
        }

        com.eventer.user.data.model.Comment comment = CommentMapper.toModel(partialCommentOrError.getValue(), foundUser.get());

        comment = this.commentRepository.save(comment);

        return CommentMapper.toDomain(comment);
    }
}
