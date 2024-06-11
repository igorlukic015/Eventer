package com.eventer.user.mapper;

import com.eventer.user.config.ApplicationConfiguration;
import com.eventer.user.service.domain.Comment;
import com.eventer.user.web.dto.comment.CommentDTO;
import com.github.igorlukic015.resulter.Result;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
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
                domain.getUserProfileImageUrl());
    }

}
