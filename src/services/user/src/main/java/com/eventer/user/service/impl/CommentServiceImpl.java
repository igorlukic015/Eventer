package com.eventer.user.service.impl;

import com.eventer.user.contracts.comment.CreateCommentRequest;
import com.eventer.user.contracts.comment.UpdateCommentRequest;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Objects;
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
    public Result<Comment> create(CreateCommentRequest request, CustomUserDetails userDetails) {
        Optional<User> foundUser =
                this.userRepository.findByUsername(userDetails.getUsername());

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

    @Override
    public Result<Comment> update(UpdateCommentRequest request, CustomUserDetails userDetails) {
        Optional<User> foundUser =
                this.userRepository.findByUsername(userDetails.getUsername());

        if (foundUser.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        Optional<com.eventer.user.data.model.Comment> foundComment = this.commentRepository.findById(request.id());

        if (foundComment.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.commentNotFound);
        }

        if (!Objects.equals(foundComment.get().getUser().getUsername(), foundUser.get().getUsername())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.editCommentNotAllowed);
        }

        com.eventer.user.data.model.Comment comment = foundComment.get();

        comment.setText(request.text());

        comment = this.commentRepository.save(comment);

        return CommentMapper.toDomain(comment);
    }

    @Override
    public Result delete(Long id, CustomUserDetails userDetails) {
        Optional<User> foundUser =
                this.userRepository.findByUsername(userDetails.getUsername());

        if (foundUser.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.userNotFound);
        }

        Optional<com.eventer.user.data.model.Comment> foundComment = this.commentRepository.findById(id);

        if (foundComment.isEmpty()) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.commentNotFound);
        }

        if (!Objects.equals(foundComment.get().getUser().getUsername(), foundUser.get().getUsername())) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.invalid(ResultErrorMessages.editCommentNotAllowed);
        }

        this.commentRepository.delete(foundComment.get());

        return Result.success();
    }
}
