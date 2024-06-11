package com.eventer.user.mapper;

import com.eventer.user.config.ApplicationConfiguration;
import com.eventer.user.contracts.comment.CreateCommentRequest;
import com.eventer.user.service.domain.Comment;
import com.eventer.user.web.dto.comment.CommentDTO;
import com.eventer.user.web.dto.comment.CreateCommentDTO;
import com.github.igorlukic015.resulter.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CreateCommentRequest toRequest(CreateCommentDTO dto) {
        return new CreateCommentRequest(dto.text(), dto.eventId());
    }

    public static Result<Page<Comment>> toDomainPage(
            Page<com.eventer.user.data.model.Comment> models) {
        Result<List<Comment>> commentsOrError =
                Result.getResultValueSet(
                        models.stream().map(CommentMapper::toDomain).toList(), Collectors.toList());

        if (commentsOrError.isFailure()) {
            return Result.fromError(commentsOrError);
        }

        Page<Comment> result =
                new PageImpl<>(
                        commentsOrError.getValue(),
                        models.getPageable(),
                        models.getTotalElements());

        return Result.success(result);
    }

    public static Result<Comment> toDomain(com.eventer.user.data.model.Comment model) {
        String userProfileImageUrl =
                String.format(
                        "%s/%s/%s",
                        ApplicationConfiguration.getImageBaseUrl(),
                        ApplicationConfiguration.getImageBucketName(),
                        model.getUser().getProfileImage().getName());

        return Comment.create(
                model.getId(),
                model.getText(),
                model.getEventId(),
                model.getUser().getId(),
                model.getUser().getUsername(),
                userProfileImageUrl);
    }

    public static Page<CommentDTO> toDTOPage(Page<Comment> comments) {
        List<CommentDTO> dtos =comments.stream().map(CommentMapper::toDTO).toList();

        return new PageImpl<>(dtos, comments.getPageable(), comments.getTotalElements());
    }

    public static CommentDTO toDTO(Comment domain) {
        return new CommentDTO(
                domain.getId(),
                domain.getText(),
                domain.getEventId(),
                domain.getUserId(),
                domain.getUserUsername(),
                domain.getUserProfileImageUrl());
    }

    public static com.eventer.user.data.model.Comment toModel(Comment domain, com.eventer.user.data.model.User user) {
        com.eventer.user.data.model.Comment comment = new com.eventer.user.data.model.Comment();

        comment.setText(domain.getText());
        comment.setEventId(domain.getEventId());
        comment.setUser(user);

        return comment;
    }
}
